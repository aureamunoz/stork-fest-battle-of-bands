package org.acme.stork.dispatcher;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.concurrent.TimeUnit;

@Path("/dispatch")
public class DispatcherResource {

    @RestClient
    BandClient bandClient;

    @Inject
    MeterRegistry registry;

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

    @GET
    @Path("/stork-metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public String storkMetrics() {
        String svc = "band-service";
        Timer selDur = registry.find("stork.service-selection.duration").tag("service-name", svc).timer();
        long selCount  = selDur != null ? selDur.count() : 0;
        double selAvg  = selDur != null && selCount > 0 ? selDur.mean(TimeUnit.MILLISECONDS) : 0.0;

        Timer discDur = registry.find("stork.service-discovery.duration").tag("service-name", svc).timer();
        long discCount = discDur != null ? discDur.count() : 0;
        double discAvg = discDur != null && discCount > 0 ? discDur.mean(TimeUnit.MILLISECONDS) : 0.0;

        Counter selFail   = registry.find("stork.service-selection.failures").tag("service-name", svc).counter();
        Counter discFail  = registry.find("stork.service-discovery.failures").tag("service-name", svc).counter();
        Counter instances = registry.find("stork.service-discovery.instances.count").tag("service-name", svc).counter();

        return String.format(java.util.Locale.US,
                "{\"selectionCount\":%d,\"selectionAvgMs\":%.4f," +
                "\"discoveryCount\":%d,\"discoveryAvgMs\":%.4f," +
                "\"selectionFailures\":%d,\"discoveryFailures\":%d,\"instancesDiscovered\":%d}",
                selCount, selAvg, discCount, discAvg,
                selFail   != null ? (long) selFail.count()   : 0,
                discFail  != null ? (long) discFail.count()  : 0,
                instances != null ? (long) instances.count() : 0);
    }
}
