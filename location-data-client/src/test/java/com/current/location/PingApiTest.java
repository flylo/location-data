package com.current.location;

import com.current.ApiClient;
import com.current.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Make sure that our generated clients can actually hit a real endpoint
@Testcontainers
@Disabled("Disabling due to cloud-build issues that don't seem worth debugging at the moment")
public class PingApiTest {

  private static final String SERVICE_EMULATOR_CONTAINER_NAME = "current/location:latest";
  private static final int SERVICE_EMULATOR_PORT = 8080;

  @Container
  private static GenericContainer SERVICE = new GenericContainer<>(SERVICE_EMULATOR_CONTAINER_NAME)
      .withFileSystemBind(
          // Our testing config
          System.getProperty("user.dir") + "/src/test/resources",
          // Overwriting the actual config
          "/volumemount")
      .withExposedPorts(SERVICE_EMULATOR_PORT)
      .withCommand(
          "java", "-jar", "target/location-data-service-1.0-SNAPSHOT.jar", "server",
          "volumemount/trollingconfig.yaml"
      );


  @Test
  void testPingApi() throws Exception {
    ApiClient client = Configuration.getDefaultApiClient()
        .setBasePath(String.format("http://localhost:%s", SERVICE.getMappedPort(SERVICE_EMULATOR_PORT)));
    PingApi pingApi = new PingApi(client);
    Assertions.assertEquals("pong", pingApi.pingResourcePingGet());
  }

}
