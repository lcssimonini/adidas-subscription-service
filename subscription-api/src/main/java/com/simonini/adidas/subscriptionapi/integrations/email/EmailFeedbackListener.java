package com.simonini.adidas.subscriptionapi.integrations.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailResponse;
import com.simonini.adidas.subscriptionapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailFeedbackListener {

    private final ObjectMapper objectMapper;
    private final SubscriptionService subscriptionService;

    @RabbitListener(queues = "${email.feedback-queue}")
    public void emailFeedbackListener(String feedbackMessage) throws JsonProcessingException {
        log.info("received email feedback message");
        SendEmailResponse response = objectMapper.readValue(feedbackMessage, SendEmailResponse.class);
        handleEmailFeedback(response);
    }

    private void handleEmailFeedback(SendEmailResponse response) {
        Subscription subscription = subscriptionService.findById(response.getEntityId());
        subscription.setEmailSent(true);
        subscriptionService.save(subscription);
    }
}
