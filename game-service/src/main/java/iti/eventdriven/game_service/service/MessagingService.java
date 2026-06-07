package iti.eventdriven.game_service.service;

import iti.eventdriven.game_service.DTO.GameFinished;
import iti.eventdriven.game_service.DTO.GameMove;
import iti.eventdriven.game_service.DTO.GameRequest;
import iti.eventdriven.game_service.DTO.GameState;

public interface MessagingService {
    void sendMove(GameMove move);

    void sendStartGame(GameRequest gameRequest); ;

    void sendEndGame(GameFinished endGame);
}
