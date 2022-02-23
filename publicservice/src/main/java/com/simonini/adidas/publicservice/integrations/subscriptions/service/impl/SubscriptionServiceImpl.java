package com.simonini.adidas.publicservice.integrations.subscriptions.service.impl;

import com.simonini.adidas.publicservice.integrations.subscriptions.client.SubscriptionsClient;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionRequest;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionResponse;
import com.simonini.adidas.publicservice.integrations.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionsClient subscriptionsClient;

    @Override
    public SubscriptionResponse create(SubscriptionRequest request) {
        return subscriptionsClient.create(request);
    }

    @Override
    public void cancel(String id) {
        subscriptionsClient.cancel(id);
    }

    @Override
    public SubscriptionResponse findById(String id) {
        return subscriptionsClient.findById(id);
    }

    @Override
    public Page<SubscriptionResponse> findAll(Pageable pageable) {
        return subscriptionsClient.findAll(pageable);
    }
}
