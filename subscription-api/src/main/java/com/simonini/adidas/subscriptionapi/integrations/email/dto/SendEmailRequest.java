package com.simonini.adidas.subscriptionapi.integrations.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest implements Serializable {

    private String entityId;
    private String email;
    private String message;
}
