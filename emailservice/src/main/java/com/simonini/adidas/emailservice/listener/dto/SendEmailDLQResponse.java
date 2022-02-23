package com.simonini.adidas.emailservice.listener.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDLQResponse implements Serializable {

  private String entityId;
  private List<String> errorMessages;
}
