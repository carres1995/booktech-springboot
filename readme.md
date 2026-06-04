# LibroTech

![CI Status](https://github.com/carres1995/booktech-springboot.git/actions/workflows/ci.yml/badge.svg)

Sistema de gestión de biblioteca desarrollado con Spring Boot.

### Diferencias entre EntityGraphj y Join Fetch

las 2 eliminan el problema que genera el N+1 que es el hecho que por cada consulta si contiene una relacion compleja la
convierte en un loop infinito de anidados.
con join fetch puedes tener mas control de la consulta puesto que realizasespecificaciones directamente en la query. se
usa el distinct para evitar duplicados
JOINFETCH:consultas complejas ,filtros avanzados, tuning SQL específico, optimización crítica, queries muy personalizada

@EntityGraph: Esta es probablemente la mejor herramienta para la mayoría de consultas. puesto que le especificamos las
las relaciones internas que vamos a cargar, el proceso lo genera hibernate no hay que hacer JPQL, normal mente genera un
leftjoin de la entidad, reutilizacion de metodos estandar, integracion mas simple, mantenibilidad, simplemente agreagas
relaciones. usarlo cuando hay paginacion.
desventajas: No tienes control del SQL, no sirve para logica compleja(filtros, dtos complejos, subquerys, agregaciones,
group by, case)

Usar EntityGraph para la mayoría de casos

es más limpio
más mantenible
mejor integración Spring
menos JPQL manual
JOIN FETCH se usa más para:
optimizaciones específicas
consultas críticas
casos complejos
reportes
tuning SQLv(JOIN FETCH sería útil si haces:,WHERE, ORDER BY, filtros complejos, consultas específicas)

### Page/Slice

Page ejecuta una doble consulta:
y devuelve: contenido actual, total de paginas, total de elementos, tamaño, numero de pagina, siguiente p y anterior.
la primera consulta es: SELECT * FROM books LIMIT 20
la segunda: SELECT COUNT(*) FROM books

Slice solo conoce el contenido actual, y si hay siguiente pagina.
consulta: SELECT * FROM books LIMIT 21, ese de mas es para ver si hay una siguiente.

formas de carga: Page: pagina 1 de 45
Slice: cargar mas...
por optimizacion claramente slice.

pero cuando es la ejecucion de un crud necesitamos totales de datos como experiencia de usuario en ciertos casos es
mejor usar page.

### ¿Qué pasaría si dos desarrolladores hacen push al mismo tiempo? ¿Se ejecutarían dos pipelines en paralelo?

Sí, la respuesta corta es sí, se ejecutarían dos pipelines en paralelo. GitHub Actions está diseñado para ser escalable
y eficiente, por lo que no hace esperar a un desarrollador mientras otro está desplegando.

Aquí te explico exactamente cómo maneja esto la plataforma y qué debes tener en cuenta:

1. Ejecución en paralelo (Independencia)
   Cada vez que haces un push, GitHub detecta el evento y crea una instancia de ejecución (Workflow Run) independiente.
   Si el Desarrollador A y el Desarrollador B envían código simultáneamente:

GitHub activará dos procesos separados.

Cada proceso tiene su propia máquina virtual (runner).

No se "bloquean" entre sí porque, en teoría, cada proceso es atómico.

2. El desafío: El "Estado Final" (Race Conditions)
   Aunque los pipelines corran en paralelo, si ambos desarrolladores están modificando el mismo entorno (por ejemplo,
   desplegando en el mismo servidor de AWS o actualizando la misma base de datos), puedes tener un problema llamado "
   Race Condition" (Condición de Carrera).

Ejemplo: El pipeline A comienza a desplegar la versión 1.0. Mientras, el pipeline B termina de compilar y comienza a
desplegar la versión 1.1 sobre la misma carpeta.

Resultado: Podrías terminar con una mezcla de archivos o una versión inestable en el servidor porque los despliegues se
solaparon.

### ¿Por qué es útil cachear las dependencias de Maven? ¿Cuánto tiempo ahorraría aproximadamente?

Cachear las dependencias de Maven es una de las optimizaciones más impactantes que puedes aplicar en un pipeline de
CI/CD. Básicamente, consiste en guardar la carpeta .m2/repository (donde Maven almacena todas las librerías descargadas)
para que no tengan que volver a bajarse desde internet en cada ejecución.

### El badge de estado (esa pequeña imagen que suele ir al principio del README.md) es una herramienta diseñada para la comunicación instantánea y externa.

Si bien abrir el repositorio te da el detalle técnico, el badge te entrega información de contexto rápida que sirve para
diferentes propósitos:
Visibilidad fuera del repositorio:

- La página de inicio del proyecto (README): Cualquier persona, incluso alguien que no sabe qué es un pipeline, ve un
  círculo verde que dice "Passing". Esto genera confianza inmediata: el software funciona.

- Documentación externa: Puedes incrustar el badge en una página web, un foro o un portal corporativo, permitiendo que
  usuarios o clientes vean la salud del proyecto sin entrar a GitHub.

Diagnóstico "de un vistazo": Indica el estado del último push en la rama principal (main) de forma automática. Si ves un
badge rojo que dice "Failing", sabes instantáneamente que hay un error bloqueante, sin necesidad de navegar por toda la
interfaz de GitHub.

Diferenciación técnica (Lo que el repo NO muestra a simple vista):
Estado de despliegue: Puedes tener un badge que indique si el código fue desplegado con éxito en el servidor de
producción (por ejemplo, "Deployed to AWS").

Cobertura de pruebas (Code Coverage): Algunos badges (como los de Codecov o SonarQube) muestran un porcentaje de cuánto
de tu código está cubierto por tests. Abrir el repositorio no te dice esto a menos que busques el reporte específico.

Calidad de código: Badges que indican vulnerabilidades de seguridad o deuda técnica.

### Para calcular esto, primero determinemos cuántos minutos consumes al día y luego multipliquemos por los días de un mes promedio.

El cálculo:
Minutos por día:

3 minutos por ejecución × 20 pushes al día = 60 minutos diarios.

Minutos por mes:

60 minutos al día × 30 días = 1,800 minutos mensuales.

¿Es mucho o poco?
Plan Gratuito: GitHub ofrece 2,000 minutos gratuitos al mes para repositorios privados en cuentas personales.

Con 1,800 minutos, estarías justo por debajo del límite gratuito.

Repositorios Públicos: Si tu proyecto es público, no consumes minutos de tu cuota, por lo que serían ilimitados.

¿Cómo optimizar esto si te pasas del límite?
Si decides expandir tus proyectos y superas los 2,000 minutos, aquí te doy tres estrategias para bajar ese consumo:

Uso de Caché: Como vimos anteriormente, cachear las dependencias de Maven puede reducir el tiempo de ejecución de 3
minutos a quizás 1.5 o 2 minutos.

Ejemplo: Si bajas a 2 minutos, tu consumo mensual caería a 1,200 minutos, dándote mucha más holgura.

Filtrar disparadores (Push filtering): ¿Necesitas que el pipeline corra con cada push? Puedes configurar el archivo .yml
para que solo corra cuando subas código a la rama main o cuando modifiques archivos específicos.

YAML
on:
push:
branches: [ "main" ]
paths:

- 'src/**' # Solo corre si cambias código fuente
  Ejecución paralela: Si tienes muchos tests, dividir tu suite de pruebas en varios jobs que corran en paralelo reduce
  el tiempo total de reloj (aunque el consumo total de minutos sea similar, el pipeline termina más rápido).

### que es un artifact?

En el contexto de la ingeniería de software y los pipelines, un artifact (artefacto) es cualquier archivo o paquete
generado como resultado de un proceso de compilación o construcción (build).

### query consultas filtros avanzados

Hibernate:
select distinct b1_0.id, b1_0.title, b1_0.fecha_publicacion, b1_0.price, e1_0.name, e1_0.country
from books b1_0
join editorials e1_0 on e1_0.id=b1_0.editorial_id
where lower(e1_0.country) like lower(('%'||?||'%')) escape ''
order by b1_0.fecha_publicacion desc
fetch first ? rows only

