package com.example.UserService.Config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @RabbitListener(queues = NotificationConfig.EMAIL_QUEUE)
    public void processEmailNotification(String message) {
        System.out.println("Email Notification Received: " + message);
    }

    @RabbitListener(queues = NotificationConfig.SMS_QUEUE)
    public void processSmsNotification(String message) {
        System.out.println("SMS Notification Received: " + message);
    }
}

