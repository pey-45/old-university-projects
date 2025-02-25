# Informe del Facilitador-Administrador

## 30/9 - 28/10
 
Durante estas semanas la tarea principal ha sido crear los diagramas y el código del programa basándonos en el diseño estático.
    
En el momento actual la app cuenta con una pantalla principal con una lista de pacientes en la parte izquierda en la que se muestra su código y su nombre, y una lista de medicamentos del paciente seleccionado en la parte derecha.

De forma más detallada, en esta pantalla se puede:

- Buscar paciente por nombre o código
- Ver información sobre un paciente y sus medicaciones
- Añadir, editar y eliminar un paciente
- Añadir y editar medicación de un paciente
- Ver la pantalla de About

Las pantallas de editar y añadir, tanto medicaciones como pacientes, son la misma pantalla con los cambios necesarios en base a lo que se quiera hacer. Estas son parecidas, se llenan los campos requeridos menos la ID, que se maneja internamente.

La posología se maneja de una forma completamente abstracta para el usuario, simplemente se pone en el último campo de editar/añadir medicación las horas en un formato determinado separadas por comas.

## 29/10-3/11

Esta semana nos hemos centrado en corregir varios aspectos del proyecto:
- Eliminar casos de uso asociados a la gestión de pacientes.
- Añadir placeholders para mejorar la UX.
- Eliminar la lista de pacientes para utilizar un sistema simple de búsqueda por código.
- Eliminar las IDs tanto de pacientes como de medicaciones de la interfaz.
- Respetar la jerarquía de capas, no accediendo directamente a elementos de una capa ajena.
- Cambiar la página de editar medicación a una ventana.

Además de esto, la tarea principal de esta semana ha sido gestionar tanto los errores de capa modelo como los de conexión mediante el uso de excepciones personalizadas y diálogos.

También nos hemos dedicado en parte a corregir otros aspectos de la fase anterior para hacer un código más limpio y usar buenas prácticas de POO.

## 4/11-11/11

El trabajo de esta semana se resume en rehacer prácticamente todo el código para seguir buenas prácticas de arquitectura multicapa y poder implementar la gestión de concurrencia de una forma adecuada. Esto para proceder con la internacionalización.

Los objetivos clave han sido:
- No tener getters en el presenter para que el view llame indirectamente al model, si no que el presenter le proporciona todo lo necesario.
- Arreglar el error de generar IDs.
- Reducir considerablemente las llamadas a la API mediante caché (variables selected_patient/medications/posologies).
- Mejorar la gestión de errores informando al usuario.
- Implementar correctamente la concurrencia.
- Optimizaciones y mejoras generales.
- Implementar la internacionalización del proyecto al español y al gallego.
