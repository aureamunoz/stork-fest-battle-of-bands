package org.acme.stork.dispatcher;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/dispatch")
public class DispatcherResource {

    @RestClient
    BandClient bandClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dispatch() {
        return bandClient.play();
    }
}
