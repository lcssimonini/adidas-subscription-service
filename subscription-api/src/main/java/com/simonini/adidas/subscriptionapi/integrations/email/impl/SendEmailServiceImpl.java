package com.simonini.adidas.subscriptionapi.integrations.email.impl;

import com.simonini.adidas.subscriptionapi.integrations.email.SendEmailService;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.simonini.adidas.subscriptionapi.integrations.email.config.SendEmailQueueConfig.SEND_EMAIL_QUEUE_NAME;
import static com.simonini.adidas.subscriptionapi.util.LogUtil.asJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final RabbitTemplate queueSender;

    @Override
    public SendEmailResponse sendEmail(SendEmailRequest sendEmailRequest) {
        log.info("Attempt to send email: {}", asJson(sendEmailRequest));
        SendEmailResponse response;
        try {
            queueSender.convertAndSend(SEND_EMAIL_QUEUE_NAME, sendEmailRequest);
            response = SendEmailResponse.builder()
                    .entityId(sendEmailRequest.getEntityId())
                    .sentAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Error trying to send email request to queue", e);
            response = SendEmailResponse.builder()
                    .isAttemptFailure(true)
                    .build();
        }
        return response;
    }
}
