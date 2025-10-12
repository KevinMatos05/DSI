Instrucciones rápidas para ejecutar la aplicación JavaFX

Requisitos:
- JDK 11 o superior instalado y en PATH.
- Maven instalado y en PATH.

Opción A: Ejecutar con Maven (recomendado si el plugin javafx funciona)
1. Desde la carpeta del proyecto:
   mvn clean javafx:run

Opción B: Si Maven falla por falta de runtime (o quieres ejecutar desde VS Code)
1. Descarga y descomprime JavaFX SDK 17.0.2
   Ejemplo: C:\javafx-sdk-17.0.2\lib
2. En VS Code, usa la configuración de lanzamiento (Launch MainApp) que ya está en .vscode/launch.json
   - Asegúrate de ajustar el path en vmArgs si descomprimiste en otra carpeta.

Comprobar versiones y PATH:
  java -version
  mvn -v

Si quieres que modifique el POM para usar dependencias nativas por plataforma (win/linux/macos) y evitar descargar el SDK manualmente, dime y lo hago.
