package com.current.location.health;

import com.codahale.metrics.health.HealthCheck;
import com.current.location.persistence.FirestoreIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirestoreHealthCheck extends HealthCheck {

  private static final Logger LOGGER = LoggerFactory.getLogger(FirestoreHealthCheck.class);

  private FirestoreIO firestore;

  public FirestoreHealthCheck(FirestoreIO firestore) {
    this.firestore = firestore;
  }

  @Override
  protected Result check() throws Exception {
    return firestore.healthCheck();
  }
}
