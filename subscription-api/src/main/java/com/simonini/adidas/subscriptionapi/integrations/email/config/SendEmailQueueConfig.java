package com.simonini.adidas.subscriptionapi.integrations.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendEmailQueueConfig {

  @Value("${email.send-queue}")
  private String sendEmailQueue;

  @Bean
  public Queue sendEmailQueue() {
    return new Queue(sendEmailQueue, false);
  }
}
