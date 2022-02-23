package com.simonini.adidas.emailservice.listener.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest implements Serializable {

  @NotBlank(message = "EntityID cannot be empty")
  private String entityId;

  @Email(message = "Wrong email format")
  private String email;

  @NotBlank(message = "Message cannot be empty")
  private String message;
}
