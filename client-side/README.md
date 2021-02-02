# Client-side Development

Este documento contiene detalles adicionales acerca de la estructura del
cliente, y de las operaciones disponibles sobre la misma.

## Dependencies

En cualquier momento es posible agregar dependencias sobre el cliente, tanto
para desarrollo (utilizando _NPM_), o de producción (utilizando _Bower_). En
particular:

* __Dependencia de desarrollo__: agregar la misma al archivo `package.json`.
* __Dependencia de producción__: agregar la misma al archivo `bower.json`.

Luego de agregar, modificar, o eliminar las dependencias, ejecute un deploy
completo bajo _Maven_, o simplemente reconstruya el cliente ejecutando en este
directorio:

	npm install

Este comando construirá el cliente y lo montará en el servidor, específicamente
bajo la ruta `server-side/webapp/src/main/webapp/`. Recuerde que no es
necesario reconstruir el servidor si sólo se modificó el cliente. Sin embargo,
es posible que el servidor deba __reiniciarse__ para percibir los cambios.

Además, es posible instalar de forma global las dependencias con las cuales se
construye el proyecto, a modo de evitar la reconstrucción completa. Por
ejemplo, puede instalar los siguientes paquetes:

	npm install -g bower
	npm install -g gulp

De esta forma, puede instalar dependencias de producción utilizando _Bower_, o
reconstruir el cliente ejecutando _Gulp_. Se dispone una tarea especial en
_Gulp_ que permite vaciar el servidor (_i.e._, eliminar el cliente dentro de la
web-app):

	gulp clean

Para cada dependencia agregada y/o modificada, quizás sea necesario realizar
algunos pasos adicionales descriptos en las siguientes secciones.

## Adding a Development Dependency

Una vez que la dependencia fue instalada mediante _NPM_, puede utilizarse en
cualquier script de configuración, por ejemplo, en `gulpfile.js`. Debido a que
estos scripts ejecutan sobre _Node.js_, se logra obtener la dependencia
mediante:

```javascript
const dependency = require("name-of-dependency");
```

## Adding a Production Dependency

En este caso, luego de instalar la dependencia utilizando _Bower_, es necesario
ubicar la misma. En primer lugar, se debe indicar en el archivo `lib.json` los
archivos a utilizar, tanto de tipo _CSS_ como _JS_. Estos archivos se
encontrarán disponibles en la carpeta `lib/` luego de construir el cliente. Si
los archivos especificados no estaban minificados, se minificarán durante la
ejecución del task-runner.

Para solicitar una dependencia mediante __AMD__ (_Asynchronous Module
Definition_), es necesario agregar una entrada en `require-build.js`, por
ejemplo, para el caso de _AngularJS_:

```javascript
...
paths : {
    "lib/angular" : "empty:",
    ...
}
...
```

Esto impide que la librería se integre en los módulos que la usen, y que se
solicite al igual que un tag __<script\>__ de _HTML_.

Además, en `require-runtime.js` es posible que la dependencia deba expresarse
dentro del bloque _shim_ debido a que la misma puede utilizar un esquema que no
sea _AMD_. Es posible en esta misma sección definir dependencias, lo que
permite descargar y ejecutar dichos scripts en un orden determinado (como es el
caso de `lib/angular-route`).

En el objeto `sri` (_Sub-Resource Integrity_), se puede definir el hash de
verificación al descargar una dependencia. Es necesario considerar que este
hash se debe corresponder con la versión final (minificada), y que por lo tanto
se deba construir el cliente dos veces (una para calcular el hash del archivo
final y otra luego de actualizar el `sri` correspondiente). Su uso no es
obligatorio.

## Running

Es posible desplegar un servidor local de prueba ejecutando:

	npm start

Este comando despliega el cliente (que debe ser construido previamente
utilizando _Gulp_), bajo la ruta __http://localhost:8080/__.

## Testing

Los test (o especificaciones), deben agregarse al directorio `tests/`, y deben
finalizar con el sufijo `-spec.js` para que _Jasmine_ pueda validarlos. Además,
es posible construir librerías de soporte (_helpers_), dentro del directorio
`tests/helpers/`. De cualquier modo, los tests se pueden verificar ejecutando
en este directorio:

	npm test

Para construir el cliente por completo y luego testear, ejecute:

	npm install-test

## Module Bundles (with AMD)

Existen dos tipos de _bundle_ que se pueden construir:

* __Desde un JS__: en este caso, se escribe a mano un archivo _\*.js_ en el
cual se define un nuevo módulo y sus dependencias, utilizando sintaxis de
_RequireJS_ (_e.g_, el módulo principal `js/app.module.js`).
* __Sintetizados__: en este caso el módulo no existe, sino que se conforma
directamente especificando un nombre y un conjunto de dependencias.

En cualquiera de los casos, es necesario modificar el archivo `bundles.js`, el
cual permite construir módulos. Para los módulos sintetizados, en particular,
se debe utilizar la opción `create : true`. Si bien los mismos pueden ubicarse
en cualquier parte del cliente, es esperable que los sintetizados se ubiquen
bajo el directorio `amd/`.

## Style Bundles

Para construir bundles de archivos _CSS_ se puede utilizar _SCSS_. En este
caso, _Gulp_ permite compilar dichos documentos, resolviendo los mismos dentro
del directorio `css/`, automáticamente.

## Security

Para agregar accesos a recursos estáticos (_i.e._, aquellos que atraviesan
directamente el filtro de _Spring Security_), es necesario agregar una
excepción en la configuración de seguridad, dentro del servidor, bajo la ruta
`server-side\webapp\src\main\java\ar\edu\itba\paw\webapp\config\SecurityConfig.java`,
más específicamente, en el sigueinte método:

```java
@Override
public void configure(final WebSecurity web) {
    web.ignoring()
        .antMatchers(GET, "/components/**/*.css", "/css/**/*.css", "/lib/css/**/*.css")
        .antMatchers(GET, "/components/**/*.html", "/errors/**/*.html")
        ...
}
```
