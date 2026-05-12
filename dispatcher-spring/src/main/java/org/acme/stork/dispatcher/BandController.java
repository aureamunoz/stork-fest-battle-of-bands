package org.acme.stork.dispatcher;

import io.smallrye.stork.Stork;
import io.smallrye.stork.api.Service;
import io.smallrye.stork.api.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RestController
public class BandController {

    private final Stork stork;
    private final RestTemplate restTemplate;

    public BandController(Stork stork) {
        this.stork = stork;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/dispatch")
    public String dispatch() {
        Service service = stork.getService("band-service");
        ServiceInstance instance = service.selectInstance()
                .await().atMost(Duration.ofSeconds(5));
        String url = String.format("http://%s:%d/", instance.getHost(), instance.getPort());
        return restTemplate.getForObject(url, String.class);
    }
}
