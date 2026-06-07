package iti.eventdriven.game_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameRequest {
    String gameId;
    char humanSymbol;
    GameDifficulty difficultyLevel;
}
