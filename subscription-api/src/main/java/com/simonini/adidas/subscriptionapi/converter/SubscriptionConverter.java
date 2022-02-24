package com.simonini.adidas.subscriptionapi.converter;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionRequest;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SubscriptionConverter {

  public static Subscription fromRequest(SubscriptionRequest request) {
    return Subscription.builder()
        .email(request.getEmail())
        .firstName(request.getFirstName())
        .consentSubscribe(request.isConsentSubscribe())
        .dateOfBirth(request.getDateOfBirth())
        .gender(request.getGender().name())
        .newsletterId(request.getNewsletterId())
        .build();
  }

  public static SubscriptionResponse fromDomain(Subscription subscription) {
    return SubscriptionResponse.builder()
        .id(subscription.getId())
        .gender(subscription.getGender())
        .dateOfBirth(subscription.getDateOfBirth())
        .firstName(subscription.getFirstName())
        .email(subscription.getEmail())
        .emailSent(subscription.isEmailSent())
        .consentSubscribe(subscription.isConsentSubscribe())
        .newsletterId(subscription.getNewsletterId())
        .canceled(subscription.isCanceled())
        .build();
  }
}
