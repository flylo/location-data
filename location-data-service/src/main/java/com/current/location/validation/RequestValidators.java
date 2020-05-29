package com.current.location.validation;

import com.current.location.request.UserVisitRequest;
import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;

public class RequestValidators {

  public static void validateUserVisitPostRequest(String userId, UserVisitRequest userVisitRequest) {
    if (!userId.equals(userVisitRequest.user().userId())) {
      throw new WebApplicationException("User ID in request body and path don't match", Response.Status.BAD_REQUEST);
    }
  }

  // Validates parameter and converts it to minTimestampMillis
  public static long validateAndConvertLookback(@Nullable Integer maxLookbackHrs) {
    if (maxLookbackHrs == null) {
      return Long.MIN_VALUE;
    } else {
      if (maxLookbackHrs <= 0) {
        throw new WebApplicationException("maxLookbackHrs must be greater than 0", Response.Status.BAD_REQUEST);
      } else {
        return DateTime.now().minusHours(maxLookbackHrs).getMillis();
      }
    }
  }

}
