package com.simonini.adidas.subscriptionapi.integrations.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailResponse implements Serializable {

    private String entityId;
    private boolean isAttemptFailure;
    private LocalDateTime sentAt;
}
