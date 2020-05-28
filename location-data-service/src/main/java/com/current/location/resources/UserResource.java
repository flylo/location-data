package com.current.location.resources;

import static com.current.location.validation.RequestValidators.validateAndConvertLookback;
import static com.current.location.validation.RequestValidators.validateUserVisitPostRequest;

import com.current.location.configuration.SearchConfiguration;
import com.current.location.core.ImmutableVisit;
import com.current.location.core.Visit;
import com.current.location.persistence.FirestoreIO;
import com.current.location.request.UserVisitRequest;
import com.current.location.response.ImmutableUserVisitResponse;
import com.current.location.response.UserVisitResponse;
import com.current.location.response.VisitResponse;
import com.current.location.search.FuzzyMatchingFilter;
import com.google.cloud.firestore.WriteResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api("Users")
public class UserResource {

  private static final String USER_ID_FIELD = "userId";
  private static final String TIMESTAMP_FIELD = "timestampMillis";

  private FirestoreIO firestore;
  private FuzzyMatchingFilter fuzzyMatchingFilter;

  public UserResource(FirestoreIO firestore, SearchConfiguration searchConfiguration) {
    this.firestore = firestore;
    this.fuzzyMatchingFilter = new FuzzyMatchingFilter(searchConfiguration.getMaxLevenshteinDistance());
  }

  @POST
  @Path("{userId}/visits")
  @ApiOperation(
      value = "Submit a user visit.",
      notes = "Adheres to the standard response codes",
      response = UserVisitResponse.class
  )
  public UserVisitResponse postUserVisit(UserVisitRequest userVisitRequest,
                                         @PathParam("userId") UUID userId) {
    validateUserVisitPostRequest(userId, userVisitRequest);
    UUID visitId = UUID.randomUUID();
    long now = DateTime.now().getMillis();
    Visit newVisit = ImmutableVisit.builder()
        .visitId(visitId)
        .userId(userId)
        .timestampMillis(now)
        .merchant(userVisitRequest.merchant())
        .build();
    // NOTE: firestore will automatically index all top-level fields
    WriteResult result = firestore.write(newVisit);
    return ImmutableUserVisitResponse.builder()
        .visitId(visitId)
        .timestampMillis(now)
        .build();
  }

  @GET
  @Path("{userId}/visits")
  @ApiOperation(
      value = "Retrieve a list of potential visits by userId and search string.",
      notes = "Adheres to the standard response codes",
      response = Visit.class,
      responseContainer = "List"
  )
  public List<VisitResponse> getUserVisits(@PathParam("userId") UUID userId,
                                           @QueryParam("searchString") @Nullable String searchString,
                                           @QueryParam("maxLookbackHrs") @Nullable Integer maxLookbackHrs) {
    // NOTE: we have this time filter here because Firestore doesn't have native fuzzy matching. As such,
    //       we need to pull in every transaction that a user has ever made. This could be huge, so in practice
    //       we'd probably want to only grab the last n days or hours of transactions
    long minTimestampMillis = validateAndConvertLookback(maxLookbackHrs);
    // First grab the user history
    Stream<Visit> visits = firestore.whereQueryWithTimeFilter(USER_ID_FIELD, userId.toString(),
        TIMESTAMP_FIELD, minTimestampMillis, Visit.class);
    // Filter by fuzzy-match if necessary
    if (searchString == null) {
      return visits.map(Visit::toResponse).collect(Collectors.toList());
    } else {
      return fuzzyMatchingFilter.apply(searchString, visits, visit -> visit.merchant().merchantName())
          .map(Visit::toResponse)
          .collect(Collectors.toList());
    }
  }
}
