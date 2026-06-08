package iti.eventdriven.analytics_service.service;

import iti.eventdriven.analytics_service.dto.GameFinished;
import iti.eventdriven.analytics_service.dto.GameMove;
import iti.eventdriven.analytics_service.dto.GameRequest;

public interface MessagingService {

    void receiveMove(GameMove move);

    void receiveStartGame(GameRequest gameRequest);

    void receiveEndGame(GameFinished endGame);
}
