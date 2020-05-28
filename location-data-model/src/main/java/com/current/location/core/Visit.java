package com.current.location.core;

import com.current.location.response.ImmutableVisitResponse;
import com.current.location.response.VisitResponse;
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

  UUID userId();

  Long timestampMillis();

  Merchant merchant();

  default VisitResponse toResponse() {
    return ImmutableVisitResponse.builder()
        .visitId(visitId())
        .timestampMillis(timestampMillis())
        .user(ImmutableUser.builder().userId(userId()).build())
        .merchant(merchant())
        .build();
  }
}
