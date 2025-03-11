package com.example.UserService.Config;

import com.example.UserService.LoginRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public LoginQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendLoginRequest(LoginRequest request, String role) {
        String routingKey = role.equalsIgnoreCase("admin") ? "admin" : "user";
        rabbitTemplate.convertAndSend(RabbitMQConfig.LOGIN_EXCHANGE, routingKey, request);
        System.out.println("Sent login request for " + request.getEmail() + " to " + routingKey + " queue.");
    }
}
