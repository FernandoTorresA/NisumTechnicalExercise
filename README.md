# Nisum

## Ejercicio Práctico Java

El componente en cuestión es un CRUD de usuarios básiico, pero con el plus de generación de tokens de acceso mediante
JWT. Dichho token se crea al momento de crear un nuevo usuario y al logearse en un endpoint que se creó para dicho 
propósito. Existe también otro endpoint para validar la vegencia del token generado.

El proyecto fue creado con **Java 17 y Springboot** mediante el asistente de Intellij IDEA (initializer).
El manejo de dependencias se gestiona mediante gradle, importando de antemano dependencias básicas para la creación 
del proyecto (en el asistente). Las dependencias posteriores están comentadas en el archivo build.gradle

El código está hecho en base a alineamientos de buenas prácticas definido por herramientas como SonarCube. Es por 
ello que se hace uso estricto de llaves para métodos y condiciones aún cuando no sean estrictamente necesarias 
(una sola línea de código por ejemplo)

El proyecto por defecto utiliza servidor tomcat (springboot) y levanta el puerto 8080 de la máquina local. A partir de 
allí, se expone el requestMapping para el proyecto REST

Los parámetros como keys, tiempo de vida de los tokens y validadores de campos como correo, están definidos en el
property del proyecto **application.properties**

Se puede consultar el swagger del proyecto en la siguiente dirección una vez levantado en local:

http://localhost:8080/swagger-ui/index.html#/

El collection de los servicios está disponible en la carpeta resources en éste mismo proyecto, por lo que se puede
importar en un cliente como Postman:

***src/main/resources/extras/NisumTechnicalExercise.postman_collection.json***

### Se procede ha hacer una breve descripción de cada endpoint existente:


1- Ingresar Usuario (con RequestBody)
---------------------------------------------------------------------------
POST -> http://localhost:8080/nisum/usuarios/ingresarUsuario
Request body de ejemplo (Json):
```
{
    "nombre": "Alan brito",
    "email": "alan_brito@nisum.com",
    "password": "A123!sdfsdf",
    "telefonos": [
        {
            "numero": 98765432,
            "codigoCiudad": 2,
            "codigoPais": 59
        }
    ]
}
```

1- Obtener todos los usuarios
---------------------------------------------------------------------------
GET -> http://localhost:8080/nisum/usuarios/obtenerTodos

2- Ingresar usuario
---------------------------------------------------------------------------
POST -> http://localhost:8080/nisum/usuarios/ingresarUsuario

El servicio retornara la info creada más campos adicionales como fecha y un token temporal Request body de ejemplo 
(Json):
```
{
    "nombre": "Alan brito",
    "email": "alan_brito@nisum.com",
    "password": "A123!sdfsdf",
    "telefonos": [
        {
            "numero": 98765432,
            "codigoCiudad": 2,
            "codigoPais": 59
        }
    ]
}
```
3- Obtener usuario por email
---------------------------------------------------------------------------
GET -> http://localhost:8080/nisum/usuarios/obtenerUsuario?email=alan_brito@nisum.com

4- Actualizar usuario por email
---------------------------------------------------------------------------
PUT -> http://localhost:8080/nisum/usuarios/actualizarUsuario?email=alan_brito@nisum.com
Request body de ejemplo (Json):
```
{
    "nombre": "Alan Brito",
    "email": "alan_brito@nisum.com",
    "password": "A123!sdfsdf2",
    "telefonos": [
        {
            "numero": 98765432,
            "codigoCiudad": 2,
            "codigoPais": 59
        },
        {
            "numero": 987654321,
            "codigoCiudad": 21,
            "codigoPais": 591
        }
    ]
}
```
5- Eliminar usuario por email
---------------------------------------------------------------------------
DELETE -> http://localhost:8080/nisum/usuarios/borrarUsuario?email=alan_brito@nisum.com

6- Valida Tokens
---------------------------------------------------------------------------
Este endpoint **debe nutrirse del header de la solicitud** especificando una Key de nombre **Authorization** y como
value usar el token que se retorna al crear el nuevo usuario, ejemplo: 

**Bearer eykdfgdfgdfg...**

Se hace así con el propósito de emular lo que realmente se hace internamente en la llamada de servicios entre 
aplicativos (un front y backend por ejemplo).

Si el token es aún válido o no, este servicio lo puede comprobar (por defecto, los tokens tienen 30 segundos de vida).

GET -> http://localhost:8080/nisum/usuarios/validaToken?email=alan_brito@nisum.com

7- Login
---------------------------------------------------------------------------
Se puede usar este endpoint para generar otro token jwt en caso de que el generado en la creación haya expirado.
Basta con indicar las credenciales en el post para que se validen si son correctas y generar otro token.

POST -> http://localhost:8080/nisum/usuarios/login
Request body de ejemplo (Json):
```
{
    "email": "alan_brito@nisum.com",
    "password": "A123!sdfsdf"
}
```

Base de datos
---------------------------------------------------------------------------

Se utilizó HSQLDB para usar como base de datos en memoria. Tiene ciertas limitaciones, pero cumple con la mayoría de
requerimientos.

El schema en cuestión utilizado está en el siguiente directorio del proyecto:

***src/main/resources/schema.sql***

Ejecución de aplicativo
-----------------------------

Puede optarse por levantar la app directamente desde el IDE y que éste mismo resuelva las dependencias especificadas
en el archivo ***build.gradle***

Sin embargo, para facilitar su uso, se adjuntó dentro de este mismo proyecto el archivo Jar generado, más un .bat para
permitir su ejecución en ambientes Windows sin complicaciones.

Basta con copiar y pegar los archivos en algún directorio:

***src/main/resources/extras/execute_jar.bat***
***src/main/resources/extras/NisumTechnicalExercise-0.0.1-SNAPSHOT.jar***

Doble click en el archivo .bat y debería ejecutar el jar y levantar la app en el puerto 8080

### Recordar que se necisita tener instalado el JRE o JDK en la versión correspondiente (Java 17)
