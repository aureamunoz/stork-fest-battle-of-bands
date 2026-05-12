package org.acme.stork.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    @Path("/call/quarkus")
    @Produces(MediaType.TEXT_PLAIN)
    public String callQuarkus() {
        return quarkusDispatcher.dispatch();
    }

    @GET
    @Path("/call/spring")
    @Produces(MediaType.TEXT_PLAIN)
    public String callSpring() {
        return springDispatcher.dispatch();
    }
}
