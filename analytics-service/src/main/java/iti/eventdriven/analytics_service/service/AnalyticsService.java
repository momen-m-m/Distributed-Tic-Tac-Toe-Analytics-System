package iti.eventdriven.analytics_service.service;

import iti.eventdriven.analytics_service.dto.*;
import iti.eventdriven.analytics_service.model.Game;

public interface AnalyticsService {

    void processMove(GameMove move);

    void processStartGame(GameRequest gameRequest);

    void processEndGame(GameFinished endGame);
    AnalyticsSummary getSummary();
    AnalyticsSummary getSummaryByDifficulty(GameDifficulty difficulty);

    Game getGameById(String gameId);
    PageResponse<GameSummary> getAllGames(int page, int size);
}
