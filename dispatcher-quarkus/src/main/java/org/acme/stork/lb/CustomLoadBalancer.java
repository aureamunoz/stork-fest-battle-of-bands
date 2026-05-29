package org.acme.stork.lb;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.NoServiceInstanceFoundException;
import io.smallrye.stork.api.ServiceInstance;

import java.util.Collection;

public class CustomLoadBalancer implements LoadBalancer {

    public CustomLoadBalancer(CustomLoadBalancerConfiguration config) {
    }

    @Override
    public ServiceInstance selectServiceInstance(Collection<ServiceInstance> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            throw new NoServiceInstanceFoundException("No services found.");
        }
        for (ServiceInstance serviceInstance : serviceInstances) {
            if (serviceInstance.getLabels().containsKey("guns-n-roses")) {
                return serviceInstance;
            }
        }
        throw new NoServiceInstanceFoundException("No instance with 'guns-n-roses' label found.");
    }
}
