package iti.eventdriven.analytics_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsSummary {
    private long totalGames;
    private long aiWins;
    private long humanWins;
    private long draws;
    private double aiWinRate;
    private double humanWinRate;
    private double drawRate;
    private double avgMovesPerGame;
}