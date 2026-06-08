package iti.eventdriven.analytics_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameMove {
    private String gameId;
    private char symbol;
    private String playerType; // "HUMAN" or "AI"
    private int row;
    private int col;
    private int moveNumber;
}
