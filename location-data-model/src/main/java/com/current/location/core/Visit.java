package com.current.location.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableVisit.class)
@JsonDeserialize(as = ImmutableVisit.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Visit {
  UUID visitId();

  Long timestampMillis();

  Merchant merchant();

  User user();
}
