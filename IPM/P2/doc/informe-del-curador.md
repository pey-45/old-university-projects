# Informe del Curador-Traductor

  El traductor debe definir -con sus propias palabras- los objetivos
  de aprendizaje de la práctica.

  El curador buscará los recursos online que ayuden al equipo a
  completar de mejor manera su tarea.
  
  Además, el curador es el responsable de recopilar y organizar de
  forma esquemática, todas las fuentes adicionales de información que
  el grupo ha utilizado para desarrollar la actividad.

  Finalmente, el curador tiene la tarea de presentar y explicar a sus
  compañeros los recursos indentificados.

  > A continuación se ofrece una sugerencia sobre el contenido del
  > informe que debe generar la persona encargada de este role cada
  > semana.

  

## Objetivos de aprendizaje de la semana 1

  - Analizar las necesidades de los potenciales usuarios y plasmar en diagramas los flujos y escenarios clave que la aplicación debe cubrir.

  - Crear interfaces que se ajusten a las configuraciones de teléfonos móviles y relojes inteligentes, incorporar elementos para la gestión de errores y retroalimentación interactiva durante las operaciones de entrada/salida (E/S).

  - Elegir un patrón como MVP, MVVM o Provider, garantizando la separación entre el modelo/estado y la vista para una mejor mantenibilidad.

  - Representar la estructura estática (clases y relaciones) y dinámica (flujos de interacción) mediante diagramas UML usando Mermaid e incluir la documentación generada en un archivo markdown siguiendo el formato Github Flavored Markdown.
	
	
## Otros recursos empleados en la semana 1

 - Enlace a documentación sobre UML: Guía que explica cómo crear diagramas de casos de uso en UML, proporcionando ejemplos y pasos para representar actores, interacciones y flujos clave dentro de una aplicación. https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-uml-use-case-diagram/

  - Enlace a documentación sobre diseño adaptativo: Documentación oficial de Flutter que detalla cómo implementar diseños adaptativos y responsivos para distintos tamaños de pantalla, utilizando herramientas como MediaQuery y LayoutBuilder. https://flutter.dev/docs/development/ui/layout/adaptive-responsive

  - Enlace a documentación sobre patrones arquitectónicos: Guía de Flutter sobre diferentes enfoques para gestionar el estado, incluyendo patrones populares como Provider, que ayudan a mantener la separación entre la vista y la lógica de la aplicación. https://flutter.dev/docs/development/data-and-backend/state-mgmt



## Objetivos de aprendizaje de la semana 2

  - Construcción de Interfaces de Usuario (IGU):
	- Diseñar y desarrollar las pantallas principales para la consulta, registro de tomas, y manejo de errores.
	- Crear una UI responsiva compatible con teléfonos y relojes inteligentes.
  - Programación dirigida por eventos:
	- Implementar lógica reactiva para actualizar la UI en respuesta a interacciones del usuario, como marcar una toma realizada.
  - Concurrencia y manejo de tareas asíncronas:
	- Gestionar operaciones como consultas y actualizaciones en la base de datos local mediante funciones asíncronas.
  - Gestión robusta de errores:
	- Manejar errores relacionados con E/S, como fallos en la base de datos o almacenamiento local.
	- Proveer retroalimentación interactiva (SnackBars, AlertDialogs) en caso de errores.
	
## Otros recursos empleados en la semana 2

 - Enlace a ejemplos de diseño responsivo con Flutter: Tutorial oficial para implementar interfaces adaptativas que funcionen correctamente en distintos dispositivos, como teléfonos y relojes inteligentes.
https://docs.flutter.dev/development/ui/layout/adaptive-responsive

  - Enlace a documentación sobre programación asíncrona en Dart: Introducción al uso de funciones asíncronas, esenciales para manejar tareas como operaciones de entrada/salida sin bloquear la UI.
https://dart.dev/codelabs/async-await

  - Enlace a documentación sobre manejo de errores en Flutter: Explicación de cómo capturar y manejar errores comunes en Flutter, con ejemplos de try-catch y herramientas para mostrar mensajes de error en la UI de manera amigable.
https://docs.flutter.dev/testing/errors


## Objetivos de aprendizaje de la semana 3

  - Pruebas de extremo a extremo (end-to-end):
	- Implementar pruebas que simulen escenarios reales de uso para verificar la funcionalidad completa de la aplicación desde la interacción del usuario hasta la respuesta final del sistema.
   	- Diseñar pruebas que cubran flujos de interacción clave en la interfaz gráfica, garantizando que todos los componentes de la aplicación funcionen de manera integrada.

  - Gestión de errores en pruebas:
	- Desarrollar pruebas para escenarios donde ocurren errores de entrada/salida (E/S).
  	- Simular errores comunes de usuario, como entradas inválidas o interacciones no previstas, y verificar que la aplicación maneja estos casos de manera adecuada.
          
## Otros recursos empleados en la semana 3

  - Enlace a documentación sobre pruebas en Flutter:
    	- Guía oficial para implementar pruebas end-to-end. Incluye ejemplos prácticos para crear y ejecutar escenarios de prueba completos. https://docs.flutter.dev/testing/integration-tests

  - Enlace a tutorial sobre simulación de errores en pruebas:
    	- Tutorial que explica cómo simular condiciones de error en pruebas de Flutter, como fallos en la red o excepciones de la base de datos. https://flutter.dev/docs/cookbook/testing/integration

  - Enlace a documentación sobre herramientas de automatización de pruebas:
    	- Explicación detallada sobre el uso de herramientas de automatización, como flutter_test y integration_test, para desarrollar y gestionar pruebas end-to-end. https://blog.codemagic.io/e2e-testing-flutter/
