package com.current.location.configuration;

import javax.annotation.Nullable;

public class CloudFirestoreConfiguration {
  private String googleCloudProjectId;
  private String collectionName;
  private String cloudFirestoreHost;
  private int readTimeoutMillis;
  private int writeTimeoutMillis;
  private int readWriteThreadpoolSize;

  public String getGoogleCloudProjectId() {
    return googleCloudProjectId;
  }

  public void setGoogleCloudProjectId(String googleCloudProjectId) {
    this.googleCloudProjectId = googleCloudProjectId;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  @Nullable
  public String getCloudFirestoreHost() {
    return cloudFirestoreHost;
  }

  public void setCloudFirestoreHost(String cloudFirestoreHost) {
    this.cloudFirestoreHost = cloudFirestoreHost;
  }

  public int getReadTimeoutMillis() {
    return readTimeoutMillis;
  }

  public void setReadTimeoutMillis(int readTimeoutMillis) {
    this.readTimeoutMillis = readTimeoutMillis;
  }

  public int getWriteTimeoutMillis() {
    return writeTimeoutMillis;
  }

  public void setWriteTimeoutMillis(int writeTimeoutMillis) {
    this.writeTimeoutMillis = writeTimeoutMillis;
  }

  public int getReadWriteThreadpoolSize() {
    return readWriteThreadpoolSize;
  }

  public void setReadWriteThreadpoolSize(int readWriteThreadpoolSize) {
    this.readWriteThreadpoolSize = readWriteThreadpoolSize;
  }
}
