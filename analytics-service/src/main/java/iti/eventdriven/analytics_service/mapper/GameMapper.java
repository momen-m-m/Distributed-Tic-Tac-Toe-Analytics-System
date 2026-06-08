package iti.eventdriven.analytics_service.mapper;

import iti.eventdriven.analytics_service.dto.GameSummary;
import iti.eventdriven.analytics_service.model.Game;

import java.util.List;

public class GameMapper {
    public static GameSummary toGameSummary(Game game){
            return GameSummary.builder()
                    .gameId(game.getGameId())
                    .gameStatus(game.getGameStatus())
                    .difficultyLevel(game.getDifficultyLevel())
                    .winnerSymbol(game.getWinnerSymbol())
                    .numberOfMoves(game.getNumberOfMoves())
                    .endTime(game.getEndTime())
                    .build();
    }

    public static  List<GameSummary> toGameSummaryList(List<Game> games){
        return games.stream()
                .map(GameMapper::toGameSummary)
                .toList();
    }
}
