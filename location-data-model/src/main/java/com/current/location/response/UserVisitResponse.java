package com.current.location.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUserVisitResponse.class)
@JsonDeserialize(as = ImmutableUserVisitResponse.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface UserVisitResponse {

  UUID visitId();

  Long timestampMillis();
}
