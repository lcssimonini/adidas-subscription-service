package com.simonini.adidas.publicservice.integrations.subscriptions.service;

import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionRequest;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {

  SubscriptionResponse create(SubscriptionRequest subscription);

  void cancel(String id);

  SubscriptionResponse findById(String id);

  Page<SubscriptionResponse> findAll(Pageable pageable);
}
