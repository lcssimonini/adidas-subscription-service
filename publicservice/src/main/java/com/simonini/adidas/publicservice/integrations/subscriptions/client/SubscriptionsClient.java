package com.simonini.adidas.publicservice.integrations.subscriptions.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionRequest;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "${integrations.subscriptions-api.url}/subscriptions", name = "subscription-api")
public interface SubscriptionsClient {

  @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  SubscriptionResponse create(@RequestBody SubscriptionRequest request);

  @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
  SubscriptionResponse findById(@PathVariable(value = "id") final String id);

  @DeleteMapping(path = "/{id}")
  void cancel(@PathVariable(value = "id") final String id);

  @GetMapping(consumes = APPLICATION_JSON_VALUE)
  Page<SubscriptionResponse> findAll(Pageable pageable);
}
