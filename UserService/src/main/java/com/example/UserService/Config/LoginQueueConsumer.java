package com.example.UserService.Config;

import com.example.UserService.LoginRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LoginQueueConsumer {

    @RabbitListener(queues = RabbitMQConfig.ADMIN_QUEUE)
    public void processAdminLogin(LoginRequest request) {
        System.out.println("✅ Received ADMIN login request: " + request);
        // TODO: Handle admin login logic
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void processUserLogin(LoginRequest request) {
        System.out.println("✅ Received USER login request: " + request);
        // TODO: Handle user login logic
    }
}
