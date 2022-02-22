package com.simonini.adidas.emailservice.receiver;

import com.simonini.adidas.emailservice.receiver.dto.SendEmailDLQResponse;
import com.simonini.adidas.emailservice.receiver.dto.SendEmailRequest;
import com.simonini.adidas.emailservice.sender.EmailFeedbackSender;
import com.simonini.adidas.emailservice.validator.SendRequestValidator;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageReceiver {

    private final SendRequestValidator validator;
    private final EmailFeedbackSender feedbackSender;
    private final RabbitTemplate queueSender;

    @Value("${email.dlq}")
    private String sendEmailDLQName;

    @RabbitListener(queues = "${email.send-queue}")
    public void listenEmailMessage(SendEmailRequest request) {
        log.info("received message to send email: {}", request);
        List<String> errorMessages = validator.validateRequest(request);
        if (errorMessages.isEmpty()) {
            handleValidMessage(request);
        } else {
            handleInvalidMessage(request, errorMessages);
        }
    }

    private void handleInvalidMessage(SendEmailRequest request, List<String> errorMessages) {
        log.info("Invalid payload, email cannot be sent, sending DLQ message");
        queueSender.convertAndSend(sendEmailDLQName, buildDLQMessage(request, errorMessages));
    }

    private void handleValidMessage(SendEmailRequest request) {
        log.info("Valid payload, email sent. Sending feedback message");
        feedbackSender.sendEmailFeedback(request);
    }

    private SendEmailDLQResponse buildDLQMessage(SendEmailRequest request, List<String> errorMessages) {
        return SendEmailDLQResponse.builder()
                .entityId(request.getEntityId())
                .errorMessages(errorMessages)
                .build();
    }
}
