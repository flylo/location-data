package com.current.location.response;

import com.current.location.core.ImmutableVisit;
import com.current.location.core.Merchant;
import com.current.location.core.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableVisitResponse.class)
@JsonDeserialize(as = ImmutableVisitResponse.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface VisitResponse {
  UUID visitId();

  Long timestampMillis();

  Merchant merchant();

  User user();
}
