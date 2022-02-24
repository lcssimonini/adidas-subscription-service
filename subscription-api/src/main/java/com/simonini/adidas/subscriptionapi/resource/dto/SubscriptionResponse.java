package com.simonini.adidas.subscriptionapi.resource.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionResponse {

  private String id;
  private String email;
  private String firstName;
  private String gender;
  private String dateOfBirth;
  private boolean consentSubscribe;
  private boolean emailSent;
  private boolean canceled;
  private String newsletterId;
}
