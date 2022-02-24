package com.simonini.adidas.subscriptionapi.converter;

import static com.simonini.adidas.subscriptionapi.domain.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionRequest;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionResponse;
import org.junit.jupiter.api.Test;

class SubscriptionConverterTest {

  private final SubscriptionRequest subscriptionRequest =
      SubscriptionRequest.builder()
          .firstName("Lucas")
          .gender(MALE)
          .email("lucas@simo.com")
          .dateOfBirth("07/01/1988")
          .consentSubscribe(true)
          .newsletterId("newsletterId")
          .build();

  private final Subscription subscription =
      Subscription.builder()
          .id("id")
          .firstName("Lucas")
          .gender("MALE")
          .email("lucas@simo.com")
          .dateOfBirth("07/01/1988")
          .consentSubscribe(true)
          .newsletterId("newsletterId")
          .build();

  @Test
  void shouldBuildSubscriptionDomainObject() {
    Subscription subscription = SubscriptionConverter.fromRequest(subscriptionRequest);
    assertEquals(subscriptionRequest.getFirstName(), subscription.getFirstName());
    assertEquals(subscriptionRequest.getGender().name(), subscription.getGender());
    assertEquals(subscriptionRequest.getEmail(), subscription.getEmail());
    assertEquals(subscriptionRequest.getDateOfBirth(), subscription.getDateOfBirth());
    assertEquals(subscriptionRequest.isConsentSubscribe(), subscription.isConsentSubscribe());
    assertEquals(subscriptionRequest.getNewsletterId(), subscription.getNewsletterId());
  }

  @Test
  void shouldBuildSubscriptionResponse() {
    SubscriptionResponse subscriptionResponse = SubscriptionConverter.fromDomain(subscription);
    assertEquals(subscription.getId(), subscriptionResponse.getId());
    assertEquals(subscription.getFirstName(), subscriptionResponse.getFirstName());
    assertEquals(subscription.getGender(), subscriptionResponse.getGender());
    assertEquals(subscription.getEmail(), subscriptionResponse.getEmail());
    assertEquals(subscription.getDateOfBirth(), subscriptionResponse.getDateOfBirth());
    assertEquals(subscription.isConsentSubscribe(), subscriptionResponse.isConsentSubscribe());
    assertEquals(subscription.isEmailSent(), subscriptionResponse.isEmailSent());
    assertEquals(subscription.getNewsletterId(), subscriptionResponse.getNewsletterId());
  }
}
