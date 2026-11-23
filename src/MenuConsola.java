import java.util.List;
import java.util.Scanner;

/**
 * ============================================================================
 * MODO 1: INTERFAZ EN CONSOLA DE TEXTO (Terminal Interactivo)
 * ============================================================================
 * Permite gestionar la Copa Mundial mediante comandos y menús de texto
 * en la consola estándar utilizando la clase Scanner.
 * Implementa todas las operaciones CRUD para:
 *   1. Selecciones Nacionales
 *   2. Plantillas de Jugadores
 *   3. Partidos y Resultados en todas las Fases del Mundial
 *   4. Visualización de la Tabla de Posiciones y Estadísticas
 *   5. Verificación de Persistencia y Serialización en archivos .dat
 */
public class MenuConsola {

    private final GestorCopaMundial gestor;
    private final Scanner scanner;

    public MenuConsola(GestorCopaMundial gestor) {
        this.gestor = gestor;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia el bucle principal del menú de consola.
     */
    public void iniciar() {
        int opcion = -1;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> menuSelecciones();
                case 2 -> menuJugadores();
                case 3 -> menuPartidos();
                case 4 -> mostrarTablaPosiciones();
                case 5 -> menuPersistencia();
                case 6 -> recargarDatosEjemplo();
                case 0 -> System.out.println("\n⚽ ¡Gracias por usar el Gestor de la Copa Mundial! Hasta pronto.");
                default -> System.out.println("⚠️ Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    // =========================================================================
    // MENÚ PRINCIPAL
    // =========================================================================

    private void mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🏆 SISTEMA DE GESTIÓN DE LA COPA MUNDIAL 🏆           ║");
        System.out.println("║                (Modo 1: Consola de Texto)                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. 🌍 Gestión de Selecciones Nacionales (CRUD)                ║");
        System.out.println("║  2. ⚽ Gestión de Plantilla de Jugadores (CRUD)                ║");
        System.out.println("║  3. 🏟️  Gestión de Partidos y Resultados (Todas las Fases)     ║");
        System.out.println("║  4. 📊 Tabla de Posiciones y Estadísticas del Mundial          ║");
        System.out.println("║  5. 💾 Persistencia: Guardar / Recargar Archivos .dat          ║");
        System.out.println("║  6. ⭐ Cargar Datos Demostrativos de la Copa Mundial           ║");
        System.out.println("║  0. 🚪 Salir                                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    // =========================================================================
    // SUBMENÚ: SELECCIONES (CRUD)
    // =========================================================================

    private void menuSelecciones() {
        int op = -1;
        do {
            System.out.println("\n--- [1] GESTIÓN DE SELECCIONES NACIONALES ---");
            System.out.println("1. Listar todas las selecciones");
            System.out.println("2. Registrar nueva selección (Create)");
            System.out.println("3. Buscar selección por ID o País (Read)");
            System.out.println("4. Actualizar estadísticas de una selección (Update)");
            System.out.println("5. Eliminar selección (Delete)");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Opción: ");

            switch (op) {
                case 1 -> listarSelecciones();
                case 2 -> registrarSeleccion();
                case 3 -> buscarSeleccion();
                case 4 -> actualizarSeleccion();
                case 5 -> eliminarSeleccion();
                case 0 -> {}
                default -> System.out.println("Opción no válida.");
            }
        } while (op != 0);
    }

    private void listarSelecciones() {
        List<Seleccion> selecciones = gestor.listarSelecciones();
        System.out.println("\n================= LISTA DE SELECCIONES NACIONALES =================");
        if (selecciones.isEmpty()) {
            System.out.println("No hay selecciones registradas.");
            return;
        }
        System.out.printf("%-6s | %-16s | %-6s | %-6s | %-6s | %-10s%n",
                "ID", "PAÍS", "PUNTOS", "PG", "GF", "JUGADORES");
        System.out.println("-------------------------------------------------------------------");
        for (Seleccion s : selecciones) {
            System.out.printf("%-6s | %-16s | %-6d | %-6d | %-6d | %-10d%n",
                    s.getIdSeleccion(), s.getNombrePais(), s.getPuntos(),
                    s.getPartidosGanados(), s.getGolesAFavor(), s.getListaJugadores().size());
        }
    }

    private void registrarSeleccion() {
        System.out.println("\n--- REGISTRAR NUEVA SELECCIÓN ---");
        String id = leerTexto("ID de la Selección (ej: S007): ");
        if (gestor.buscarSeleccionPorId(id) != null) {
            System.out.println("❌ Error: Ya existe una selección con el ID " + id);
            return;
        }
        String pais = leerTexto("Nombre del País: ");
        int puntos = leerEntero("Puntos obtenidos: ");
        int pg = leerEntero("Partidos ganados: ");
        int gf = leerEntero("Goles a favor: ");

        Seleccion nueva = new Seleccion(id, pais, puntos, pg, gf);
        if (gestor.crearSeleccion(nueva)) {
            System.out.println("✅ Selección registrada y persistida en 'seleccionesMundial.dat'.");
        } else {
            System.out.println("❌ No se pudo registrar la selección.");
        }
    }

    private void buscarSeleccion() {
        String criterio = leerTexto("\nIngrese el ID o el Nombre del País a buscar: ");
        Seleccion sel = gestor.buscarSeleccionPorId(criterio);
        if (sel == null) {
            sel = gestor.buscarSeleccionPorPais(criterio);
        }
        if (sel != null) {
            System.out.println("\n✅ Selección encontrada:");
            System.out.println(sel);
        } else {
            System.out.println("❌ No se encontró ninguna selección con el criterio: " + criterio);
        }
    }

    private void actualizarSeleccion() {
        String id = leerTexto("\nIngrese el ID de la selección a actualizar: ");
        Seleccion sel = gestor.buscarSeleccionPorId(id);
        if (sel == null) {
            System.out.println("❌ Selección no encontrada.");
            return;
        }
        System.out.println("Selección actual: " + sel.getNombrePais() + " (Pts: " + sel.getPuntos() + ", PG: " + sel.getPartidosGanados() + ", GF: " + sel.getGolesAFavor() + ")");
        int nuevosPuntos = leerEntero("Nuevos puntos: ");
        int nuevasVictorias = leerEntero("Nuevos partidos ganados: ");
        int nuevosGoles = leerEntero("Nuevos goles a favor: ");

        if (gestor.actualizarSeleccion(id, nuevosPuntos, nuevasVictorias, nuevosGoles)) {
            System.out.println("✅ Estadísticas actualizadas y guardadas con éxito.");
        } else {
            System.out.println("❌ Error al actualizar.");
        }
    }

    private void eliminarSeleccion() {
        String id = leerTexto("\nIngrese el ID de la selección a eliminar: ");
        Seleccion sel = gestor.buscarSeleccionPorId(id);
        if (sel == null) {
            System.out.println("❌ Selección no encontrada.");
            return;
        }
        String confirm = leerTexto("¿Seguro que desea eliminar a " + sel.getNombrePais() + " y toda su plantilla? (S/N): ");
        if (confirm.equalsIgnoreCase("S")) {
            if (gestor.eliminarSeleccion(id)) {
                System.out.println("✅ Selección eliminada y archivo .dat actualizado.");
            } else {
                System.out.println("❌ Error al eliminar.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // =========================================================================
    // SUBMENÚ: JUGADORES (CRUD)
    // =========================================================================

    private void menuJugadores() {
        int op = -1;
        do {
            System.out.println("\n--- [2] GESTIÓN DE PLANTILLA DE JUGADORES ---");
            System.out.println("1. Listar jugadores de una selección");
            System.out.println("2. Listar todos los jugadores del torneo");
            System.out.println("3. Registrar nuevo jugador en selección (Create)");
            System.out.println("4. Buscar jugador por ID (Read)");
            System.out.println("5. Actualizar estadísticas de un jugador (Update)");
            System.out.println("6. Eliminar jugador de la plantilla (Delete)");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Opción: ");

            switch (op) {
                case 1 -> listarJugadoresPorSeleccion();
                case 2 -> listarTodosLosJugadores();
                case 3 -> registrarJugador();
                case 4 -> buscarJugador();
                case 5 -> actualizarJugador();
                case 6 -> eliminarJugador();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    private void listarJugadoresPorSeleccion() {
        String idSel = leerTexto("\nIngrese el ID o Nombre de la selección: ");
        Seleccion sel = gestor.buscarSeleccionPorId(idSel);
        if (sel == null) sel = gestor.buscarSeleccionPorPais(idSel);

        if (sel == null) {
            System.out.println("❌ Selección no encontrada.");
            return;
        }
        System.out.println("\nPlantilla de " + sel.getNombrePais() + " (Total: " + sel.getListaJugadores().size() + " convocados):");
        if (sel.getListaJugadores().isEmpty()) {
            System.out.println("  No tiene jugadores registrados actualmente.");
            return;
        }
        for (Jugador j : sel.getListaJugadores()) {
            System.out.println(j);
        }
    }

    private void listarTodosLosJugadores() {
        System.out.println("\n================ LISTA DE TODOS LOS JUGADORES DEL MUNDIAL ================");
        List<Seleccion> selecciones = gestor.listarSelecciones();
        int total = 0;
        for (Seleccion sel : selecciones) {
            for (Jugador j : sel.getListaJugadores()) {
                System.out.printf("[%s] %s%n", sel.getNombrePais(), j);
                total++;
            }
        }
        if (total == 0) {
            System.out.println("No hay jugadores registrados en el torneo.");
        } else {
            System.out.println("Total de jugadores convocados: " + total);
        }
    }

    private void registrarJugador() {
        System.out.println("\n--- REGISTRAR JUGADOR EN SELECCIÓN ---");
        String idSel = leerTexto("ID de la Selección a la que pertenece: ");
        Seleccion sel = gestor.buscarSeleccionPorId(idSel);
        if (sel == null) {
            System.out.println("❌ Selección con ID '" + idSel + "' no encontrada.");
            return;
        }

        String idJ = leerTexto("ID del Jugador (ej: J021): ");
        if (gestor.buscarJugadorPorId(idJ) != null) {
            System.out.println("❌ Error: Ya existe un jugador con el ID " + idJ);
            return;
        }
        String nombre = leerTexto("Nombre del jugador: ");
        System.out.println("Posiciones comunes: Arquero | Defensa | Mediocampista | Delantero");
        String posicion = leerTexto("Posición: ");
        int goles = leerEntero("Goles marcados: ");
        int amarillas = leerEntero("Tarjetas amarillas: ");

        Jugador nuevo = new Jugador(idJ, nombre, posicion, goles, amarillas);
        if (gestor.agregarJugadorASeleccion(idSel, nuevo)) {
            System.out.println("✅ Jugador agregado a " + sel.getNombrePais() + " y persistido.");
        } else {
            System.out.println("❌ No se pudo agregar el jugador.");
        }
    }

    private void buscarJugador() {
        String idJ = leerTexto("\nIngrese el ID del jugador a buscar: ");
        Jugador j = gestor.buscarJugadorPorId(idJ);
        if (j != null) {
            Seleccion sel = gestor.buscarSeleccionDeJugador(idJ);
            String pais = (sel != null) ? sel.getNombrePais() : "Desconocido";
            System.out.println("\n✅ Jugador encontrado:");
            System.out.println("  País: " + pais);
            System.out.println(j);
        } else {
            System.out.println("❌ Jugador no encontrado con ID: " + idJ);
        }
    }

    private void actualizarJugador() {
        String idJ = leerTexto("\nIngrese el ID del jugador a modificar: ");
        Jugador j = gestor.buscarJugadorPorId(idJ);
        if (j == null) {
            System.out.println("❌ Jugador no encontrado.");
            return;
        }
        System.out.println("Datos actuales: " + j);
        String nuevoNombre = leerTexto("Nuevo nombre (Enter para conservar '" + j.getNombre() + "'): ");
        if (nuevoNombre.trim().isEmpty()) nuevoNombre = j.getNombre();

        String nuevaPos = leerTexto("Nueva posición (Enter para conservar '" + j.getPosicion() + "'): ");
        if (nuevaPos.trim().isEmpty()) nuevaPos = j.getPosicion();

        int nuevosGoles = leerEntero("Nuevos goles marcados: ");
        int nuevasAmarillas = leerEntero("Nuevas tarjetas amarillas: ");

        if (gestor.actualizarJugador(idJ, nuevoNombre, nuevaPos, nuevosGoles, nuevasAmarillas)) {
            System.out.println("✅ Jugador actualizado y persistido.");
        } else {
            System.out.println("❌ Error al actualizar el jugador.");
        }
    }

    private void eliminarJugador() {
        String idJ = leerTexto("\nIngrese el ID del jugador a eliminar: ");
        Jugador j = gestor.buscarJugadorPorId(idJ);
        if (j == null) {
            System.out.println("❌ Jugador no encontrado.");
            return;
        }
        String confirm = leerTexto("¿Eliminar al jugador " + j.getNombre() + "? (S/N): ");
        if (confirm.equalsIgnoreCase("S")) {
            if (gestor.eliminarJugador(idJ)) {
                System.out.println("✅ Jugador eliminado de la plantilla y archivo actualizado.");
            } else {
                System.out.println("❌ Error al eliminar.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ: PARTIDOS (CRUD)
    // =========================================================================

    private void menuPartidos() {
        int op = -1;
        do {
            System.out.println("\n--- [3] GESTIÓN DE PARTIDOS Y RESULTADOS ---");
            System.out.println("1. Listar todos los partidos disputados");
            System.out.println("2. Filtrar partidos por Fase (Grupos, Octavos, Cuartos, Semis, Final)");
            System.out.println("3. Registrar nuevo partido (Create)");
            System.out.println("4. Actualizar marcador de un partido (Update)");
            System.out.println("5. Eliminar partido (Delete)");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Opción: ");

            switch (op) {
                case 1 -> listarPartidos(gestor.listarPartidos());
                case 2 -> filtrarPartidosPorFase();
                case 3 -> registrarPartido();
                case 4 -> actualizarMarcadorPartido();
                case 5 -> eliminarPartido();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    private void listarPartidos(List<Partido> partidos) {
        System.out.println("\n================= ENCUENTROS DE LA COPA MUNDIAL =================");
        if (partidos.isEmpty()) {
            System.out.println("No hay partidos registrados en esta consulta.");
            return;
        }
        for (Partido p : partidos) {
            System.out.println(p);
        }
    }

    private void filtrarPartidosPorFase() {
        System.out.println("\nFases disponibles: Fase de Grupos | Octavos de Final | Cuartos de Final | Semifinales | Tercer Puesto | FINAL DEL MUNDIAL");
        String fase = leerTexto("Ingrese la fase exacta a filtrar: ");
        List<Partido> filtrados = gestor.listarPartidosPorFase(fase);
        listarPartidos(filtrados);
    }

    private void registrarPartido() {
        System.out.println("\n--- REGISTRAR NUEVO PARTIDO ---");
        String idP = leerTexto("ID del Partido (ej: P017): ");
        if (gestor.buscarPartidoPorId(idP) != null) {
            System.out.println("❌ Ya existe un partido con el ID " + idP);
            return;
        }
        System.out.println("Fase (Fase de Grupos / Octavos / Cuartos / Semifinal / Final):");
        String fase = leerTexto("Fase: ");
        String sel1 = leerTexto("Nombre Selección 1: ");
        int g1 = leerEntero("Goles de " + sel1 + ": ");
        String sel2 = leerTexto("Nombre Selección 2: ");
        int g2 = leerEntero("Goles de " + sel2 + ": ");

        Partido nuevo = new Partido(idP, fase, sel1, sel2, g1, g2);
        if (gestor.registrarPartido(nuevo)) {
            System.out.println("✅ Partido registrado con éxito y guardado en 'partidosMundial.dat'.");
        } else {
            System.out.println("❌ No se pudo registrar el partido.");
        }
    }

    private void actualizarMarcadorPartido() {
        String idP = leerTexto("\nIngrese el ID del partido a actualizar: ");
        Partido p = gestor.buscarPartidoPorId(idP);
        if (p == null) {
            System.out.println("❌ Partido no encontrado.");
            return;
        }
        System.out.println("Partido actual: " + p);
        int g1 = leerEntero("Nuevos goles para " + p.getSel1() + ": ");
        int g2 = leerEntero("Nuevos goles para " + p.getSel2() + ": ");

        if (gestor.actualizarResultadoPartido(idP, g1, g2)) {
            System.out.println("✅ Marcador actualizado: " + p.getSel1() + " " + g1 + " - " + g2 + " " + p.getSel2());
        } else {
            System.out.println("❌ Error al actualizar el partido.");
        }
    }

    private void eliminarPartido() {
        String idP = leerTexto("\nIngrese el ID del partido a eliminar: ");
        Partido p = gestor.buscarPartidoPorId(idP);
        if (p == null) {
            System.out.println("❌ Partido no encontrado.");
            return;
        }
        String confirm = leerTexto("¿Eliminar el partido " + p.getIdPartido() + "? (S/N): ");
        if (confirm.equalsIgnoreCase("S")) {
            if (gestor.eliminarPartido(idP)) {
                System.out.println("✅ Partido eliminado y archivo .dat actualizado.");
            } else {
                System.out.println("❌ Error al eliminar.");
            }
        }
    }

    // =========================================================================
    // 4. TABLA DE POSICIONES Y ESTADÍSTICAS
    // =========================================================================

    private void mostrarTablaPosiciones() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║            🏆 TABLA DE POSICIONES DE LA COPA MUNDIAL 🏆           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
        List<Seleccion> tabla = gestor.getTablaPosiciones();
        if (tabla.isEmpty()) {
            System.out.println("No hay datos en la tabla de posiciones.");
            return;
        }

        System.out.printf("%-4s | %-16s | %-6s | %-6s | %-6s | %-10s%n",
                "POS", "PAÍS", "PUNTOS", "PG", "GF", "PLANTILLA");
        System.out.println("--------------------------------------------------------------------");
        int pos = 1;
        for (Seleccion s : tabla) {
            System.out.printf("%-4d | %-16s | %-6d | %-6d | %-6d | %-10d%n",
                    pos++, s.getNombrePais(), s.getPuntos(), s.getPartidosGanados(),
                    s.getGolesAFavor(), s.getListaJugadores().size());
        }

        System.out.println("\n--- 📈 ESTADÍSTICAS GENERALES DEL TORNEO ---");
        System.out.println("• Total de Selecciones participantes: " + gestor.getCantidadSelecciones());
        System.out.println("• Total de Jugadores convocados: " + gestor.getCantidadJugadores());
        System.out.println("• Total de Partidos disputados: " + gestor.getCantidadPartidos());

        Jugador goleador = gestor.getGoleadorMaximo();
        if (goleador != null) {
            Seleccion paisGoleador = gestor.buscarSeleccionDeJugador(goleador.getIdJugador());
            String nombrePais = (paisGoleador != null) ? paisGoleador.getNombrePais() : "";
            System.out.println("• ⚽ Bota de Oro (Goleador): " + goleador.getNombre() + " (" + nombrePais + ") con " + goleador.getGoles() + " goles");
        }
    }

    // =========================================================================
    // 5. MENÚ DE PERSISTENCIA Y SERIALIZACIÓN
    // =========================================================================

    private void menuPersistencia() {
        System.out.println("\n--- [5] VERIFICACIÓN DE PERSISTENCIA Y SERIALIZACIÓN (.DAT) ---");
        System.out.println("1. Guardar manualmente todo el estado a los archivos .dat");
        System.out.println("2. Recargar datos desde los archivos .dat del disco");
        System.out.println("0. Regresar");
        int op = leerEntero("Opción: ");

        if (op == 1) {
            gestor.guardarTodo();
            System.out.println("✅ Archivos 'seleccionesMundial.dat' y 'partidosMundial.dat' guardados exitosamente.");
        } else if (op == 2) {
            gestor.cargarSelecciones();
            gestor.cargarPartidos();
            System.out.println("✅ Datos recargados desde disco. Total selecciones: " + gestor.getCantidadSelecciones()
                    + " | Total partidos: " + gestor.getCantidadPartidos());
        }
    }

    private void recargarDatosEjemplo() {
        String confirm = leerTexto("\n¿Desea restaurar los datos de demostración del Mundial (Argentina, Francia, etc.)? (S/N): ");
        if (confirm.equalsIgnoreCase("S")) {
            gestor.cargarDatosInicialesEjemplo();
            System.out.println("✅ Datos de demostración restablecidos y persistidos en disco.");
        }
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE LECTURA ROBUSTA
    // =========================================================================

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Por favor ingrese un número entero válido.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
}
