package com.current.location.resources;

import com.current.location.core.Visit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Api("Visits")
public class VisitResource {

  @GET
  @Path("{visitId}")
  @ApiOperation(
      value = "Retrieve a single visit by visit ID.",
      notes = "Adheres to the standard response codes",
      response = Visit.class
  )
  public Visit getVisit() {
    return null;
  }
}
