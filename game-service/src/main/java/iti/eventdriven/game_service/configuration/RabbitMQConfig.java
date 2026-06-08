package iti.eventdriven.game_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME        = "game.exchange";
    public static final String ROUTING_KEY_STARTED  = "game.started";
    public static final String ROUTING_KEY_MOVE     = "game.move";
    public static final String ROUTING_KEY_FINISHED = "game.finished";


    @Bean public DirectExchange gameExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue finishedQueue() { return new Queue("game.finished.queue"); }
    @Bean
    public Queue startedQueue()  { return new Queue("game.started.queue"); }
    @Bean
    public Queue moveQueue()     { return new Queue("game.move.queue"); }

    @Bean public Binding finishedBinding() {
        return BindingBuilder.bind(finishedQueue()).to(gameExchange()).with(ROUTING_KEY_FINISHED);
    }
    @Bean public Binding startedBinding() {
        return BindingBuilder.bind(startedQueue()).to(gameExchange()).with(ROUTING_KEY_STARTED);
    }
    @Bean public Binding moveBinding() {
        return BindingBuilder.bind(moveQueue()).to(gameExchange()).with(ROUTING_KEY_MOVE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
