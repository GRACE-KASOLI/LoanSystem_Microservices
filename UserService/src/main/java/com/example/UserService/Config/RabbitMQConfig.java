package com.example.UserService.Config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String LOGIN_EXCHANGE = "login-exchange";
    public static final String ADMIN_QUEUE = "admin-login-queue";
    public static final String USER_QUEUE = "user-login-queue";
    public static final String ADMIN_ROUTING_KEY = "admin";
    public static final String USER_ROUTING_KEY = "user";

    @Bean
    public DirectExchange loginExchange() {
        return new DirectExchange(LOGIN_EXCHANGE);
    }

    @Bean
    public Queue adminQueue() {
        return new Queue(ADMIN_QUEUE, true);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    @Bean
    public Binding adminBinding(Queue adminQueue, DirectExchange loginExchange) {
        return BindingBuilder.bind(adminQueue).to(loginExchange).with(ADMIN_ROUTING_KEY);
    }

    @Bean
    public Binding userBinding(Queue userQueue, DirectExchange loginExchange) {
        return BindingBuilder.bind(userQueue).to(loginExchange).with(USER_ROUTING_KEY);
    }

    // ✅ Configure RabbitMQ to use JSON Serialization
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
