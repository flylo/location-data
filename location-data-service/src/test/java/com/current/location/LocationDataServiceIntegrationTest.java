package com.current.location;

import static javax.ws.rs.client.Entity.json;

import com.current.location.core.ImmutableMerchant;
import com.current.location.core.ImmutableUser;
import com.current.location.request.ImmutableUserVisitRequest;
import com.current.location.request.UserVisitRequest;
import com.current.location.response.ImmutableUserVisitResponse;
import com.current.location.response.UserVisitResponse;
import com.current.location.testing.FirestoreIntegrationTest;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({DropwizardExtensionsSupport.class})
public class LocationDataServiceIntegrationTest extends FirestoreIntegrationTest {
  private static final int TIMEOUT_MILLIS = 10_000;

  public static final DropwizardAppExtension<LocationDataConfiguration> DROPWIZARD =
      new DropwizardAppExtension<>(LocationDataService.class,
          "src/main/resources/locationdataservice.yaml",
          ConfigOverride.config("cloudFirestoreConfiguration.googleCloudProjectId", getFirestoreGcpProject()),
          ConfigOverride.config("cloudFirestoreConfiguration.cloudFirestoreHost", getFirestoreHost()),
          ConfigOverride.config("cloudFirestoreConfiguration.readTimeoutMillis", String.valueOf(TIMEOUT_MILLIS)),
          ConfigOverride.config("cloudFirestoreConfiguration.writeTimeoutMillis", String.valueOf(TIMEOUT_MILLIS))
      );

  @Test
  void testPing() {
    Assertions.assertEquals("pong",
        webTarget("/ping").request().get().readEntity(String.class));
  }

  @Test
  void testWriteUserVisit() {
    UUID userId = UUID.randomUUID();
    UserVisitRequest userVisitRequest = ImmutableUserVisitRequest.builder()
        .user(ImmutableUser.builder()
            .userId(userId)
            .build())
        .merchant(ImmutableMerchant.builder()
            .merchantId(UUID.randomUUID())
            .merchantName("Troll Merchant")
            .build())
        .build();
    UserVisitResponse response = webTarget("/users/" + userId.toString() + "/visits").request()
        .post(json(userVisitRequest))
        .readEntity(ImmutableUserVisitResponse.class);
    Assertions.assertNotNull(response.visitId());
  }

  protected WebTarget webTarget(String endpoint) {
    Client client = DROPWIZARD.client();
    int port = 8080;
    // Make these timeouts long so we don't throw exceptions while debugging in breakpoints
    client.property(ClientProperties.CONNECT_TIMEOUT, 5 * 60 * 1000);
    client.property(ClientProperties.READ_TIMEOUT, 5 * 60 * 1000);
    String urlPrefix = "http://localhost:" + port;
    return client.target(urlPrefix + endpoint);
  }

}
