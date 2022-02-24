package com.simonini.adidas.subscriptionapi.service;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.error.exceptions.NotFoundException;
import com.simonini.adidas.subscriptionapi.integrations.email.SendEmailService;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.repository.SubscriptionRepository;
import com.simonini.adidas.subscriptionapi.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository repository;
    @Mock
    private SendEmailService sendEmailService;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;
    private final String emailTemplate = "Subscription for newsletter [%s] was created for email [%s]";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(subscriptionService, "emailTemplate", emailTemplate);
    }

    @Test
    void shouldSaveSubscriptionAndSendEmail() {
        Subscription subscription = Subscription.builder().id("id").build();
        when(repository.save(subscription)).thenReturn(subscription);
        subscriptionService.create(subscription);

        ArgumentCaptor<Subscription> subscriptionArgument =
                ArgumentCaptor.forClass(Subscription.class);
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

        ArgumentCaptor<Subscription> subscriptionArgument =
                ArgumentCaptor.forClass(Subscription.class);
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
}
