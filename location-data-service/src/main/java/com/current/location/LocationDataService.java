package com.current.location;

import com.current.location.health.BigtableHealthCheck;
import com.current.location.resources.PingResource;
import com.current.location.resources.UserResource;
import com.current.location.resources.VisitResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationDataService extends Application<LocationDataConfiguration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocationDataService.class);

  public static void main(String[] args) throws Exception {
    try {
      new LocationDataService().run(args);
    } catch (Throwable t) {
      LOGGER.error("Location Data Service aborted unexpectedly.", t);
      throw t;
    }
  }

  @Override
  public void initialize(Bootstrap<LocationDataConfiguration> bootstrap) {
    // TODO: do
  }

  @Override
  public void run(LocationDataConfiguration configuration,
                  Environment environment) {
    final PingResource pingResource = new PingResource();
    final UserResource userResource = new UserResource();
    final VisitResource visitResource = new VisitResource();
    final BigtableHealthCheck bigtableHealthCheck = new BigtableHealthCheck();
    environment.healthChecks().register("Bigtable Health", bigtableHealthCheck);
    environment.jersey().register(pingResource);
    environment.jersey().register(userResource);
    environment.jersey().register(visitResource);
  }

}
