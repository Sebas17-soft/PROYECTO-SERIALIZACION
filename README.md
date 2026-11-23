# 🏆 Sistema de Gestión Completo de la Copa Mundial de Fútbol

## 🎯 Tema del Proyecto
Sistema de registro, serialización y persistencia de datos para administrar selecciones nacionales, plantillas de jugadores, partidos y resultados desde la fase de grupos, octavos, cuartos, semifinales hasta la gran final de la Copa Mundial de Fútbol, con operaciones CRUD completas en Java. Se puede ejecutar de las tres formas que enseña el profesor: **consola de texto**, **interfaz gráfica Swing** y **ventana JavaFX**.

---

## 📌 Conceptos de la Pizarra del Profesor Aplicados

1. **Serialización (`Serializable`)**: Conversión de los objetos `Seleccion`, `Jugador` y `Partido` en flujos de bytes para almacenarlos en archivos binarios `.dat`.
2. **Persistir = Guardar**: Toda la información del torneo no se pierde al cerrar el programa; queda almacenada de forma permanente en disco y se recupera automáticamente al iniciar.
3. **CRUD Completo**:
   - **[C] Create**: Registrar nuevas selecciones, jugadores y partidos de cualquier fase.
   - **[R] Read**: Consultar, filtrar y listar selecciones, plantillas por país y resultados.
   - **[U] Update**: Modificar estadísticas, marcadores de partidos y datos de futbolistas.
   - **[D] Delete**: Eliminar registros manteniendo la integridad y actualizando los archivos `.dat`.
4. **Tres Tipos de Interfaz**:
   - **Modo 1: Consola de Texto**: Menú interactivo estructurado con `Scanner` y validación de entradas.
   - **Modo 2: Interfaz Gráfica Swing**: Ventana de escritorio con `JTabbedPane`, formularios interactivos, `JTable` y cuadros de diálogo `JOptionPane`.
   - **Modo 3: Interfaz Gráfica JavaFX**: Ventana moderna basada en `Application`, paneles `BorderPane`/`TabPane`, `TableView` reactivo con `ObservableList` y alertas nativas.
5. **Persistencia en archivos `.dat`**:
   - `seleccionesMundial.dat`: Almacena la lista de selecciones nacionales y, por serialización en cascada, la plantilla completa de jugadores convocados por cada país.
   - `partidosMundial.dat`: Almacena todos los partidos disputados en cada fase del Mundial (Fase de Grupos, Octavos, Cuartos, Semifinal, Tercer Puesto y Final).

---

## ⚽ Estructura de Clases y Relaciones

- **`Jugador.java`** (`implements Serializable`):
  - Atributos: `idJugador`, `nombre`, `posicion`, `goles`, `tarjetasAmarillas`.
  - Encapsulamiento con Getters, Setters y `toString()`.

- **`Seleccion.java`** (`implements Serializable`):
  - Atributos: `idSeleccion`, `nombrePais`, `puntos`, `partidosGanados`, `golesAFavor`.
  - **Relación 1 a N**: `List<Jugador> listaJugadores`. Contiene su propia lista de jugadores, implementando métodos para agregar, buscar, actualizar y eliminar jugadores dentro de la selección.

- **`Partido.java`** (`implements Serializable`):
  - Atributos: `idPartido`, `fase`, `sel1`, `sel2`, `golesSel1`, `golesSel2`.
  - Métodos para determinar el equipo ganador, goles y formato textual.

- **`GestorCopaMundial.java`**:
  - Administrador de persistencia binaria (`ObjectOutputStream` / `ObjectInputStream`).
  - Métodos CRUD para Selecciones, Jugadores y Partidos.
  - Métodos estadísticos: Tabla de posiciones ordenada y Máximo Goleador (Bota de Oro).
  - Carga automática inicial con datos reales del Mundial (Argentina, Francia, Croacia, Marruecos, Brasil, España y sus encuentros hasta la Gran Final).

- **`MenuConsola.java`**:
  - Implementación completa del Modo 1 por consola estándar.

- **`VentanaSwing.java`**:
  - Implementación del Modo 2 con interfaz Swing de escritorio.

- **`VentanaJavaFX.java`**:
  - Implementación del Modo 3 con interfaz moderna JavaFX.

- **`Main.java`**:
  - Lanzador unificado que permite seleccionar o pasar como argumento el modo de ejecución deseado.

---

## 🚀 Instrucciones de Ejecución

### Opción A: Archivos por Lotes (.bat)
En la carpeta raíz del proyecto se incluyen accesos directos listos para hacer doble clic:
1. `compilar.bat`: Compila todo el código fuente en la carpeta `/out`.
2. `ejecutar_menu.bat`: Abre el menú principal para elegir la interfaz deseada.
3. `ejecutar_consola.bat`: Inicia directamente el Modo 1 (Consola).
4. `ejecutar_swing.bat`: Inicia directamente el Modo 2 (Ventana Swing).
5. `ejecutar_javafx.bat`: Inicia directamente el Modo 3 (Ventana JavaFX).

### Opción B: Por Línea de Comandos (Terminal)
```bash
# 1. Compilar todas las clases
javac -encoding UTF-8 -cp "lib/*" -d out src/*.java

# 2. Ejecutar Modo Consola
java -Dfile.encoding=UTF-8 -cp "out;lib/*" Main 1

# 3. Ejecutar Modo Swing
java -Dfile.encoding=UTF-8 -cp "out;lib/*" Main 2

# 4. Ejecutar Modo JavaFX
java -Dfile.encoding=UTF-8 --enable-native-access=javafx.graphics --module-path lib --add-modules javafx.controls -cp out Main 3

# 5. Ejecutar Demostración Rápida de Persistencia
java -Dfile.encoding=UTF-8 -cp "out;lib/*" Main 4
```

### Opción C: Desde IntelliJ IDEA / NetBeans / Eclipse
1. Abrir la carpeta del proyecto en el IDE.
2. La carpeta `lib/` ya se encuentra configurada en el archivo `SERIALIZACION.iml`.
3. Ejecutar la clase `Main.java` o `GestorCopaMundial.java`.

---

## 📄 Texto de Presentación para Entregar

> Este proyecto es un sistema de administración completo para toda la Copa Mundial de Fútbol, desde la fase de grupos, octavos, cuartos, semifinales hasta la gran final. Implementa serialización de objetos en Java para guardar de forma permanente cada selección nacional, sus jugadores y el resultado de cada partido en archivos `.dat`. Cuenta con CRUD completo, persistencia de datos, relación entre cada país y su plantilla, y el registro de cada encuentro del torneo. Se puede ejecutar de las tres formas que enseña el profesor: por consola, con interfaz gráfica Swing y con ventana JavaFX. Al cerrar y volver a ejecutar el programa, toda la tabla de posiciones y todos los resultados del mundial se cargan automáticamente.
