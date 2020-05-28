package com.current.location;

import com.current.location.configuration.CloudFirestoreConfiguration;
import com.current.location.configuration.SearchConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;


public class LocationDataConfiguration extends Configuration {

  SearchConfiguration searchConfiguration;
  CloudFirestoreConfiguration cloudFirestoreConfiguration;
  @JsonProperty("swagger")
  SwaggerBundleConfiguration swaggerBundleConfiguration;

  public CloudFirestoreConfiguration getCloudFirestoreConfiguration() {
    return cloudFirestoreConfiguration;
  }

  public void setCloudFirestoreConfiguration(
      CloudFirestoreConfiguration cloudFirestoreConfiguration) {
    this.cloudFirestoreConfiguration = cloudFirestoreConfiguration;
  }

  public SearchConfiguration getSearchConfiguration() {
    return searchConfiguration;
  }

  public void setSearchConfiguration(SearchConfiguration searchConfiguration) {
    this.searchConfiguration = searchConfiguration;
  }

  public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
    return swaggerBundleConfiguration;
  }

  public void setSwaggerBundleConfiguration(
      SwaggerBundleConfiguration swaggerBundleConfiguration) {
    this.swaggerBundleConfiguration = swaggerBundleConfiguration;
  }
}
