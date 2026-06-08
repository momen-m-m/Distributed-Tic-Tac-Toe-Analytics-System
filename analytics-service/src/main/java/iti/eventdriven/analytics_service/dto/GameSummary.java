package iti.eventdriven.analytics_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameSummary {
    private String gameId;
    private GameStatus gameStatus;
    private GameDifficulty difficultyLevel;
    private char winnerSymbol;
    private Integer numberOfMoves;
    private LocalDateTime endTime;
}