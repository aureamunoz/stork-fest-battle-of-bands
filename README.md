# Desarrollo de la demo STORKFEST

### Song 1 : autoregistro + service discovery con Consul

1. Arranca Consul:
   podman run --rm -p 8500:8500 hashicorp/consul:1.20 agent -dev -client=0.0.0.0

2. Arranca el primer guitarrista (Slash):
   mvn quarkus:dev -pl guitar-hero-service
   (Por defecto: Slash, puerto 9000, delay 300ms)

3. Verifica en Consul que se registró:
   curl -s http://localhost:8500/v1/catalog/service/band-service | jq

4. Arranca el dispatcher-quarkus (en otra terminal):
   mvn quarkus:dev -pl dispatcher-quarkus -Ddebug=false

5. Arranca el UI (en otra terminal):
   mvn quarkus:dev -pl ui -Ddebug=false

6. Abre el navegador:
   http://localhost:8082
   En Song 1 deberias ver a Slash en el panel "Registered Services". Pulsa "Play 10 notes" para probar.

7. (Opcional) Arranca mas guitarristas para verlos aparecer en tiempo real:
   GUITAR_HERO=Hendrix GUITAR_HERO_PORT=9001 GUITAR_HERO_DELAY_MS=500 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false

GUITAR_HERO=Eddie GUITAR_HERO_PORT=9002 GUITAR_HERO_DELAY_MS=5 GUITAR_HERO_FAILURE_RATIO=20 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false

También puedes ver el Consul UI directamente en http://localhost:8500.



4. Arranca el dispatcher-quarkus (en otra terminal):
   mvn quarkus:dev -pl dispatcher-quarkus -Ddebug=false

5. Arranca el UI (en otra terminal):
   mvn quarkus:dev -pl ui -Ddebug=false

6. Abre el navegador:
   http://localhost:8082

En Song 1 deberias ver a Slash en el panel "Registered Services". Pulsa "Play 10 notes" para probar.

7. (Opcional) Arranca mas guitarristas para verlos aparecer en tiempo real:
   GUITAR_HERO=Hendrix GUITAR_HERO_PORT=9001 GUITAR_HERO_DELAY_MS=500 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false

GUITAR_HERO=Eddie GUITAR_HERO_PORT=9002 GUITAR_HERO_DELAY_MS=5 GUITAR_HERO_FAILURE_RATIO=20 mvn quarkus:dev -pl guitar-hero-service -Ddebug=false

También puedes ver el Consul UI directamente en http://localhost:8500.

