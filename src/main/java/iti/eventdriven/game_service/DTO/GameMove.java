package iti.eventdriven.game_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameMove {
    String gameId;
    char symbol;
    private String playerType; // "HUMAN" or "AI"
    int row;
    int col;
    private int moveNumber;
}
