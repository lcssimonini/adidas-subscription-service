package com.simonini.adidas.subscriptionapi.email;

import static com.simonini.adidas.subscriptionapi.util.TestUtil.readFromResources;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailResponse;
import com.simonini.adidas.subscriptionapi.integrations.email.impl.SendEmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class SendEmailServiceTest {

  @Mock private ObjectMapper objectMapper;
  @Mock private RabbitTemplate queueSender;
  @InjectMocks private SendEmailServiceImpl sendEmailService;

  private final String sendEmailRequestMessage = readFromResources("send-email-request.json");
  private final SendEmailRequest sendEmailRequest =
      SendEmailRequest.builder()
          .entityId("62167db8978b0168c31cf242")
          .email("string@string.com")
          .message("Subscription for newsletter [string] was created for email [string@string.com]")
          .build();
  private final String sendEmailQueue = "sendEmailQueue";

  @Test
  void shouldSendEmailMessage() throws Exception {
    ReflectionTestUtils.setField(sendEmailService, "sendEmailQueueName", sendEmailQueue);
    when(objectMapper.writeValueAsString(sendEmailRequest)).thenReturn(sendEmailRequestMessage);
    SendEmailResponse response = sendEmailService.sendEmail(sendEmailRequest);
    verify(queueSender, times(1)).convertAndSend(sendEmailQueue, sendEmailRequestMessage);
    assertEquals(sendEmailRequest.getEntityId(), response.getEntityId());
    assertNotNull(response.getSentAt());
    assertFalse(response.isAttemptFailure());
  }

  @Test
  void shouldHandleSendFailure() throws Exception {
    ReflectionTestUtils.setField(sendEmailService, "sendEmailQueueName", sendEmailQueue);
    when(objectMapper.writeValueAsString(sendEmailRequest)).thenThrow(RuntimeException.class);
    SendEmailResponse response = sendEmailService.sendEmail(sendEmailRequest);
    verify(queueSender, times(0)).convertAndSend(sendEmailQueue, sendEmailRequestMessage);
    assertNull(response.getEntityId());
    assertNull(response.getSentAt());
    assertTrue(response.isAttemptFailure());
  }
}
