package com.current.location;

import com.current.location.configuration.CloudFirestoreConfiguration;
import com.current.location.configuration.SearchConfiguration;
import io.dropwizard.Configuration;


public class LocationDataConfiguration extends Configuration {

  SearchConfiguration searchConfiguration;
  CloudFirestoreConfiguration cloudFirestoreConfiguration;

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
}
