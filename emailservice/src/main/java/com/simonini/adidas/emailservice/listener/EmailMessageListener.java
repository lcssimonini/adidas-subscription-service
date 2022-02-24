package com.simonini.adidas.emailservice.listener;

import static com.simonini.adidas.emailservice.util.LogUtil.asJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simonini.adidas.emailservice.helper.SerializeHelper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.sender.EmailDLQSender;
import com.simonini.adidas.emailservice.sender.EmailFeedbackSender;
import com.simonini.adidas.emailservice.validator.SendRequestValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageListener {

  private final SendRequestValidator validator;
  private final EmailFeedbackSender feedbackSender;
  private final EmailDLQSender emailDLQSender;
  private final SerializeHelper serializeHelper;

  @Value("${email.dlq}")
  private String sendEmailDLQName;

  @RabbitListener(queues = "${email.send-queue}")
  public void listenEmailMessage(String requestString) throws JsonProcessingException {
    SendEmailRequest request = serializeHelper.getEmailRequestMessage(requestString);
    log.info("received message to send email: {}", asJson(request));
    List<String> errorMessages = validator.validateRequest(request);
    if (errorMessages.isEmpty()) {
      handleValidMessage(request);
    } else {
      handleInvalidMessage(request, errorMessages);
    }
  }

  private void handleInvalidMessage(SendEmailRequest request, List<String> errorMessages) {
    log.info("Invalid payload, email sent. Sending message to DLQ");
    emailDLQSender.sendDLQMessage(request, errorMessages);
  }

  private void handleValidMessage(SendEmailRequest request) {
    log.info("Valid payload, email sent. Sending feedback message");
    feedbackSender.sendEmailFeedback(request);
  }
}
