package com.simonini.adidas.subscriptionapi.resource;

import com.simonini.adidas.subscriptionapi.converter.SubscriptionConverter;
import com.simonini.adidas.subscriptionapi.domain.Subscription;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionRequest;
import com.simonini.adidas.subscriptionapi.resource.dto.SubscriptionResponse;
import com.simonini.adidas.subscriptionapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.simonini.adidas.subscriptionapi.converter.SubscriptionConverter.fromDomain;
import static com.simonini.adidas.subscriptionapi.converter.SubscriptionConverter.fromRequest;
import static com.simonini.adidas.subscriptionapi.util.LogUtil.asJson;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionResource {

    private final SubscriptionService service;

    @ResponseStatus(CREATED)
    @PostMapping
    public SubscriptionResponse createNewSubscription(@RequestBody @Valid SubscriptionRequest request) {
        log.info("Attempt to create subscription: {}", asJson(request));
        return fromDomain(service.create(fromRequest(request)));
    }

    @GetMapping("/{subscriptionId}")
    public SubscriptionResponse getSubscriptionDetails(@PathVariable("subscriptionId") String subscriptionId) {
        log.info("Attempt to get subscription details, id: {}", subscriptionId);
        return fromDomain(service.findById(subscriptionId));
    }

    @GetMapping
    public Page<SubscriptionResponse> getAllSubscriptions(Pageable pageable) {
        log.info("Attempt to find page of subscriptions: {}", asJson(pageable));
        return getSubscriptionsPage(service.findAll(pageable), pageable);
    }

    @DeleteMapping("/{subscriptionId}")
    public void cancelSubscription(@PathVariable("subscriptionId") String subscriptionId) {
        log.info("Attempt to cancel subscription with id: {}", subscriptionId);
        service.cancel(subscriptionId);
    }

    private Page<SubscriptionResponse> getSubscriptionsPage(Page<Subscription> allSubscriptions, Pageable pageable) {
        return new PageImpl<>(getSubscriptionResponseList(allSubscriptions), pageable, allSubscriptions.getSize());
    }

    private List<SubscriptionResponse> getSubscriptionResponseList(Page<Subscription> allSubscriptions) {
        return allSubscriptions.getContent().stream()
                .map(SubscriptionConverter::fromDomain)
                .collect(Collectors.toList());
    }
}
