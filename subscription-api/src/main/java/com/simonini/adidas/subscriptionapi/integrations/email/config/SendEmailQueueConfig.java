package com.simonini.adidas.subscriptionapi.integrations.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendEmailQueueConfig {

  @Value("${email.send-queue}")
  private String sendEmailQueue;

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

  @Bean
  public Queue sendEmailQueue() {
    return new Queue(sendEmailQueue, false);
  }
}
