package com.simonini.adidas.subscriptionapi.repository;

import com.simonini.adidas.subscriptionapi.domain.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {}
