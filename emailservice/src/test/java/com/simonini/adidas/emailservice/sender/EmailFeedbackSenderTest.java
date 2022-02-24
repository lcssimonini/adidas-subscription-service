package com.simonini.adidas.emailservice.sender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.simonini.adidas.emailservice.helper.SerializeHelper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.listener.dto.SendEmailResponse;
import com.simonini.adidas.emailservice.sender.impl.EmailFeedbackSenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmailFeedbackSenderTest {

  @Mock private RabbitTemplate queueSender;
  @Mock private SerializeHelper serializeHelper;
  @InjectMocks private EmailFeedbackSenderImpl emailFeedbackSender;

  private final String feedbackQueueName = "queue-name";
  private final SendEmailRequest emailRequest =
      SendEmailRequest.builder()
          .entityId("62167db8978b0168c31cf242")
          .email("string@string.com")
          .message("Subscription for newsletter [string] was created for email [string@string.com]")
          .build();

  @Test
  void shouldSendEmailFeedback() throws Exception {
    ReflectionTestUtils.setField(
        emailFeedbackSender, "sendEmailFeedbackQueueName", feedbackQueueName);
    when(serializeHelper.getFeedbackMessageString(any()))
        .thenReturn(SendEmailResponse.builder().build().toString());
    emailFeedbackSender.sendEmailFeedback(emailRequest);

    ArgumentCaptor<SendEmailResponse> argument = ArgumentCaptor.forClass(SendEmailResponse.class);
    verify(serializeHelper).getFeedbackMessageString(argument.capture());
    verify(queueSender, times(1)).convertAndSend(anyString(), anyString());

    assertEquals(emailRequest.getEntityId(), argument.getValue().getEntityId());
    assertNotNull(argument.getValue().getSentAt());
  }
}
