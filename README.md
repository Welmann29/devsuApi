# Test Api

## Montar el proyecto local en docker 
Para montar el proyecto de manera local solo se debe correr el docker-compose, este levantara la aplcicacion y las bases de datos requeridas, asi mismo creara el 
modelo relacional en la base de datos 

Posicionese dentro de la carpeta bankapi y ejecute 
```
docker-compose up -d
```

## Documentacion del api 
La documentación del api se encuentra en Swagger y permite realizar pruebas en ella, para acceder a la documentacion debe entrar en:
**http://{host}:8080/api/docs.html**

## Proyecto montado
Actualmente una instancia del proyecto se encuentra montado en GCP, el link del mismo se compartio por los comentarios privados de la prueba 

Para los endpoints que requieren funcionario se registraron 2, los codigos son 1 y 2, cada uno con una agencia distinta, esto para probar los ids generados por 
agencia

