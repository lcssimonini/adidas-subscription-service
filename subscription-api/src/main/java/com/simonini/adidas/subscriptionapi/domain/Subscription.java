package com.simonini.adidas.subscriptionapi.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class Subscription {

  private String id;
  private String email;
  private String firstName;
  private String gender;
  private String dateOfBirth;
  private boolean consent;
  private boolean canceled;
  private String newsletterId;
  private boolean emailSent;
  private LocalDateTime createdAt;

  public void cancel() {
    canceled = true;
  }
}
