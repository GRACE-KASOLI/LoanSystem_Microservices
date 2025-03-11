package com.example.UserService.Config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    public static final String FANOUT_EXCHANGE = "notification-exchange";
    public static final String EMAIL_QUEUE = "email-notification-queue";
    public static final String SMS_QUEUE = "sms-notification-queue";

    @Bean
    public FanoutExchange notificationExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Queue smsQueue() {
        return new Queue(SMS_QUEUE, true);
    }

    @Bean
    public Binding bindEmailQueue(Queue emailQueue, FanoutExchange notificationExchange) {
        return BindingBuilder.bind(emailQueue).to(notificationExchange);
    }

    @Bean
    public Binding bindSmsQueue(Queue smsQueue, FanoutExchange notificationExchange) {
        return BindingBuilder.bind(smsQueue).to(notificationExchange);
    }
}

