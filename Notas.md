## Mejoras Stork después de la demo.

Registrar tags/labels junto con las instancias. 
Vale, lo dejamos asi para la demo pero explicamelo un poco mejor? no acabo de entender la necesidad de exponer la config

⏺ Claro. El flujo actual de auto-registration es:

1. Quarkus arranca y lee quarkus.stork.band-service.service-registrar.* del application.properties
2. Quarkus llama internamente a registrar.registerServiceInstance(serviceName, ip, port)
3. El servicio se registra en Consul con nombre, IP y puerto — nada más

El problema: esa llamada interna que Quarkus hace no pasa tags ni metadata. Y como es Quarkus quien hace la llamada (no tú), no puedes intervenir para añadir Map.of("guitarist", "Slash").

Para poder hacerlo vía config necesitarías algo como:

quarkus.stork.band-service.service-registrar.tags=slash
quarkus.stork.band-service.service-registrar.metadata.guitarist=Slash

Pero esas propiedades no existen hoy ni en Stork ni en la extensión de Quarkus. Alguien tendría que implementarlas — que Stork las lea y las pase al RegistrarOptions internamente.

La alternativa sería hacer el registro programáticamente (como el ConsulRegistration.java que creamos y borramos), donde tú controlas la llamada y puedes pasar los tags que quieras. Pero entonces
pierdes la magia del "auto" — hay que escribir código.

Resumiendo: la API de Stork sí soporta tags/metadata, pero el puente automático de Quarkus (config → registro) no los expone. Por eso usamos el mapeo por puerto como workaround.

