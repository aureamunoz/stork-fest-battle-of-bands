package org.acme.stork.guitarist;

import io.smallrye.common.annotation.Blocking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Random;

@Path("/")
public class GuitarHeroResource {

    @ConfigProperty(name = "guitar-hero.name", defaultValue = "Slash")
    String name;

    @ConfigProperty(name = "guitar-hero.delay-ms", defaultValue = "300")
    int delayMs;

    @ConfigProperty(name = "guitar-hero.failure-ratio", defaultValue = "0")
    int failureRatio;

    private final Random random = new Random();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    public Response play() {
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (failureRatio > 0 && random.nextInt(100) < failureRatio) {
            return Response.serverError().entity("FAIL").build();
        }

        return Response.ok(name).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "OK";
    }
}
