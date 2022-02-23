package com.simonini.adidas.emailservice.sender;

import static com.simonini.adidas.emailservice.util.LogUtil.asJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.listener.dto.SendEmailResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailFeedbackSender {

  @Value("${email.feedback-queue}")
  private String sendEmailFeedbackQueueName;

  private final RabbitTemplate queueSender;
  private final ObjectMapper objectMapper;

  public void sendEmailFeedback(SendEmailRequest sendEmailRequest) {
    log.info("Attempt to send email feedback: {}", asJson(sendEmailRequest));
    try {
      SendEmailResponse response =
          SendEmailResponse.builder()
              .entityId(sendEmailRequest.getEntityId())
              .sentAt(LocalDateTime.now())
              .build();
      queueSender.convertAndSend(
          sendEmailFeedbackQueueName, objectMapper.writeValueAsString(response));
    } catch (Exception e) {
      log.error("Error trying to send email feedback to queue", e);
    }
  }
}
