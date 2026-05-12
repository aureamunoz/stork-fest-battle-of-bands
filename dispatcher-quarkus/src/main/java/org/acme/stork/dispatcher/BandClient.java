package org.acme.stork.dispatcher;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(baseUri = "stork://band-service")
public interface BandClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String play();
}
