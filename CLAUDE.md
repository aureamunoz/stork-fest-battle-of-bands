# Stork Demo 2026 — Context for New Project

## Background

Aurea (auri) is preparing a presentation about SmallRye Stork (Service Discovery and Client-Side Load Balancer). She has done previous presentations using https://github.com/aureamunoz/stork-guitar-hero-demo and wants to update the demo for 2026.

## Existing Demo Summary

The current demo (`stork-guitar-hero-demo`) is a single Quarkus module with:

- **3 embedded HTTP servers** simulating guitarists:
  - **Slash** (port 9000): 300ms delay, 100% reliable
  - **Hendrix** (port 9001): 500ms delay, 100% reliable
  - **Eddie Van Halen** (port 9002): ~5ms delay, 20% failure rate
- **Dispatcher**: REST endpoint that uses Stork via `@RegisterRestClient(baseUri = "stork://guitar-hero-service")`
- **UI**: Qute/HTML template with buttons to call the service 10 times (sequential or concurrent), showing guitarist images per response
- **Service Discovery**: Static list (Consul available but manual setup via curl)
- **Load Balancing**: Multiple strategies available via config (round-robin, random, least-requests, power-of-two-choices, least-response-time, custom)
- **Custom LB example**: Always selects the service with "guns-n-roses" tag (Slash)
- Stack: Quarkus 3.34.3, RESTEasy Reactive, Vert.x, Qute Templates

## New Features to Showcase (2026)

### 1. Spring Boot Integration
- Module `spring-boot/` in SmallRye Stork provides `SpringBootConfigProvider`
- Reads `stork.*` properties from Spring Environment
- Same config pattern: `stork.<service-name>.service-discovery.<attribute>`
- Loaded via ServiceLoader mechanism, no code changes needed

### 2. Auto-Registration
- `service-registration/consul/` — `ConsulServiceRegistrar` auto-registers with Consul
- `service-registration/eureka/` — `EurekaServiceRegistrar` auto-registers with Eureka
- Services register themselves on startup when the right dependency is on the classpath
- Provider-based plugin system with SPI auto-discovery

### 3. All Load Balancer Strategies
| Strategy | Type ID | Algorithm |
|----------|---------|-----------|
| Round Robin | `round-robin` | Sequential (in core) |
| Random | `random` | Probabilistic |
| Least Requests | `least-requests` | Min in-flight count |
| Power of Two Choices | `power-of-two-choices` | Min of 2 random picks |
| Least Response Time | `least-response-time` | Time-weighted scoring with error penalty |
| Sticky | `sticky` | Session affinity with failure backoff |

### 4. Observability
- `quarkus.micrometer.binder.stork.enabled=true`
- Metrics: service discovery duration, selection duration, error rates
- Prometheus + Grafana compatible

### 5. Kubernetes Discovery
- `stork-service-discovery-kubernetes` — uses K8s API to get pods behind a service
- Enables client-side LB instead of K8s default round-robin proxy
- Config: `quarkus.stork.rest-service.service-discovery.type=kubernetes`

## Agreed Demo Architecture: "Battle of the Bands"

```
┌─────────────────────────────────────────────────────────┐
│                    UI (Qute/HTML)                        │
│  "Stage Manager" — controls each scenario               │
└────────────┬──────────────────────┬─────────────────────┘
             │                      │
    ┌────────▼────────┐   ┌────────▼────────┐
    │   Dispatcher    │   │   Dispatcher    │
    │   (Quarkus)     │   │  (Spring Boot)  │
    │  stork://band   │   │  stork://band   │
    └────────┬────────┘   └────────┬────────┘
             │                      │
             └──────────┬───────────┘
                        │
              ┌─────────▼──────────┐
              │    Consul / Auto   │
              │    Registration    │
              └─────────┬──────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
   ┌────▼────┐    ┌─────▼────┐    ┌────▼────┐
   │  Slash  │    │ Hendrix  │    │  Eddie  │
   │ 300ms   │    │  500ms   │    │ fast but│
   │ fiable  │    │  lento   │    │ 20% fail│
   └─────────┘    └──────────┘    └─────────┘
```

## 4 Demo Scenarios ("Songs")

### Song 1 — "Plug & Play" (Auto-registration)
- Guitarist services start and auto-register in Consul (no manual curl)
- Uses `stork-service-registration-consul` in each service
- Visual: guitarist icons appear in UI as services register
- Shows service discovery working dynamically

### Song 2 — "Battle of Strategies" (Live Load Balancing)
- UI buttons to switch between LB strategies in real-time
- Fire 20 calls and see visual distribution (which guitarist responds)
- round-robin → even distribution
- least-response-time → favors Eddie (fast) but penalizes his failures
- sticky → same guitarist until failure
- power-of-two-choices → smart balance

### Song 3 — "Two Stages, One Band" (Quarkus + Spring Boot)
- Two dispatchers: Quarkus and Spring Boot
- Same Stork config, same service, same guitarists
- UI shows two columns: Quarkus results vs Spring Boot results
- Proves Stork is framework-agnostic

### Song 4 — "The Encore" (Observability)
- Embedded metrics panel (or Grafana)
- Shows: discovery time, selection time, error rates
- Prometheus integration via Micrometer

## Proposed Module Structure

```
stork-battle-of-bands-demo/
├── guitar-hero-service/          # Generic parameterizable service (Quarkus, auto-registers in Consul)
├── dispatcher-quarkus/         # Quarkus dispatcher with Stork
├── dispatcher-spring/          # Spring Boot dispatcher with Stork
├── ui/                         # Frontend (can live in dispatcher-quarkus)
└── docker-compose.yml          # Consul + 3 guitarists + 2 dispatchers
```

## Key Stork Integration Points

### REST Client with Stork (Quarkus)
```java
@RegisterRestClient(baseUri = "stork://guitar-hero-service")
public interface Client {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String invoke();
}
```

### Stork Config (Quarkus)
```properties
quarkus.stork.guitar-hero-service.service-discovery.type=consul
quarkus.stork.guitar-hero-service.service-discovery.consul-host=localhost
quarkus.stork.guitar-hero-service.service-discovery.consul-port=8500
quarkus.stork.guitar-hero-service.load-balancer.type=round-robin
```

### Stork Config (Spring Boot)
```properties
stork.guitar-hero-service.service-discovery.type=consul
stork.guitar-hero-service.service-discovery.consul-host=localhost
stork.guitar-hero-service.service-discovery.consul-port=8500
stork.guitar-hero-service.load-balancer.type=round-robin
```

### Auto-Registration Config
```properties
quarkus.stork.guitar-hero-service.service-registrar.type=consul
quarkus.stork.guitar-hero-service.service-registrar.consul-host=localhost
quarkus.stork.guitar-hero-service.service-registrar.consul-port=8500
```

## SmallRye Stork Source Reference
The Stork source code is at: /Users/auri/Code/smallrye-load-balancer
Key modules for reference:
- `spring-boot/` — Spring Boot config provider
- `service-registration/consul/` — Consul auto-registration
- `load-balancer/` — All LB strategy implementations
- `service-discovery/consul/` — Consul service discovery
- `core/` — Stork core, CachingServiceDiscovery, RoundRobinLoadBalancer