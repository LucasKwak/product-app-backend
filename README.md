# PRODUCT APP BACKEND

Backend para mi aplicación de productos. En él utilizo SpringSecurity, SpringJPA y SpringWeb. 

## Tabla de Contenidos

- [Instalación](#instalación)
- [Uso](#uso)

## Instalación

Instrucciones para configurar el proyecto localmente. Se incluye el proceso para dockerizar la aplicación y utilizar un contenedor mysql.

### Crear el .jar
Primero hay que asegurarse que en ***application.properties*** esté marcado el fichero de dev:
```java
spring.profiles.active=dev
```

Para crear el .jar hay que ejecutar maven en la raíz del proyecto de esta forma (Esto se hace así porque todavía no se arrancó el contendor mysql y por eso si se ejecutan tests dará un error de conexión):
```bash
mvn clean compile package -Dmaven.test.skip=true
```
### Ejecutar Docker
#### Opción A) 
Ejecutamos docker-compose en la raíz del proyecto (esto nos creará un contenedor padre con dos hijos: db y my-app):
```bash
docker-compose up --build -d
```

#### Opción B) 
Ejecutamos docker en la raíz del proyecto (esto nos creará una imagen de la aplicación). Como esta opción no crea una imagen mysql, la tendrás que crear aparte y luego ejecutar primero el contenedor mysql y luego el de la app:
```bash
docker build -d -t <nombre imagen>:<tag>
```
Creamos y arrancamos el contenedor de la aplicación usando la imagen antes creada:
```bash
docker run --name <nombre del contenedor> -d -p <puerto al exterior>:<puerto dentro del contenedor> <nombre imagen>:<tag>
```

## Uso

### Roles

El sistema consta de tres roles: ADMINISTRATOR, ASSISTANT_ADMINISTRATOR y CUSTOMER

Cada rol puede realizar unas determinadas operaciones:
  - Administrator:
    - READ_ALL_PRODUCTS
    - READ_ONE_PRODUCT
    - CREATE_ONE_PRODUCT
    - UPDATE_ONE_PRODUCT
    - DISABLE_ONE_PRODUCT
    - READ_ALL_CATEGORIES
    - READ_ONE_CATEGORY
    - CREATE_ONE_CATEGORY
    - UPDATE_ONE_CATEGORY
    - DISABLE_ONE_CATEGORY
    - READ_ALL_CUSTOMERS
  - Assistant_administrator:
    - READ_ALL_PRODUCTS
    - READ_ONE_PRODUCT
    - UPDATE_ONE_PRODUCT
    - READ_ALL_CATEGORIES
    - READ_ONE_CATEGORY
    - UPDATE_ONE_CATEGORY
    - READ_ALL_CUSTOMERS
  - Customer:
    - READ_ALL_PRODUCTS
    - READ_ONE_PRODUCT
    - READ_ALL_CATEGORIES
    - READ_ONE_CATEGORY
    - READ_MY_PROFILE
  - Operaciones públicas:
    - REGISTER_ONE
    - LOGIN
    - AUTHENTICATE
    - LOG_OUT

### Endpoints

Los endpoints que hay son:
  - GET .../my-backend/products : para ver todos los productos
  - GET .../my-backend/products/{productId} : para ver un producto
  - POST .../my-backend/products : para crear un producto
  - PUT .../my-backend/products/{productId} : para actualizar un producto
  - PATCH .../my-backend/products/{productId}/disabled : para deshabilitar un producto
  
  - GET .../my-backend/categories : para ver todas las categorias
  - GET .../my-backend/categories/{categoryId} : para ver una categoria
  - POST .../my-backend/categories : para crear una categoria
  - PUT .../my-backend/categories/{categoryId} : para actualizar una categoria
  - PATCH .../my-backend/categories/{categoryId}/disabled : para deshabilitar una categoria

  - GET .../my-backend/auth/profile : para obtener la información de cuenta
  - POST .../my-backend/auth/authenticate : para iniciar sesión
  - POST .../my-backend/auth/logout : para cerrar sesión

  - POST .../my-backend/customers : para registrar un cliente

### Security

Para la seguridad utilizo jwt (tuve que crear un filtro para la gestión de estos jwt) y además un authorization manager personalizado (para profundizar en cómo se comprueba si un usuario tiene los permisos necesarios). En la bd también se guardan los jwt que un usuario puede tener (esto se hace para que cuando cierre sesión el jwt que estaba usando quede invalidado). 

### Base de datos

En la base de datos se guarda toda la información de los usuarios, los roles que hay, las operaciones que hay, los módulos que hay, los productos y las categorías.

El ER de la base de datos es el siguiente:

<img width="600" alt="BD-ER" src="https://github.com/user-attachments/assets/9ea00fe2-9802-44f5-9317-7d424aa2f832">




