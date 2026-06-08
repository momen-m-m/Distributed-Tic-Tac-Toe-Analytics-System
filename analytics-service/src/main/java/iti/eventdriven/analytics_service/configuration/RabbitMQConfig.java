package iti.eventdriven.analytics_service.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "game.exchange";
    public static final String ROUTING_KEY_STARTED = "game.started";
    public static final String ROUTING_KEY_MOVE = "game.move";
    public static final String ROUTING_KEY_FINISHED = "game.finished";
    public static final String FINISHED_QUEUE = "game.finished.queue";
    public static final String STARTED_QUEUE = "game.started.queue";
    public static final String MOVE_QUEUE = "game.move.queue";


    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate = new org.springframework.amqp.rabbit.core.RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
