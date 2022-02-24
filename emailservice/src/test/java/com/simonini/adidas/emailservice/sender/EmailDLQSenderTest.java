package com.simonini.adidas.emailservice.sender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.simonini.adidas.emailservice.helper.SerializeHelper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailDLQResponse;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.sender.impl.EmailDLQSenderImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class EmailDLQSenderTest {

  @Mock private RabbitTemplate queueSender;
  @Mock private SerializeHelper serializeHelper;
  @InjectMocks private EmailDLQSenderImpl emailDLQSender;

  private final String feedbackQueueName = "queue-name";
  private final SendEmailRequest emailRequest =
      SendEmailRequest.builder()
          .entityId("62167db8978b0168c31cf242")
          .message("Subscription for newsletter [string] was created for email [string@string.com]")
          .build();

  List<String> errorList = List.of("email cannot be blank");

  @Test
  void shouldSendDLQMessage() throws Exception {
    ReflectionTestUtils.setField(emailDLQSender, "sendEmailDLQName", feedbackQueueName);
    when(serializeHelper.getDLQMessageString(any()))
        .thenReturn(SendEmailDLQResponse.builder().build().toString());

    emailDLQSender.sendDLQMessage(emailRequest, errorList);

    ArgumentCaptor<SendEmailDLQResponse> argument =
        ArgumentCaptor.forClass(SendEmailDLQResponse.class);
    verify(serializeHelper).getDLQMessageString(argument.capture());
    verify(queueSender, times(1)).convertAndSend(anyString(), anyString());

    assertEquals(emailRequest.getEntityId(), argument.getValue().getEntityId());
    assertEquals(errorList, argument.getValue().getErrorMessages());
  }
}
