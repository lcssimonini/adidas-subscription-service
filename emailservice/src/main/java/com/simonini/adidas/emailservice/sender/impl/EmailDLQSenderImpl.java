package com.simonini.adidas.emailservice.sender.impl;

import static com.simonini.adidas.emailservice.util.LogUtil.asJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simonini.adidas.emailservice.helper.SerializeHelper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailDLQResponse;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.sender.EmailDLQSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailDLQSenderImpl implements EmailDLQSender {

  @Value("${email.dlq}")
  private String sendEmailDLQName;

  private final RabbitTemplate queueSender;
  private final SerializeHelper serializeHelper;

  @Override
  public void sendDLQMessage(SendEmailRequest request, List<String> errorMessages) {
    try {
      SendEmailDLQResponse sendEmailDLQResponse = buildDLQMessage(request, errorMessages);
      log.info(
          "Invalid payload, email cannot be sent, sending DLQ message: {}",
          asJson(sendEmailDLQResponse));
      queueSender.convertAndSend(
          sendEmailDLQName, serializeHelper.getDLQMessageString(sendEmailDLQResponse));
    } catch (JsonProcessingException e) {
      log.error("Error trying to send dlq message to queue", e);
    }
  }

  private SendEmailDLQResponse buildDLQMessage(
      SendEmailRequest request, List<String> errorMessages) {
    return SendEmailDLQResponse.builder()
        .entityId(request.getEntityId())
        .errorMessages(errorMessages)
        .build();
  }
}
