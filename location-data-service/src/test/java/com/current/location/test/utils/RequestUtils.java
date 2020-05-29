package com.current.location.test.utils;

import com.current.location.core.ImmutableMerchant;
import com.current.location.core.ImmutableUser;
import com.current.location.request.ImmutableUserVisitRequest;
import com.current.location.request.UserVisitRequest;
import java.util.UUID;

public class RequestUtils {

  public static UserVisitRequest userVisitRequest(String userId) {
    return userVisitRequest(userId, "Troll Merchant");
  }

  public static UserVisitRequest userVisitRequest(String userId, String merchantName) {
    return ImmutableUserVisitRequest.builder()
        .user(ImmutableUser.builder()
            .userId(userId)
            .build())
        .merchant(ImmutableMerchant.builder()
            .merchantId(UUID.randomUUID().toString())
            .merchantName(merchantName)
            .build())
        .build();
  }
}
