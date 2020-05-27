package com.current.location;

import io.dropwizard.Configuration;


public class LocationDataConfiguration extends Configuration {

  private String bigtableHost;

  public String getBigtableHost() {
    return bigtableHost;
  }

  public void setBigtableHost(String bigtableHost) {
    this.bigtableHost = bigtableHost;
  }
}
