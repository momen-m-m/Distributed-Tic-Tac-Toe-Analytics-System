package iti.eventdriven.analytics_service.model;

import iti.eventdriven.analytics_service.dto.GameDifficulty;
import iti.eventdriven.analytics_service.dto.GameMove;
import iti.eventdriven.analytics_service.dto.GameStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "games")
public class Game {
    @Id
    private String gameId;
    private GameStatus gameStatus;
    private char humanSymbol;
    private char aiSymbol;
    private char winnerSymbol; // "X", "O", or null if draw
    private GameDifficulty difficultyLevel;
    private Integer numberOfMoves;
    private LocalDateTime endTime;
    private List<GameMove> moves = new ArrayList<>();

}
