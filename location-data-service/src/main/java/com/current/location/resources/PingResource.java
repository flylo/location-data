package com.current.location.resources;

import com.codahale.metrics.annotation.Metered;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("ping")
@Api("Ping")
public class PingResource {

  @GET
  @Metered
  @ApiOperation(
      value = "Ping the service.",
      notes = "Get a quick verification that the service is running. "
          + "Adheres to the standard response codes",
      response = String.class
  )
  public String ping() {
    return "pong";
  }

}
