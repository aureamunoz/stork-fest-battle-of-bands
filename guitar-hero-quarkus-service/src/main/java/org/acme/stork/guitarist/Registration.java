package org.acme.stork.guitarist;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.stork.Stork;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class Registration {

    /**
     * Register the service programmatically using custom registrar.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev) {
        System.out.println("Registration started");
        Stork.getInstance().getService("band-service").registerInstance("band-service","Slash","localhost",9000);
    }
}
