package iti.eventdriven.analytics_service.service.impl;

import iti.eventdriven.analytics_service.dto.*;
import iti.eventdriven.analytics_service.mapper.GameMapper;
import iti.eventdriven.analytics_service.model.Game;
import iti.eventdriven.analytics_service.repository.AnalyticsRepository;
import iti.eventdriven.analytics_service.service.AnalyticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public void processMove(GameMove move) {
        Game game = analyticsRepository.findById(move.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found: " + move.getGameId()));
        game.getMoves().add(move);
        analyticsRepository.save(game);
    }

    @Override
    public void processStartGame(GameRequest gameRequest) {
        Game game = new Game();
        game.setGameId(gameRequest.getGameId());
        game.setHumanSymbol(gameRequest.getHumanSymbol());
        game.setAiSymbol((gameRequest.getHumanSymbol() == 'X') ? 'O' : 'X');
        game.setDifficultyLevel(gameRequest.getDifficultyLevel());
        analyticsRepository.save(game);
    }


    @Override
    public void processEndGame(GameFinished endGame) {
        Game game = analyticsRepository.findById(endGame.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found: " + endGame.getGameId()));
        game.setEndTime(endGame.getFinishedAt());
        game.setGameStatus(endGame.getResult());
        game.setWinnerSymbol(endGame.getWinnerSymbol());
        game.setNumberOfMoves(endGame.getTotalMoves());
        analyticsRepository.save(game);
    }

    @Override
    public AnalyticsSummary getSummary() {
        return buildSummary(
                analyticsRepository.count(),
                analyticsRepository.countByGameStatus(GameStatus.AIWon),
                analyticsRepository.countByGameStatus(GameStatus.HumanWon),
                analyticsRepository.countByGameStatus(GameStatus.Draw),
                Optional.ofNullable(analyticsRepository.findAvgNumberOfMoves()).orElse(0.0)
        );
    }

    @Override
    public AnalyticsSummary getSummaryByDifficulty(GameDifficulty difficulty) {
        return buildSummary(
                analyticsRepository.countByDifficultyLevel(difficulty),
                analyticsRepository.countByDifficultyLevelAndGameStatus(difficulty, GameStatus.AIWon),
                analyticsRepository.countByDifficultyLevelAndGameStatus(difficulty, GameStatus.HumanWon),
                analyticsRepository.countByDifficultyLevelAndGameStatus(difficulty, GameStatus.Draw),
                Optional.ofNullable(analyticsRepository.findAvgNumberOfMovesByDifficulty(difficulty)).orElse(0.0)
        );
    }

    @Override
    public Game getGameById(String gameId) {
        return analyticsRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));
    }

    @Override
    public PageResponse<GameSummary> getAllGames(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime"));
        Page<Game> gamePage = analyticsRepository.findAll(pageable);

        List<GameSummary> summaries = GameMapper.toGameSummaryList(gamePage.getContent());

        return PageResponse.<GameSummary>builder()
                .content(summaries)
                .currentPage(gamePage.getNumber())
                .pageSize(gamePage.getSize())
                .totalElements(gamePage.getTotalElements())
                .totalPages(gamePage.getTotalPages())
                .isFirst(gamePage.isFirst())
                .isLast(gamePage.isLast())
                .build();
    }

    private AnalyticsSummary buildSummary(long total, long aiWins, long humanWins, long draws, double avgMoves) {
        return AnalyticsSummary.builder()
                .totalGames(total)
                .aiWins(aiWins)
                .humanWins(humanWins)
                .draws(draws)
                .aiWinRate(total > 0 ? (double) aiWins / total * 100 : 0)
                .humanWinRate(total > 0 ? (double) humanWins / total * 100 : 0)
                .drawRate(total > 0 ? (double) draws / total * 100 : 0)
                .avgMovesPerGame(avgMoves)
                .build();
    }
}
