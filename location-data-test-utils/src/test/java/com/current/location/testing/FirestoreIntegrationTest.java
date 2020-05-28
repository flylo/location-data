package com.current.location.testing;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(FirestoreAfterEachExtension.class)
@ExtendWith(FirestoreEmulatorHostPatchExtension.class)
public abstract class FirestoreIntegrationTest {
  private static final String FIRESTORE_CONTAINER_NAME = "pathmotion/firestore-emulator-docker";
  private static final int FIRESTORE_PORT = 8088;
  private static final String FIRESTORE_PROJECT_ID = "test-project";
  @Container
  private static GenericContainer FIRESTORE = new GenericContainer<>(FIRESTORE_CONTAINER_NAME)
      .withEnv("FIRESTORE_PROJECT_ID", FIRESTORE_PROJECT_ID)
      .withExposedPorts(FIRESTORE_PORT);

  public static String getFirestoreHost() {
    return FIRESTORE.getHost() + ":" + FIRESTORE.getMappedPort(FIRESTORE_PORT);
  }

  public static String getFirestoreGcpProject() {
    return FIRESTORE_PROJECT_ID;
  }

}
