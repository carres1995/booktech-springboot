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

