package com.simonini.adidas.emailservice.listener.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailResponse implements Serializable {

  private String entityId;
  private LocalDateTime sentAt;
}
