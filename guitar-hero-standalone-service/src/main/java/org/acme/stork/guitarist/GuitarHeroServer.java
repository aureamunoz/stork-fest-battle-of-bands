package org.acme.stork.guitarist;

import io.smallrye.stork.Stork;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.List;
import java.util.Random;

public class GuitarHeroServer {

    public static void main(String[] args) {
        var config = ConfigProvider.getConfig();

        String name = config.getOptionalValue("guitar-hero.name", String.class).orElse("Slash");
        int port = config.getOptionalValue("guitar-hero.port", Integer.class).orElse(9000);
        int delayMs = config.getOptionalValue("guitar-hero.delay-ms", Integer.class).orElse(300);
        int failureRatio = config.getOptionalValue("guitar-hero.failure-ratio", Integer.class).orElse(0);
        Random random = new Random();

        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer()
                .requestHandler(req -> {
                    if ("/health".equals(req.path())) {
                        req.response().endAndForget("OK");
                        return;
                    }
                    if (failureRatio > 0 && random.nextInt(100) < failureRatio) {
                        req.response().setStatusCode(500).endAndForget("FAIL");
                    } else if (delayMs > 0) {
                        vertx.setTimer(delayMs, id -> req.response().endAndForget(name));
                    } else {
                        req.response().endAndForget(name);
                    }
                })
                .listenAndAwait(port);

        System.out.println(name + " listening on port " + port);

        Stork.initialize();
        Stork.getInstance().getService("band-service")
                .registerInstance(name, List.of("guns-n-roses", "hard-rock"), "localhost", port)
                .await().indefinitely();

        System.out.println(name + " registered in Consul");
    }
}
