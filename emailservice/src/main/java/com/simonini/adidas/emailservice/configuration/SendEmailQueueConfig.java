package com.simonini.adidas.emailservice.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendEmailQueueConfig {

    @Value("${email.feedback-queue}")
    private String sendEmailFeedbackQueueName;

    @Value("${email.dlq}")
    private String sendEmailDLQName;

    @Bean
    public Queue sendEmailFeedbackQueue() {
        return new Queue(sendEmailFeedbackQueueName, false);
    }

    @Bean
    public Queue sendEmailDLQ() {
        return new Queue(sendEmailDLQName, false);
    }
}
