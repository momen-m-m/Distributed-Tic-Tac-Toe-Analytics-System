package iti.eventdriven.game_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GameFinished {
    private String gameId;
    private GameStatus result;   // HumanWon, AIWon, Draw
    private char winnerSymbol; // "X", "O", or null if draw
    private int totalMoves;
    private LocalDateTime finishedAt;
}
