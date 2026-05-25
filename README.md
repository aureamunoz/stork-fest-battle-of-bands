# Storkfest Demo

### Song 1: Auto-registration + Service Discovery with Consul

1. Start Consul:
   ```
   podman run --rm -p 8500:8500 hashicorp/consul:1.20 agent -dev -client=0.0.0.0
   ```

2. Start the first guitarist (Slash):
   ```
   mvn quarkus:dev -pl guitar-hero-service
   ```
   (Default: Slash, port 9000, 300ms delay)

3. Verify registration in Consul:
   ```
   curl -s http://localhost:8500/v1/catalog/service/band-service | jq
   ```

4. Start the Quarkus dispatcher (new terminal):
   ```
   mvn quarkus:dev -pl dispatcher-quarkus -Ddebug=false
   ```

5. Start the UI (new terminal):
   ```
   mvn quarkus:dev -pl ui -Ddebug=false
   ```

6. Open the browser:
   http://localhost:8082
   In Song 1 you should see Slash in the "Registered Services" panel. Click "Play 10 notes" to test.

7. (Optional) Start more guitarists to see them appear in real time:
   ```
   GUITAR_HERO=Hendrix GUITAR_HERO_PORT=9001 GUITAR_HERO_DELAY_MS=500 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false
   GUITAR_HERO=Eddie GUITAR_HERO_PORT=9002 GUITAR_HERO_DELAY_MS=5 GUITAR_HERO_FAILURE_RATIO=20 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false
   GUITAR_HERO=Flying GUITAR_HERO_PORT=9003 GUITAR_HERO_DELAY_MS=5 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false
   ```

You can also view the Consul UI directly at http://localhost:8500.