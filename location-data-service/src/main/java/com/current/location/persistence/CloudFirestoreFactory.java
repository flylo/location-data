package com.current.location.persistence;

import com.current.location.configuration.CloudFirestoreConfiguration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;

public class CloudFirestoreFactory {

  public static Firestore createCloudFirestore(CloudFirestoreConfiguration configuration) {
    try {
      GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
      FirebaseOptions options;
      if (configuration.getCloudFirestoreHost() == null) {
        options = new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .setProjectId(configuration.getGoogleCloudProjectId())
            .build();
      } else {
        options = new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .setProjectId(configuration.getGoogleCloudProjectId())
            .setFirestoreOptions(FirestoreOptions.newBuilder()
                .setHost(configuration.getCloudFirestoreHost())
                .build())
            .build();
      }
      FirebaseApp app = FirebaseApp.initializeApp(options);
      return FirestoreClient.getFirestore(app);
    } catch (IOException ioe) {
      throw new RuntimeException("Failed to initialize Firestore client from GCP credentials", ioe);
    }
  }

}
