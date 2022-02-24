package com.simonini.adidas.emailservice.listener;

import static com.simonini.adidas.emailservice.util.TestUtil.readFromResources;
import static org.mockito.Mockito.*;

import com.simonini.adidas.emailservice.helper.SerializeHelper;
import com.simonini.adidas.emailservice.listener.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.sender.EmailDLQSender;
import com.simonini.adidas.emailservice.sender.EmailFeedbackSender;
import com.simonini.adidas.emailservice.validator.SendRequestValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailMessageListenerTest {

  @Mock private SendRequestValidator validator;
  @Mock private EmailFeedbackSender feedbackSender;
  @Mock private EmailDLQSender emailDLQSender;
  @Mock private SerializeHelper serializeHelper;
  @InjectMocks private EmailMessageListener emailMessageListener;

  private final SendEmailRequest emailRequest =
      SendEmailRequest.builder()
          .entityId("62167db8978b0168c31cf242")
          .email("string@string.com")
          .message("Subscription for newsletter [string] was created for email [string@string.com]")
          .build();

  private final String emailRequestString = readFromResources("send-email-message.json");

  @Test
  void shouldHandleEmailMessage() throws Exception {
    when(serializeHelper.getEmailRequestMessage(emailRequestString)).thenReturn(emailRequest);
    when(validator.validateRequest(emailRequest)).thenReturn(new ArrayList<>());
    emailMessageListener.listenEmailMessage(emailRequestString);
    verify(feedbackSender, times(1)).sendEmailFeedback(emailRequest);
  }

  @Test
  void shouldHandleInvalidEmailMessage() throws Exception {
    when(serializeHelper.getEmailRequestMessage(emailRequestString)).thenReturn(emailRequest);
    List<String> validationErrors = List.of("validationError");
    when(validator.validateRequest(emailRequest)).thenReturn(validationErrors);
    emailMessageListener.listenEmailMessage(emailRequestString);
    verify(feedbackSender, times(0)).sendEmailFeedback(emailRequest);
    verify(emailDLQSender, times(1)).sendDLQMessage(emailRequest, validationErrors);
  }
}
