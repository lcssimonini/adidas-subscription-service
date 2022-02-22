package com.simonini.adidas.subscriptionapi.integrations.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendEmailQueueConfig {

    public static final String SEND_EMAIL_QUEUE_NAME = "send-email-queue";

    @Bean
    public Queue myQueue() {
        return new Queue(SEND_EMAIL_QUEUE_NAME, false);
    }
}
