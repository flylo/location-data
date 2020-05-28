package com.current.location;

import com.current.ApiClient;
import com.current.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Make sure that our generated clients can actually hit a real endpoint
@Testcontainers
public class PingApiTest {

  private static final String GOOGLE_CREDENTIALS_ENV = "GOOGLE_APPLICATION_CREDENTIALS";
  private static final String SERVICE_EMULATOR_CONTAINER_NAME = "current/location:latest";
  private static final int SERVICE_EMULATOR_PORT = 8080;

  @Container
  private static GenericContainer SERVICE = new GenericContainer<>(SERVICE_EMULATOR_CONTAINER_NAME)
      .withEnv(GOOGLE_CREDENTIALS_ENV, System.getenv(GOOGLE_CREDENTIALS_ENV))
      .withFileSystemBind(System.getenv(GOOGLE_CREDENTIALS_ENV), System.getenv(GOOGLE_CREDENTIALS_ENV))
      .withExposedPorts(SERVICE_EMULATOR_PORT);


  @Test
  void testPingApi() throws Exception {
    ApiClient client = Configuration.getDefaultApiClient()
        .setBasePath(String.format("http://localhost:%s", SERVICE.getMappedPort(SERVICE_EMULATOR_PORT)));
    PingApi pingApi = new PingApi(client);
    Assertions.assertEquals("pong", pingApi.pingResourcePingGet());
  }

}
