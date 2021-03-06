package com.simonini.adidas.subscriptionapi.service.impl;

import static com.simonini.adidas.subscriptionapi.util.LogUtil.asJson;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.error.exceptions.NotFoundException;
import com.simonini.adidas.subscriptionapi.integrations.email.SendEmailService;
import com.simonini.adidas.subscriptionapi.integrations.email.dto.SendEmailRequest;
import com.simonini.adidas.subscriptionapi.repository.SubscriptionRepository;
import com.simonini.adidas.subscriptionapi.service.SubscriptionService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository repository;
  private final SendEmailService sendEmailService;

  @Value("${subscription.email.template}")
  private String emailTemplate;

  @Override
  public Subscription create(Subscription subscription) {
    log.info("Subscription to be created: {}", asJson(subscription));
    subscription.setCreatedAt(LocalDateTime.now());
    Subscription savedSubscription = repository.save(subscription);
    sendEmailService.sendEmail(getEmailRequest(subscription));
    return savedSubscription;
  }

  @Override
  public Subscription save(Subscription subscription) {
    log.info("Subscription to save: {}", asJson(subscription));
    return repository.save(subscription);
  }

  @Override
  public void cancel(String id) {
    log.info("Attempt to cancel subscription with id: {}", id);
    Subscription subscription = findById(id);
    subscription.cancel();
    save(subscription);
  }

  @Override
  public Subscription findById(String id) {
    log.info("Attempt to find subscription with id: {}", id);
    return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
  }

  @Override
  public Page<Subscription> findAll(Pageable pageable) {
    log.info("Attempt to find all subscriptions from page: {}", asJson(pageable));
    return repository.findAll(pageable);
  }

  private SendEmailRequest getEmailRequest(Subscription subscription) {
    return SendEmailRequest.builder()
        .entityId(subscription.getId())
        .email(subscription.getEmail())
        .message(getEmailMessage(subscription))
        .build();
  }

  private String getEmailMessage(Subscription subscription) {
    return String.format(emailTemplate, subscription.getNewsletterId(), subscription.getEmail());
  }
}
