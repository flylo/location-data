package com.current.location.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableMerchant.class)
@JsonDeserialize(as = ImmutableMerchant.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Merchant {
  String merchantId();

  String merchantName();
}
