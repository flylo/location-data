package com.current.location.resources;

import com.current.location.core.Visit;
import com.current.location.request.UserVisitRequest;
import com.current.location.response.UserVisitResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api("Users")
public class UserResource {

  public UserResource() {
  }

  @POST
  @Path("{userId}/visits")
  @ApiOperation(
      value = "Submit a user visit.",
      notes = "Adheres to the standard response codes",
      response = UserVisitResponse.class
  )
  public UserVisitResponse postUserVisit(UserVisitRequest userVisitRequest) {
    return null;
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
