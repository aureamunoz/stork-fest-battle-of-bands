# Guitarist Service (Standalone)

A standalone guitarist service that uses SmallRye Stork programmatic registration with Consul — no framework required (no Quarkus, no Spring Boot).

It uses `stork-microprofile-config` + `smallrye-config` to read `stork.*` properties from `META-INF/microprofile-config.properties`, and registers itself in Consul on startup via the Stork API.

## Prerequisites

- Consul running on port 8500:

```bash
docker run -d --name consul -p 8500:8500 hashicorp/consul
```

## Run

```bash
mvn exec:java -pl guitar-hero-standalone-service
```

You should see:

```
Slash listening on port 9000
Slash registered in Consul
```

## Verify

- Service responds: `curl localhost:9000` → `Slash`
- Health check: `curl localhost:9000/health` → `OK`
- Consul UI: http://localhost:8500/ui — `band-service` should appear registered

## Configuration

All properties are in `src/main/resources/META-INF/microprofile-config.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `guitar-hero.name` | `Slash` | Guitarist name returned by the service |
| `guitar-hero.port` | `9000` | HTTP server port |
| `guitar-hero.delay-ms` | `300` | Simulated response delay in milliseconds |
| `guitar-hero.failure-ratio` | `0` | Percentage of requests that return HTTP 500 |
| `stork.band-service.service-registrar.type` | `consul` | Stork registrar type |
| `stork.band-service.service-registrar.consul-host` | `localhost` | Consul host |
| `stork.band-service.service-registrar.consul-port` | `8500` | Consul port |
