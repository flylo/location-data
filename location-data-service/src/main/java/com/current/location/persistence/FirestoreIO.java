package com.current.location.persistence;

import com.codahale.metrics.health.HealthCheck;
import com.current.location.configuration.CloudFirestoreConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.WriteResult;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.WebApplicationException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirestoreIO {
  private static final Logger LOGGER = LoggerFactory.getLogger(FirestoreIO.class);

  private final Firestore firestore;
  private final CloudFirestoreConfiguration firestoreConfiguration;
  private final ExecutorService ioExecutor;
  private final ObjectMapper objectMapper;

  public FirestoreIO(CloudFirestoreConfiguration firestoreConfiguration,
                     ObjectMapper objectMapper) {
    this.firestore = CloudFirestoreFactory.createCloudFirestore(firestoreConfiguration);
    this.firestoreConfiguration = firestoreConfiguration;
    this.ioExecutor = Executors.newFixedThreadPool(firestoreConfiguration.getReadWriteThreadpoolSize());
    this.objectMapper = objectMapper;
  }

  public <T> WriteResult write(T obj) {
    Map<String, Object> fields = objectMapper.convertValue(obj, new TypeReference<>() {
    });
    ApiFuture<WriteResult> apiFuture = firestore.collection(firestoreConfiguration.getCollectionName())
        .document().create(fields);
    final CompletableFuture<WriteResult> completableFuture = new CompletableFuture<>();
    ApiFutures.addCallback(
        apiFuture,
        new ApiFutureCallback<>() {
          @Override
          public void onSuccess(@Nullable WriteResult result) {
            completableFuture.complete(result);
          }

          @Override
          public void onFailure(Throwable t) {
            completableFuture.completeExceptionally(new WebApplicationException("Failed to write to Firestore", t));
          }
        },
        ioExecutor
    );
    try {
      return completableFuture.get(firestoreConfiguration.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS);
    } catch (TimeoutException te) {
      throw new WebApplicationException("Timed out waiting for Firestore write to complete", te);
    } catch (InterruptedException | ExecutionException e) {
      throw new WebApplicationException("Unexpected exception waiting for Firestore write to complete", e);
    }
  }

  public HealthCheck.Result healthCheck() {
    try {
      firestore.listCollections();
      return HealthCheck.Result.healthy();
    } catch (FirestoreException fse) {
      String msg = "Unable to connect to Firestore instance";
      LOGGER.error(msg, fse);
      return HealthCheck.Result.unhealthy(msg);
    } catch (Exception e) {
      String msg = "Unexpected exception while attempting to connect to Firestore instance";
      LOGGER.error(msg, e);
      return HealthCheck.Result.unhealthy(msg);
    }
  }
}
