# Money Calculator
## Cambios con respecto al proyecto base
- Se ha añadido una base de datos capaz de guardar datos usados por el programa
- Se ha añadido un comando nuevo que muestra la evolución de un cambio de divisa en un período máximo de 10 unidades de tiempo(día, mes, año, década)
- Se ha añadido un selector de fechas decente.
- Se ha modificado la interfáz gráfica.
## Nota importante al ejecutar el proyecto
En la carpeta resources(/src/main/resources), se debe crear un archivo llamado **.keys** que contenga la clave de la API.<br>
Esta clave se puede obtener de manera gratuita en https://freecurrencyapi.com
Tambien es importante aclarar que el sistema devolvera un valor de cambio de 1 en el caso de que no encuentre un valor, y las fechas son limitadas a aquellas posteriores al 2000