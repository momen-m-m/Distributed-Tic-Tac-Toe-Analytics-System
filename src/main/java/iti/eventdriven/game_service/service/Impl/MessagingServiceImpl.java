package iti.eventdriven.game_service.service.Impl;

import iti.eventdriven.game_service.DTO.GameFinished;
import iti.eventdriven.game_service.DTO.GameMove;
import iti.eventdriven.game_service.DTO.GameRequest;
import iti.eventdriven.game_service.DTO.GameState;
import iti.eventdriven.game_service.configuration.RabbitMQConfig;
import iti.eventdriven.game_service.service.MessagingService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final RabbitTemplate rabbitTemplate;

    public MessagingServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMove(GameMove move) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_MOVE, move);
    }

    @Override
    public void sendStartGame(GameRequest gameRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_STARTED, gameRequest);
    }

    @Override
    public void sendEndGame(GameFinished endGame) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_FINISHED, endGame);
    }
}
