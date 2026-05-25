package org.acme.stork.dispatcher;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/dispatch")
public class DispatcherResource {

    @RestClient
    BandClient bandClient;

    @ConfigProperty(name = "quarkus.stork.band-service.service-discovery.type", defaultValue = "consul")
    String sdType;

    @ConfigProperty(name = "quarkus.stork.band-service.load-balancer.type", defaultValue = "round-robin")
    String lbType;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dispatch() {
        return bandClient.play();
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    public String config() {
        return "{\"sd\":\"" + sdType + "\",\"lb\":\"" + lbType + "\"}";
    }
}
