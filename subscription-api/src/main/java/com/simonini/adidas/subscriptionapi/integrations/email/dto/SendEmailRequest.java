package com.simonini.adidas.subscriptionapi.integrations.email.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest implements Serializable {

  private String entityId;
  private String email;
  private String message;
}
