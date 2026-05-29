package org.acme.stork.lb;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.LoadBalancerType;
import io.smallrye.stork.spi.LoadBalancerProvider;
import jakarta.enterprise.context.ApplicationScoped;

@LoadBalancerType("custom-load-balancer")
@ApplicationScoped
public class CustomLoadBalancerProvider implements LoadBalancerProvider<CustomLoadBalancerConfiguration> {

    @Override
    public LoadBalancer createLoadBalancer(CustomLoadBalancerConfiguration config, ServiceDiscovery serviceDiscovery) {
        return new CustomLoadBalancer(config);
    }
}
