package com.current.location.testing;

import static com.current.location.testing.FirestoreIntegrationTest.getFirestoreGcpProject;
import static com.current.location.testing.FirestoreIntegrationTest.getFirestoreHost;

import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class FirestoreAfterEachExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    smokeCollections();
  }

  private static void smokeCollections() {
    Firestore firestore = FirestoreOptions.getDefaultInstance()
        .toBuilder()
        .setHeaderProvider(FixedHeaderProvider.create(
            "Authorization", "Bearer owner"
        ))
        .build()
        .getService();
    try {
      firestore.listCollections().forEach(
          collectionReference -> collectionReference.listDocuments().forEach(documentReference -> {
            try {
              documentReference.delete().get();
            } catch (Exception e) {
              throw new RuntimeException("Failed to delete document", e);
            }
          })
      );
      firestore.close();
    } catch (Exception e) {
      throw new RuntimeException("Failed to clear out Firestore collections");
    } finally {
      try {
        firestore.close();
      } catch (Exception e) {
        throw new RuntimeException("failed to close firestore in cleanup", e);
      }
    }
  }
}
