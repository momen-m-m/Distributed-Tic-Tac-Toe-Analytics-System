package iti.eventdriven.game_service.service;

import iti.eventdriven.game_service.DTO.GameDifficulty;
import iti.eventdriven.game_service.DTO.GameState;

public interface GameService {

    /**
     * Creates a new game session and initializes the board.
     */
    GameState startNewGame(char humanSymbol, GameDifficulty difficultyLevel);

    /**
     * Returns the current state of a game.
     */
    GameState getGame(String gameId);

    /**
     * Handles a human move and returns updated state.
     */
    GameState playHumanMove(String gameId, int row, int col);

    /**
     * Triggers AI move using Minimax and returns updated state.
     */
    GameState playAIMove(String gameId);

}