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

Claro. El truco está en las variables CSS de reveal.js y en Google Fonts.

**1. Cargar los pesos que quieras de Google Fonts**

```html
<link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800;900&display=swap" rel="stylesheet">
```

El número tras `wght@` es el peso. Puedes añadir más separados por `;`:
- `400` — Regular
- `600` — SemiBold
- `700` — Bold
- `800` — ExtraBold
- `900` — Black

**2. Sobreescribir las variables CSS de reveal.js en `:root`**

```css
:root {
    --r-main-font: 'Montserrat', Helvetica, sans-serif;    /* fuente del cuerpo */
    --r-heading-font: 'Montserrat', Helvetica, sans-serif; /* fuente de títulos */
    --r-heading-font-weight: 900;  /* peso de h1/h2/h3 */
    --r-main-font-weight: 800;     /* no siempre funciona solo */
}
.reveal { font-weight: 800; }     /* esto aplica el peso al cuerpo de verdad */
```

**3. Para retocar un elemento concreto** puedes añadir CSS normal:

```css
.reveal h1 { font-weight: 900; font-size: 2em; }
.reveal p   { font-weight: 700; }
.reveal li  { font-weight: 600; }
```

Todo esto va dentro del bloque `<style>` que ya tienes en el `templates/index.html`, entre las líneas 10 y 195 aproximadamente.

#### Cambio de theme

Sí, hay varios temas claros disponibles en tu instalación de reveal.js:

- **`white.css`** — fondo blanco, texto negro (el más limpio)
- **`white-contrast.css`** — igual pero con más contraste
- **`beige.css`** — fondo crema/marfil, más cálido
- **`simple.css`** — minimalista, tipografía con serifa
- **`solarized.css`** — fondo amarillo suave (estilo solarized)
- **`serif.css`** — fondo claro con fuente serif

Para probar cualquiera, solo cambia esta línea en `templates/index.html`:

```html
<link rel="stylesheet" href="/reveal.js/dist/theme/black-contrast.css">
```

por ejemplo:

```html
<link rel="stylesheet" href="/reveal.js/dist/theme/white.css">
```

Solo esa línea:

  <link rel="stylesheet" href="/reveal.js/dist/theme/beige.css">

Cambia beige.css por cualquiera de los que te mencioné y refresca el navegador.


Song 3 : demo service discovery

1. Play sequential con 3 guitarristas arrancados → se ven los 3 alternándose (round-robin)
2. Clear
3. Arranca Flying V → enseña la guitarra flotante, Flying aparece en Consul
4. Play sequential inmediatamente → solo salen los mismos 3, Flying no aparece. "¿Veis? Flying está en Consul pero Stork no lo sabe todavía — la caché no se ha refrescado"
5. Espera ~10 segundos (con el refresh-period que tienes a 10s)
6. Clear + Play sequential → ahora sí aparece Flying. "La caché se ha refrescado y Stork ha descubierto la nueva instancia"

Song 4: demo load balancing



Depende de la estrategia:

| Estrategia               | Mejor con      | Por qué                                                                                                         |
| ------------------------ | -------------- | --------------------------------------------------------------------------------------------------------------- |
| **round-robin**          | Sequential     | Se ve el patrón 1-2-3-4-1-2-3-4 limpio                                                                          |
| **random**               | Sequential     | Se aprecia la aleatoriedad sin ruido                                                                            |
| **least-requests**       | **Concurrent** | Con sequential los contadores siempre están a 0 y no se ve la diferencia. Concurrent muestra cómo Eddie acapara |
| **power-of-two-choices** | **Concurrent** | Mismo motivo — necesitas solapamiento para que la selección "2 al azar + menos carga" tenga sentido             |
| **least-response-time**  | Sequential     | Se ve cómo Stork aprende de cada respuesta y va ajustando. Eddie domina, luego falla y Stork le penaliza        |
| **sticky**               | Sequential     | Se ve claramente que todas van al mismo guitarrista, una tras otra                                              |

En resumen: las que miden **carga en curso** (least-requests, power-of-two-choices) necesitan concurrent. Las que miden **historial** o siguen un **patrón fijo** (round-robin, least-response-time, sticky) se ven mejor con sequential.

¿Quieres que añada esto como guía en las speaker notes de la demo?


