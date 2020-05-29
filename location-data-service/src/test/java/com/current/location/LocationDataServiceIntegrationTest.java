package com.current.location;

import static com.current.location.test.utils.RequestUtils.userVisitRequest;
import static javax.ws.rs.client.Entity.json;

import com.current.location.extensions.JodaClockResetExtension;
import com.current.location.response.UserVisitResponse;
import com.current.location.response.VisitResponse;
import com.current.location.testing.FirestoreIntegrationTest;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({DropwizardExtensionsSupport.class, JodaClockResetExtension.class})
public class LocationDataServiceIntegrationTest extends FirestoreIntegrationTest {
  private static final int TIMEOUT_MILLIS = 10000_000;

  public static final DropwizardAppExtension<LocationDataConfiguration> DROPWIZARD =
      new DropwizardAppExtension<>(LocationDataService.class,
          "src/main/resources/locationdataservice.yaml",
          ConfigOverride.config("searchConfiguration.maxLevenshteinDistance", String.valueOf(3)),
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
    String userId = UUID.randomUUID().toString();
    UUID visitId = writeUserVisit(userId, "Lol");
    Assertions.assertNotNull(visitId);
  }

  @Test
  void testReadUserVisits() {
    String userId = UUID.randomUUID().toString();
    Set<UUID> expectedVisitIds = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      UUID visitId = writeUserVisit(userId, "Lol");
      expectedVisitIds.add(visitId);
    }
    List<VisitResponse> visits = webTarget("/users/" + userId + "/visits").request()
        .get()
        .readEntity(new GenericType<>() {
        });
    Set<UUID> actualVisitIds = visits.stream().map(VisitResponse::visitId).collect(Collectors.toSet());
    Assertions.assertEquals(expectedVisitIds, actualVisitIds);
  }

  @Test
  void testFuzzySearchUserVisits() {
    String searchTerm = "Starbucks";
    String userId = UUID.randomUUID().toString();
    Set<UUID> expectedVisitIds = new HashSet<>();
    // Write some matching visits for this user
    expectedVisitIds.add(writeUserVisit(userId, "Starbucks"));
    expectedVisitIds.add(writeUserVisit(userId, "Star Bucks"));
    expectedVisitIds.add(writeUserVisit(userId, "stArbuck$"));
    // And some non-matching visits for this user
    writeUserVisit(userId, "Wendy's");
    writeUserVisit(userId, "Wendys");
    // And some visits for some random other users
    writeUserVisit(UUID.randomUUID().toString(), "Starbucks");
    writeUserVisit(UUID.randomUUID().toString(), "Starbucks");

    List<VisitResponse> visits = webTarget("/users/" + userId.toString() + "/visits")
        .queryParam("searchString", searchTerm)
        .request()
        .get()
        .readEntity(new GenericType<>() {
        });
    Set<UUID> actualVisitIds = visits.stream().map(VisitResponse::visitId).collect(Collectors.toSet());
    Assertions.assertEquals(expectedVisitIds, actualVisitIds);
  }

  @Test
  void testUserVisitLookbackWindow() {
    String userId = UUID.randomUUID().toString();
    Set<UUID> expectedVisitIds = new HashSet<>();
    // Set clock to hour 1
    DateTimeUtils.setCurrentMillisFixed(1000 * 60 * 60);
    writeUserVisit(userId, "Starbucks");
    writeUserVisit(userId, "Starbucks");
    // Set clock to hour 2 + 1 millisecond
    DateTimeUtils.setCurrentMillisFixed(2 * 1000 * 60 * 60 + 1);
    expectedVisitIds.add(writeUserVisit(userId, "Starbucks"));
    expectedVisitIds.add(writeUserVisit(userId, "Starbucks"));
    // Fetch visits with 1 hour lookback cutoff
    List<VisitResponse> visits = webTarget("/users/" + userId.toString() + "/visits")
        .queryParam("maxLookbackHrs", 1)
        .queryParam("searchString", "Starbucks")
        .request()
        .get()
        .readEntity(new GenericType<>() {
        });
    Set<UUID> actualVisitIds = visits.stream().map(VisitResponse::visitId).collect(Collectors.toSet());
    Assertions.assertEquals(expectedVisitIds, actualVisitIds);
  }

  @Test
  void testGetVisits() {
    UUID visitId = writeUserVisit(UUID.randomUUID().toString(), "Starbucks");
    VisitResponse response = webTarget("/visits/" + visitId.toString()).request().get()
        .readEntity(VisitResponse.class);
    Assertions.assertEquals(visitId, response.visitId());
  }

  private UUID writeUserVisit(String userId, String merchantName) {
    Response response = webTarget("/users/" + userId.toString() + "/visits").request()
        .post(json(userVisitRequest(userId, merchantName)));
    Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    return response.readEntity(UserVisitResponse.class).visitId();
  }

  private WebTarget webTarget(String endpoint) {
    Client client = DROPWIZARD.client();
    int port = 8080;
    // Make these timeouts long so we don't throw exceptions while debugging in breakpoints
    client.property(ClientProperties.CONNECT_TIMEOUT, 5 * 60 * 1000);
    client.property(ClientProperties.READ_TIMEOUT, 5 * 60 * 1000);
    String urlPrefix = "http://localhost:" + port;
    return client.target(urlPrefix + endpoint);
  }

}
