package com.simonini.adidas.subscriptionapi.service;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {

  Subscription create(Subscription subscription);

  Subscription save(Subscription subscription);

  void cancel(String id);

  Subscription findById(String id);

  Page<Subscription> findAll(Pageable pageable);
}
