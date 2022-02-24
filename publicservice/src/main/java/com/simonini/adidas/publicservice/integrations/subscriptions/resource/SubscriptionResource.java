package com.simonini.adidas.publicservice.integrations.subscriptions.resource;

import static com.simonini.adidas.publicservice.util.LogUtil.asJson;
import static org.springframework.http.HttpStatus.CREATED;

import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionRequest;
import com.simonini.adidas.publicservice.integrations.subscriptions.resource.dto.SubscriptionResponse;
import com.simonini.adidas.publicservice.integrations.subscriptions.service.SubscriptionService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionResource {

  private final SubscriptionService service;

  @ResponseStatus(CREATED)
  @PostMapping
  public SubscriptionResponse createNewSubscription(
      @RequestBody @Valid SubscriptionRequest request) {
    log.info("Attempt to create subscription: {}", asJson(request));
    return service.create(request);
  }

  @GetMapping("/{subscriptionId}")
  public SubscriptionResponse getSubscriptionDetails(
      @PathVariable("subscriptionId") String subscriptionId) {
    log.info("Attempt to get subscription details, id: {}", subscriptionId);
    return service.findById(subscriptionId);
  }

  @GetMapping
  public Page<SubscriptionResponse> getAllSubscriptions(Pageable pageable) {
    log.info("Attempt to find page of subscriptions: {}", asJson(pageable));
    return service.findAll(pageable);
  }

  @DeleteMapping("/{subscriptionId}")
  public void cancelSubscription(@PathVariable("subscriptionId") String subscriptionId) {
    log.info("Attempt to cancel subscription with id: {}", subscriptionId);
    service.cancel(subscriptionId);
  }
}
