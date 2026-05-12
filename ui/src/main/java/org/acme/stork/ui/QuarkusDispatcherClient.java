package org.acme.stork.ui;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/dispatch")
@RegisterRestClient(configKey = "dispatcher-quarkus")
public interface QuarkusDispatcherClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String dispatch();
}
