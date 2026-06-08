package iti.eventdriven.game_service.service.Impl;

import iti.eventdriven.game_service.dto.*;
import iti.eventdriven.game_service.game.BoardChecker;
import iti.eventdriven.game_service.game.MinMaxEngine;
import iti.eventdriven.game_service.service.GameService;
import iti.eventdriven.game_service.service.MessagingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameServiceImpl implements GameService {

    private final MessagingService messagingService;
    private final BoardChecker boardChecker;
    private final MinMaxEngine minMaxEngine;

    private final Map<String, GameState> games = new ConcurrentHashMap<>();

    public GameServiceImpl(MessagingService messagingService, BoardChecker boardChecker, MinMaxEngine minMaxEngine) {
        this.messagingService = messagingService;
        this.boardChecker = boardChecker;
        this.minMaxEngine = minMaxEngine;
    }

    @Override
    public GameState startNewGame(char humanSymbol, GameDifficulty difficultyLevel) {
        String gameId = UUID.randomUUID().toString();

        GameState state = new GameState();
        state.setGameId(gameId);
        state.setBoard(new char[3][3]);
        state.setHumanSymbol(humanSymbol);
        state.setDifficultyLevel(difficultyLevel);
        state.setCurrentTurn(humanSymbol); // human always starts
        state.setStatus(GameStatus.Active);
        state.setMoveNumber(0);

        games.put(gameId, state);

        // notify dashboard — fire and forget
        GameRequest event = new GameRequest(gameId, humanSymbol, difficultyLevel);
        messagingService.sendStartGame(event);

        return state;
    }

    @Override
    public GameState getGame(String gameId) {
        GameState state = games.get(gameId);
        if (state == null) throw new RuntimeException("Game not found: " + gameId);
        return state;
    }

    @Override
    public GameState playHumanMove(String gameId, int row, int col) {
        GameState state = getGame(gameId);
        if (state.getStatus() != GameStatus.Active) throw new RuntimeException("Game is not active: " + gameId);
        if (state.getBoard()[row][col] != '\0') throw new RuntimeException("Cell is already occupied: " + row + "," + col);

        // update board
        char symbol = state.getHumanSymbol();

        applyMove(state, row, col, symbol, "Human");
        checkAndFinalise(state);
        if (state.getStatus() == GameStatus.Active)
            state.setCurrentTurn(state.getHumanSymbol() == 'X' ? 'O' : 'X'); // switch to AI
        return state;
    }

    @Override
    public GameState playAIMove(String gameId) {
        GameState state = getGame(gameId);
        char aiSymbol  = state.getHumanSymbol() == 'X' ? 'O' : 'X';
        char humanSymbol = state.getHumanSymbol();

        int[] move =  minMaxEngine.bestMove(state.getBoard(), aiSymbol, humanSymbol, state.getDifficultyLevel());

        applyMove(state, move[0], move[1], aiSymbol, "AI");
        checkAndFinalise(state);
        if (state.getStatus() == GameStatus.Active)
            state.setCurrentTurn(state.getHumanSymbol()); // switch back to human
        return state;
    }

    private void applyMove(GameState state, int row, int col, char symbol, String playerType) {
        state.getBoard()[row][col] = symbol;
        state.setMoveNumber(state.getMoveNumber() + 1);

        GameMove move = new GameMove(state.getGameId(), symbol,
                playerType, row, col, state.getMoveNumber());

        messagingService.sendMove(move);
    }

    private void checkAndFinalise(GameState state) {
        char aiSymbol = state.getHumanSymbol() == 'X' ? 'O' : 'X';
        char humanSymbol = state.getHumanSymbol();

        if (boardChecker.hasWon(state.getBoard(), aiSymbol)) {
            state.setStatus(GameStatus.AIWon);
            messagingService.sendEndGame(
                    new GameFinished(state.getGameId(), GameStatus.AIWon, aiSymbol, state.getMoveNumber(), LocalDateTime.now())
            );
        } else if (boardChecker.hasWon(state.getBoard(), humanSymbol)) {
            state.setStatus(GameStatus.HumanWon);
            messagingService.sendEndGame(
                    new GameFinished(state.getGameId(), GameStatus.HumanWon, humanSymbol, state.getMoveNumber(), LocalDateTime.now())
            );
        } else if (boardChecker.isBoardFull(state.getBoard())) {
            state.setStatus(GameStatus.Draw);
            messagingService.sendEndGame(
                    new GameFinished(state.getGameId(), GameStatus.Draw, '\0', state.getMoveNumber(), LocalDateTime.now())
            );
        }
    }
}

