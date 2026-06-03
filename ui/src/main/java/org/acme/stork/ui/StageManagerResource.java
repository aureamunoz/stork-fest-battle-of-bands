package org.acme.stork.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


@Path("/")
public class StageManagerResource {

    @Inject
    Template index;

    @RestClient
    QuarkusDispatcherClient quarkusDispatcher;

    @RestClient
    SpringDispatcherClient springDispatcher;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return index.instance();
    }

    @GET
    @Path("/config/quarkus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response configQuarkus() {
        try {
            return Response.ok(quarkusDispatcher.config()).build();
        } catch (Exception e) {
            return Response.ok("{\"sd\":\"?\",\"lb\":\"?\"}").build();
        }
    }

    @GET
    @Path("/call/quarkus")
    @Produces(MediaType.TEXT_PLAIN)
    public Response callQuarkus() {
        try {
            return Response.ok(quarkusDispatcher.dispatch()).build();
        } catch (WebApplicationException e) {
            return Response.ok("FAIL").build();
        } catch (Exception e) {
            return Response.status(503).entity("DISPATCHER_DOWN").build();
        }
    }

    @GET
    @Path("/call/spring")
    @Produces(MediaType.TEXT_PLAIN)
    public Response callSpring() {
        try {
            return Response.ok(springDispatcher.dispatch()).build();
        } catch (WebApplicationException e) {
            return Response.ok("FAIL").build();
        } catch (Exception e) {
            return Response.status(503).entity("DISPATCHER_DOWN").build();
        }
    }

    @GET
    @Path("/metrics/stork")
    @Produces(MediaType.APPLICATION_JSON)
    public Response storkMetrics() {
        try {
            return Response.ok(quarkusDispatcher.storkMetrics()).build();
        } catch (Exception e) {
            return Response.status(503).build();
        }
    }
}
