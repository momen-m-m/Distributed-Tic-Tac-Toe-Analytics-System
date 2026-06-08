package iti.eventdriven.analytics_service.service.impl;

import iti.eventdriven.analytics_service.dto.GameFinished;
import iti.eventdriven.analytics_service.dto.GameMove;
import iti.eventdriven.analytics_service.dto.GameRequest;
import iti.eventdriven.analytics_service.repository.AnalyticsRepository;
import iti.eventdriven.analytics_service.service.AnalyticsService;
import iti.eventdriven.analytics_service.service.MessagingService;
import iti.eventdriven.analytics_service.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final AnalyticsService analyticsService;

    public MessagingServiceImpl(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.MOVE_QUEUE)
    public void receiveMove(GameMove move) {
        analyticsService.processMove(move);
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.STARTED_QUEUE)
    public void receiveStartGame(GameRequest gameRequest) {
            analyticsService.processStartGame(gameRequest);
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.FINISHED_QUEUE)
    public void receiveEndGame(GameFinished endGame) {
        analyticsService.processEndGame(endGame);
    }
}
