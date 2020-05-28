package com.current.location.resources;

import com.current.location.core.Visit;
import com.current.location.persistence.FirestoreIO;
import com.current.location.response.VisitResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Api("Visits")
public class VisitResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(VisitResource.class);
  private static final String VISIT_ID_FIELD = "visitId";

  private FirestoreIO firestore;

  public VisitResource(FirestoreIO firestore) {
    this.firestore = firestore;
  }

  @GET
  @Path("{visitId}")
  @ApiOperation(
      value = "Retrieve a single visit by visit ID.",
      notes = "Adheres to the standard response codes",
      response = Visit.class
  )
  public VisitResponse getVisit(@PathParam("visitId") UUID visitId) {
    List<Visit> visits = firestore.whereQuery(VISIT_ID_FIELD, visitId.toString(), Visit.class)
        .collect(Collectors.toList());
    if (visits.size() < 1) {
      throw new WebApplicationException(String.format("Unable to find visit with ID: %s", visitId.toString()),
          Response.Status.NOT_FOUND);
    } else if (visits.size() == 1) {
      return visits.get(0).toResponse();
    } else {
      LOGGER.warn(String.format("Found multiple visit entries for ID: %s. Returning only the first",
          visitId.toString()));
      return visits.get(0).toResponse();
    }
  }
}
