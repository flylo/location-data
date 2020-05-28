package com.current.location.testing;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FirestoreTestUtilsTest extends FirestoreIntegrationTest {

  @Test
  void testFirestoreEmulation() throws Exception {
    Firestore firestore = FirestoreOptions.getDefaultInstance()
        .toBuilder()
        .setHeaderProvider(FixedHeaderProvider.create(
            "Authorization", "Bearer owner"
        ))
        .build()
        .getService();
    firestore.collection("lol").document().create(Map.of("key", "value")).get(10, TimeUnit.SECONDS);
    int numResults = firestore.collection("lol").whereEqualTo("key", "value")
        .get().get().getDocuments().size();
    Assertions.assertEquals(1, numResults);
  }

}
