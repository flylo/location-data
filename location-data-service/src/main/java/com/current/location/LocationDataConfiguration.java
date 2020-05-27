package com.current.location;

import com.current.location.configuration.CloudFirestoreConfiguration;
import io.dropwizard.Configuration;


public class LocationDataConfiguration extends Configuration {

  CloudFirestoreConfiguration cloudFirestoreConfiguration;

  public CloudFirestoreConfiguration getCloudFirestoreConfiguration() {
    return cloudFirestoreConfiguration;
  }

  public void setCloudFirestoreConfiguration(
      CloudFirestoreConfiguration cloudFirestoreConfiguration) {
    this.cloudFirestoreConfiguration = cloudFirestoreConfiguration;
  }

}
