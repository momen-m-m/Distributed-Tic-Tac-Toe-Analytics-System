package iti.eventdriven.analytics_service.repository;

import iti.eventdriven.analytics_service.dto.GameDifficulty;
import iti.eventdriven.analytics_service.dto.GameStatus;
import iti.eventdriven.analytics_service.dto.GameSummary;
import iti.eventdriven.analytics_service.dto.PageResponse;
import iti.eventdriven.analytics_service.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnalyticsRepository extends MongoRepository<Game, String> {
    long countByGameStatus(GameStatus status);
    long countByDifficultyLevel(GameDifficulty difficulty);
    long countByDifficultyLevelAndGameStatus(GameDifficulty difficulty, GameStatus status);
    Page<Game> findAll(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { numberOfMoves: { $ne: null } } }",
            "{ $group: { _id: null, avg: { $avg: '$numberOfMoves' } } }"
    })
    Double findAvgNumberOfMoves();

    @Aggregation(pipeline = {
            "{ $match: { difficultyLevel: ?0, numberOfMoves: { $ne: null } } }",
            "{ $group: { _id: null, avg: { $avg: '$numberOfMoves' } } }"
    })
    Double findAvgNumberOfMovesByDifficulty(GameDifficulty difficulty);
}
