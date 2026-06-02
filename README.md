# Storkfest Demo

### Song 1: Auto-registration + Service Discovery with Consul

1. Start Consul:
   ```
   podman run --rm -p 8500:8500 hashicorp/consul:1.20 agent -dev -client=0.0.0.0
   ```

5. Start the UI (new terminal):
   ```
   mvn quarkus:dev -pl ui 
   ```
   
4. Start the Quarkus dispatcher (new terminal):
   ```
   mvn quarkus:dev -pl dispatcher-quarkus 
   ```

   2. Start the first guitarist (Slash):
      ```
      mvn quarkus:dev -pl guitar-hero-service
      ```
      (Default: Slash, port 9000, 300ms delay)
   Slash lo tengo que registrar manualmente en Consul porque quiero necesito registrarlo con tags y además mostrar la diferencia entre auto-registro 
   y registro manual. Para eso, ejecuta este comando:
    ```
   curl -X PUT -d '{"ID": "Slash", "Name": "band-service", "Address": "localhost", "Port": 9000, "Tags": ["guns-n-roses","slash"]}' http://127.0.0.1:8500/v1/agent/service/register
   ```
   Para arrancarlo, deshabilitar el auto-registro en el application.properties de guitar-hero-service:

   ```
      mvn quarkus:dev -pl guitar-hero-service -Dquarkus.stork.band-service.service-registrar.enable=false
      ```

7. (Optional) Start more guitarists to see them appear in real time:
   ```
   QUARKUS_STORK_BAND_SERVICE_SERVICE_REGISTRAR_INSTANCE_NAME=Hendrix GUITAR_HERO_PORT=9001 GUITAR_HERO_DELAY_MS=500 mvn quarkus:dev -pl guitar-hero-service 
   QUARKUS_STORK_BAND_SERVICE_SERVICE_REGISTRAR_INSTANCE_NAME=Eddie GUITAR_HERO_PORT=9002 GUITAR_HERO_DELAY_MS=5 GUITAR_HERO_FAILURE_RATIO=20 mvn quarkus:dev -pl guitar-hero-service
   QUARKUS_STORK_BAND_SERVICE_SERVICE_REGISTRAR_INSTANCE_NAME=Flying GUITAR_HERO_PORT=9003 GUITAR_HERO_DELAY_MS=5 mvn quarkus:dev -pl guitar-hero-service
   ```


3. Verify registration in Consul:
   ```
   curl -s http://localhost:8500/v1/catalog/service/band-service | jq
   ```
   or open the browser: http://localhost:8500/ui/dc1/services/band-service/instances


6. Open the browser:
   http://localhost:8082
   In Song 1 you should see Slash in the "Registered Services" panel. Click "Play 10 notes" to test.



You can also view the Consul UI directly at http://localhost:8500.

Registering Slash with tags directoy on Consul API
```bash
curl -X PUT -d '{"ID": "Slash", "Name": "band-service", "Address": "localhost", "Port": 9000, "Tags": ["guns-n-roses","slash"]}' http://127.0.0.1:8500/v1/agent/service/register
```
`

Deleting a service instance

```bash
curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/Slash
```



mvn spring-boot:run -pl dispatcher-spring

en modo dev tienes la Dev UI de Quarkus en:

http://localhost:8080/q/dev-ui

Ahí verás la sección de Micrometer donde puedes explorar todas las métricas registradas, incluyendo las de Stork. También puedes ver el endpoint Prometheus directamente en:

http://localhost:8080/q/metrics

Y si quieres verlo formateado, filtra solo las de Stork:

http://localhost:8080/q/metrics?q=stork

La Dev UI es interesante para la presentación porque muestra visualmente todas las extensiones activas, su configuración y los beans CDI — podrías usarla como apoyo visual para explicar cómo Quarkus
integra Stork.

Vienen out of the box con solo tener estas dos dependencias en el pom y la property:

  <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
  </dependency>

quarkus.micrometer.binder.stork.enabled=true

No hay ninguna anotación que añadir. El binder de Stork para Micrometer se activa automáticamente vía CDI y empieza a recoger métricas de cada llamada que pasa por Stork — discovery duration,
selection duration, failures, instancias encontradas — sin tocar nada del código de tu dispatcher.

Eso es precisamente lo que hace la demo impactante: el mismo código que ya tenías empieza a emitir métricas de producción con una sola línea de config.
