# 💰 Money Calculator

Aplicación para el cálculo y análisis de cambios de divisa, con soporte para históricos, base de datos y una interfaz gráfica mejorada.

---

## ✨ Cambios respecto al proyecto base

* 📊 Se ha añadido una **base de datos** capaz de almacenar los datos utilizados por el programa.
* 📈 Se ha incorporado un **nuevo comando** que muestra la evolución del tipo de cambio de una divisa en un período máximo de **10 unidades de tiempo**:

    * día
    * mes
    * año
    * década
* 📅 Se ha implementado un **selector de fechas mejorado** y más intuitivo.
* 🖥️ Se ha modificado la **interfaz gráfica** para mejorar la experiencia de usuario.

---

## 🔑 Configuración de la API

1. Accede a [https://freecurrencyapi.com](https://freecurrencyapi.com) y obtén una clave gratuita.

2. Dentro de la carpeta:

   ```
   /src/main/resources
   ```

3. Crea un archivo llamado:

   ```
   .keys
   ```

4. Introduce tu clave de la API dentro del archivo (según el formato esperado por el proyecto).

---

## 🚀 Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone <url-del-repositorio>
   ```

2. Accede al directorio del proyecto:

   ```bash
   cd money-calculator
   ```

3. Asegúrate de haber configurado correctamente el archivo `.keys`.

4. Ejecuta el proyecto desde tu IDE o mediante el sistema de construcción correspondiente (por ejemplo, Maven o Gradle).

---

## ℹ️ Consideraciones importantes

* Si el sistema **no encuentra un valor de cambio**, devolverá **1** por defecto.
* Las fechas disponibles están limitadas a aquellas **posteriores al año 2000**.

---

## 🛠️ Tecnologías utilizadas

* SQLite
* Gson
* JCalendar
* JFreeChart
* FlatLaf
* Free Currency API REST