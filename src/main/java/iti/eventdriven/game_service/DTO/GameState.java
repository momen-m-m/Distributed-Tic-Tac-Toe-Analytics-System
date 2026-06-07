package iti.eventdriven.game_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameState {
    private String gameId;
    private char[][] board;
    private char currentTurn;
    private GameStatus status;
    private char humanSymbol;
    private GameDifficulty difficultyLevel;
    private int moveNumber;
}