package com.current.location.resources;

import com.current.location.core.ImmutableVisit;
import com.current.location.core.Visit;
import com.current.location.persistence.FirestoreIO;
import com.current.location.request.UserVisitRequest;
import com.current.location.response.ImmutableUserVisitResponse;
import com.current.location.response.UserVisitResponse;
import com.google.cloud.firestore.WriteResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.joda.time.DateTime;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api("Users")
public class UserResource {

  private FirestoreIO firestore;

  public UserResource(FirestoreIO firestore) {
    this.firestore = firestore;
  }

  @POST
  @Path("{userId}/visits")
  @ApiOperation(
      value = "Submit a user visit.",
      notes = "Adheres to the standard response codes",
      response = UserVisitResponse.class
  )
  public UserVisitResponse postUserVisit(UserVisitRequest userVisitRequest) {
    // TODO: error handling and whatnot
    UUID visitId = UUID.randomUUID();
    long now = DateTime.now().getMillis();
    Visit newVisit = ImmutableVisit.builder()
        .visitId(visitId)
        .timestampMillis(now)
        .merchant(userVisitRequest.merchant())
        .user(userVisitRequest.user())
        .build();
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
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public List<Visit> getUserVisits(@QueryParam("searchString") Optional<String> searchString) {
    return null;
  }
}
