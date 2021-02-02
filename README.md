# GetOut© Web Application

La aplicación se compone de un servidor desarrollado bajo _Spring Framework_ y
arquitectura _REST_ (mediante _Jersey_), utilizando una capa de persistencia
_Hibernate + PostgreSQL_. El cliente emplea el framework _AngularJS_, un
sistema de módulos _AMD_ bajo _RequireJS_, y una interfaz _Bootstrap_. El sitio
final y los archivos de log pueden accederse desde:

* __Sitio__: http://pawserver.it.itba.edu.ar/paw-2017b-3/
* __Logs__: http://pawserver.it.itba.edu.ar/logs/paw-2017b-3.yyyy-mm-dd.log

Donde el log del día actual se corresponde con el formato _dd/mm/yyyy_.

## Client-side

Para más información sobre el desarrollo del cliente, vea:

* [README del _client-side_.](client-side/README.md)

## Deployment

Para construir el proyecto es necesario que el sistema posea un compilador
**_Maven 3_**, y una versión actual de **_Node.js_** (junto con _NPM_),
superior a _10_. Luego, sobre el subdirectorio `server-side` del proyecto,
ejecute:

	mvn clean package

Este comando se encarga de:

1. Compilar cada capa de la aplicación servidor.
2. Ejecutar tests sobre la capa de aplicación, persistencia y servicios.
3. Instalar las dependencias de desarrollo para la aplicación cliente,
utilizando _NPM_.
4. Instalar las librerías para el cliente, utilizando _Bower_.
5. Testear la aplicación cliente, utilizando _Jasmine_.
6. Construir el cliente final y trasladarlo al servidor, mediante _Gulp_.
7. Compilar el _WAR_ final.

Finalmente, encontrará en la ruta `server-side/webapp/target/app.war` la
aplicación empaquetada en formato *\*.war*, lista para desplegarse directamente
en un _servlet container_. Actualmente, la aplicación despliega correctamente
sobre:

* **_Apache Tomcat 7.0.76_**, o superior.
* **_Eclipse Jetty 9.4.12_**, o superior.

## Designers

El proyecto fue construido, es diseñado y mantenido por:

* @mbiagini
* @jfilipic
* @agustin-golmar
