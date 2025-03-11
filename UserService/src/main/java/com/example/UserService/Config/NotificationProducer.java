package com.example.UserService.Config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend(NotificationConfig.FANOUT_EXCHANGE, "", message);
        System.out.println("Broadcasted notification: " + message);
    }
}

