package iti.eventdriven.game_service.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameDifficulty {
    Easy(1),
    Medium(3),
    Hard(5);

    private final int maxDepth;
}
