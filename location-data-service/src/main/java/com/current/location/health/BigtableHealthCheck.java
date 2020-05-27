package com.current.location.health;

import com.codahale.metrics.health.HealthCheck;

public class BigtableHealthCheck extends HealthCheck {

  public BigtableHealthCheck() {
  }

  @Override
  protected Result check() throws Exception {
    // TODO: test bigtable connectivity
    return Result.healthy();
  }
}
