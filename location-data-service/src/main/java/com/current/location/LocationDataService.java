package com.current.location;

import com.current.location.health.FirestoreHealthCheck;
import com.current.location.persistence.FirestoreIO;
import com.current.location.resources.PingResource;
import com.current.location.resources.UserResource;
import com.current.location.resources.VisitResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationDataService extends Application<LocationDataConfiguration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LocationDataService.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
    bootstrap.addBundle(new SwaggerBundle<>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
          LocationDataConfiguration configuration) {
        return configuration.getSwaggerBundleConfiguration();
      }
    });
  }

  @Override
  public void run(LocationDataConfiguration configuration,
                  Environment environment) {
    final FirestoreIO firestore = new FirestoreIO(configuration.getCloudFirestoreConfiguration(), OBJECT_MAPPER);
    final PingResource pingResource = new PingResource();
    final UserResource userResource = new UserResource(firestore, configuration.getSearchConfiguration());
    final VisitResource visitResource = new VisitResource(firestore);
    final FirestoreHealthCheck firestoreHealthCheck = new FirestoreHealthCheck(firestore);
    environment.healthChecks().register("Firestore Health", firestoreHealthCheck);
    environment.jersey().register(pingResource);
    environment.jersey().register(userResource);
    environment.jersey().register(visitResource);
  }

}
