package com.simonini.adidas.subscriptionapi.email;

import static com.simonini.adidas.subscriptionapi.util.TestUtil.readFromResources;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.integrations.email.EmailFeedbackListener;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailResponse;
import com.simonini.adidas.subscriptionapi.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailFeedbackListenerTest {

  @Mock private ObjectMapper objectMapper;
  @Mock private SubscriptionService subscriptionService;
  @InjectMocks private EmailFeedbackListener emailFeedbackListener;

  private final String emailFeedbackMessage = readFromResources("email-feedback-message.json");
  private final String entityId = "62167db8978b0168c31cf242";
  private final Subscription subscription = Subscription.builder().id(entityId).build();
  private final SendEmailResponse sendEmailResponse =
      SendEmailResponse.builder().entityId(entityId).build();

  @Test
  void shouldHandleFeedbackMessage() throws Exception {
    when(objectMapper.readValue(emailFeedbackMessage, SendEmailResponse.class))
        .thenReturn(sendEmailResponse);
    when(subscriptionService.findById(entityId)).thenReturn(subscription);
    emailFeedbackListener.listenEmailFeedback(emailFeedbackMessage);
    verify(objectMapper, times(1)).readValue(emailFeedbackMessage, SendEmailResponse.class);
    verify(subscriptionService, times(1)).findById(entityId);
    verify(subscriptionService, times(1)).save(subscription);
  }
}
