package com.current.location.persistence;

import com.current.location.configuration.CloudFirestoreConfiguration;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.grpc.Grpc;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;

// The ugliness in this class is mostly due to the poor emulator support
public class CloudFirestoreFactory {

  public static Firestore createCloudFirestore(CloudFirestoreConfiguration configuration) {
    try {
      if (configuration.getCloudFirestoreHost() == null) {
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(CredentialsModule.getDefaultCredentials())
            .setProjectId(configuration.getGoogleCloudProjectId())
            .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore(app);
      } else {
        FirestoreOptions.Builder builder = FirestoreOptions.getDefaultInstance().toBuilder();
        if (configuration.isFixedCredentials()) {
          builder.setCredentialsProvider(FixedCredentialsProvider.create(CredentialsModule.getFixedCredentials()));
        }
        // NOTE: Due to open issue in firestore emulator, we need to the environment variable rather than
        //       set the host directly in the configuration options:
        //       https://github.com/googleapis/java-firestore/issues/190#issuecomment-622004443
        return builder
            .setProjectId(configuration.getGoogleCloudProjectId())
            // .setHost(configuration.getCloudFirestoreHost())
            .setHeaderProvider(FixedHeaderProvider.create(
                "Authorization", "Bearer owner"
            ))
            .build()
            .getService();
      }
    } catch (IOException ioe) {
      throw new RuntimeException("Failed to initialize Firestore client from GCP credentials", ioe);
    }
  }

}
