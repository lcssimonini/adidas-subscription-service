package com.simonini.adidas.subscriptionapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.error.exceptions.NotFoundException;
import com.simonini.adidas.subscriptionapi.integrations.email.SendEmailService;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.repository.SubscriptionRepository;
import com.simonini.adidas.subscriptionapi.service.impl.SubscriptionServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private SubscriptionRepository repository;
  @Mock private SendEmailService sendEmailService;
  @InjectMocks private SubscriptionServiceImpl subscriptionService;
  private final String emailTemplate =
      "Subscription for newsletter [%s] was created for email [%s]";

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(subscriptionService, "emailTemplate", emailTemplate);
  }

  @Test
  void shouldSaveSubscriptionAndSendEmail() {
    Subscription subscription = Subscription.builder().id("id").build();
    when(repository.save(subscription)).thenReturn(subscription);
    subscriptionService.create(subscription);

    ArgumentCaptor<Subscription> subscriptionArgument = ArgumentCaptor.forClass(Subscription.class);
    verify(repository, times(1)).save(subscriptionArgument.capture());
    assertNotNull(subscriptionArgument.getValue().getCreatedAt());

    ArgumentCaptor<SendEmailRequest> sendEmailRequestArgument =
        ArgumentCaptor.forClass(SendEmailRequest.class);
    verify(sendEmailService, times(1)).sendEmail(sendEmailRequestArgument.capture());
    assertEquals(subscription.getId(), sendEmailRequestArgument.getValue().getEntityId());
  }

  @Test
  void shouldSaveSubscription() {
    Subscription subscription = Subscription.builder().id("id").build();
    when(repository.save(subscription)).thenReturn(subscription);
    subscriptionService.save(subscription);
    verify(repository, times(1)).save(subscription);
  }

  @Test
  void shouldCancelSubscription() {
    String subscriptionId = "id";
    Subscription subscription = Subscription.builder().id(subscriptionId).build();
    when(repository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
    when(repository.save(subscription)).thenReturn(subscription);
    subscriptionService.cancel(subscriptionId);

    ArgumentCaptor<Subscription> subscriptionArgument = ArgumentCaptor.forClass(Subscription.class);
    verify(repository, times(1)).save(subscriptionArgument.capture());
    assertTrue(subscription.isCanceled());
  }

  @Test
  void shouldNotCancelSubscriptionNotFound() {
    String subscriptionId = "id";
    when(repository.findById(subscriptionId)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> subscriptionService.cancel(subscriptionId));
    verify(repository, times(0)).save(any());
  }

  @Test
  void shouldFindSubscription() {
    String subscriptionId = "id";
    Subscription subscription = Subscription.builder().id(subscriptionId).build();
    when(repository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
    Subscription foundSubscription = subscriptionService.findById(subscriptionId);
    assertEquals(subscription, foundSubscription);
  }

  @Test
  void shouldThrowExceptionSubscriptionNotFound() {
    String subscriptionId = "id";
    when(repository.findById(subscriptionId)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> subscriptionService.findById(subscriptionId));
  }

  @Test
  void shouldFindAllSubscriptions() {
    Pageable pageable = Pageable.unpaged();
    Subscription subscription1 = Subscription.builder().id("subscriptionId1").build();
    Subscription subscription2 = Subscription.builder().id("subscriptionId2").build();
    when(repository.findAll(pageable))
        .thenReturn(new PageImpl<>(List.of(subscription1, subscription2)));
    Page<Subscription> pageFound = subscriptionService.findAll(pageable);
    verify(repository, times(1)).findAll(pageable);
    assertEquals(2, pageFound.getTotalElements());
  }

  @Test
  void shouldFindEmptySubscriptionsPage() {
    Pageable pageable = Pageable.unpaged();
    when(repository.findAll(pageable)).thenReturn(Page.empty());
    Page<Subscription> pageFound = subscriptionService.findAll(pageable);
    verify(repository, times(1)).findAll(pageable);
    assertEquals(0, pageFound.getTotalElements());
  }
}
