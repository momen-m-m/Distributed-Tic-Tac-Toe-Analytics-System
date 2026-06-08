package iti.eventdriven.game_service.service;

import iti.eventdriven.game_service.dto.GameFinished;
import iti.eventdriven.game_service.dto.GameMove;
import iti.eventdriven.game_service.dto.GameRequest;

public interface MessagingService {
    void sendMove(GameMove move);

    void sendStartGame(GameRequest gameRequest); ;

    void sendEndGame(GameFinished endGame);
}
