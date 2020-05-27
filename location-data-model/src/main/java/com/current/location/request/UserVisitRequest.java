package com.current.location.request;

import com.current.location.core.Merchant;
import com.current.location.core.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUserVisitRequest.class)
@JsonDeserialize(as = ImmutableUserVisitRequest.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface UserVisitRequest {
  Merchant merchant();

  User user();

}
