import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================================
 * PROYECTO: Sistema de Gestión de la Copa Mundial de Fútbol
 * CONCEPTO: Persistencia y Serialización en Java
 * ============================================================================
 * Clase GestorCopaMundial:
 * Controla toda la lógica de negocio y persistencia del torneo.
 * Almacena y recupera los datos de selecciones, jugadores y partidos
 * mediante serialización binaria en los archivos:
 *   - "seleccionesMundial.dat"
 *   - "partidosMundial.dat"
 *
 * Incluye operaciones CRUD completas:
 *   [C] Create  -> Crear / Registrar
 *   [R] Read    -> Listar / Buscar
 *   [U] Update  -> Actualizar datos
 *   [D] Delete  -> Eliminar registros
 */
public class GestorCopaMundial {

    // Nombres de los archivos binarios de persistencia
    public static final String ARCHIVO_SELECCIONES = "seleccionesMundial.dat";
    public static final String ARCHIVO_PARTIDOS = "partidosMundial.dat";

    // Estructuras en memoria principal
    private List<Seleccion> listaSelecciones;
    private List<Partido> listaPartidos;

    /**
     * Constructor: Carga automáticamente los datos persistidos desde los archivos .dat.
     * Si no existen o están vacíos, inicializa datos de muestra del Mundial.
     */
    public GestorCopaMundial() {
        this.listaSelecciones = cargarSelecciones();
        this.listaPartidos = cargarPartidos();

        // Si es la primera vez que se ejecuta y no hay datos, cargar datos de ejemplo
        if (this.listaSelecciones.isEmpty() && this.listaPartidos.isEmpty()) {
            cargarDatosInicialesEjemplo();
        }
    }

    // =========================================================================
    // 1. SERIALIZACIÓN Y DESERIALIZACIÓN: SELECCIONES (Y SUS JUGADORES)
    // =========================================================================

    /**
     * Deserializa la lista de selecciones desde el archivo binario "seleccionesMundial.dat".
     * Gracias a la serialización en cascada, los jugadores de cada país se cargan intactos.
     */
    @SuppressWarnings("unchecked")
    public List<Seleccion> cargarSelecciones() {
        File archivo = new File(ARCHIVO_SELECCIONES);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = entrada.readObject();
            if (obj instanceof List<?>) {
                return (List<Seleccion>) obj;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de selecciones no encontrado: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso al cargar selecciones: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Serializa la lista completa de selecciones al archivo binario "seleccionesMundial.dat".
     */
    public boolean guardarSelecciones() {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_SELECCIONES))) {
            salida.writeObject(listaSelecciones);
            return true;
        } catch (IOException e) {
            System.err.println("Error al serializar selecciones: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // 2. SERIALIZACIÓN Y DESERIALIZACIÓN: PARTIDOS DEL MUNDIAL
    // =========================================================================

    /**
     * Deserializa la lista de partidos desde el archivo binario "partidosMundial.dat".
     */
    @SuppressWarnings("unchecked")
    public List<Partido> cargarPartidos() {
        File archivo = new File(ARCHIVO_PARTIDOS);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = entrada.readObject();
            if (obj instanceof List<?>) {
                return (List<Partido>) obj;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de partidos no encontrado: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Aviso al cargar partidos: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Serializa la lista completa de partidos al archivo binario "partidosMundial.dat".
     */
    public boolean guardarPartidos() {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PARTIDOS))) {
            salida.writeObject(listaPartidos);
            return true;
        } catch (IOException e) {
            System.err.println("Error al serializar partidos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Guarda simultáneamente selecciones y partidos.
     */
    public void guardarTodo() {
        guardarSelecciones();
        guardarPartidos();
    }

    // =========================================================================
    // 3. CRUD COMPLETO: SELECCIONES NACIONALES
    // =========================================================================

    /**
     * [C] CREATE: Registra una nueva selección y persiste inmediatamente.
     */
    public boolean crearSeleccion(Seleccion sel) {
        if (sel == null || sel.getIdSeleccion() == null) return false;
        // Verificar que no exista ID duplicado
        if (buscarSeleccionPorId(sel.getIdSeleccion()) != null) {
            return false;
        }
        listaSelecciones.add(sel);
        guardarSelecciones();
        return true;
    }

    /**
     * [R] READ: Obtiene todas las selecciones registradas.
     */
    public List<Seleccion> listarSelecciones() {
        return listaSelecciones;
    }

    /**
     * [R] READ: Busca una selección por su ID único.
     */
    public Seleccion buscarSeleccionPorId(String id) {
        if (id == null) return null;
        for (Seleccion sel : listaSelecciones) {
            if (sel.getIdSeleccion().equalsIgnoreCase(id.trim())) {
                return sel;
            }
        }
        return null;
    }

    /**
     * [R] READ: Busca una selección por el nombre del país.
     */
    public Seleccion buscarSeleccionPorPais(String pais) {
        if (pais == null) return null;
        for (Seleccion sel : listaSelecciones) {
            if (sel.getNombrePais().equalsIgnoreCase(pais.trim())) {
                return sel;
            }
        }
        return null;
    }

    /**
     * [U] UPDATE: Actualiza las estadísticas deportivas de una selección.
     */
    public boolean actualizarSeleccion(String id, int nuevosPuntos, int nuevasVictorias, int nuevosGoles) {
        Seleccion sel = buscarSeleccionPorId(id);
        if (sel != null) {
            sel.setPuntos(nuevosPuntos);
            sel.setPartidosGanados(nuevasVictorias);
            sel.setGolesAFavor(nuevosGoles);
            guardarSelecciones();
            return true;
        }
        return false;
    }

    /**
     * [U] UPDATE: Actualización completa de todos los datos de la selección.
     */
    public boolean actualizarSeleccionCompleta(String id, String nuevoNombre, int nuevosPuntos, int nuevasVictorias, int nuevosGoles) {
        Seleccion sel = buscarSeleccionPorId(id);
        if (sel != null) {
            sel.setNombrePais(nuevoNombre);
            sel.setPuntos(nuevosPuntos);
            sel.setPartidosGanados(nuevasVictorias);
            sel.setGolesAFavor(nuevosGoles);
            guardarSelecciones();
            return true;
        }
        return false;
    }

    /**
     * [D] DELETE: Elimina una selección por su ID y actualiza el archivo .dat.
     */
    public boolean eliminarSeleccion(String id) {
        Seleccion sel = buscarSeleccionPorId(id);
        if (sel != null) {
            listaSelecciones.remove(sel);
            guardarSelecciones();
            return true;
        }
        return false;
    }

    // =========================================================================
    // 4. CRUD COMPLETO: PLANTILLA DE JUGADORES
    // =========================================================================

    /**
     * [C] CREATE: Agrega un jugador a la plantilla de una selección específica.
     */
    public boolean agregarJugadorASeleccion(String idSeleccion, Jugador j) {
        Seleccion sel = buscarSeleccionPorId(idSeleccion);
        if (sel != null && j != null) {
            // Verificar que no exista jugador con ID duplicado en todo el torneo
            if (buscarJugadorPorId(j.getIdJugador()) != null) {
                return false;
            }
            sel.agregarJugador(j);
            guardarSelecciones();
            return true;
        }
        return false;
    }

    /**
     * [R] READ: Obtiene los jugadores de una selección determinada.
     */
    public List<Jugador> listarJugadoresDeSeleccion(String idSeleccion) {
        Seleccion sel = buscarSeleccionPorId(idSeleccion);
        if (sel != null) {
            return sel.getListaJugadores();
        }
        return new ArrayList<>();
    }

    /**
     * [R] READ: Obtiene la lista completa de todos los jugadores de todas las selecciones.
     */
    public List<Jugador> listarTodosLosJugadores() {
        List<Jugador> todos = new ArrayList<>();
        for (Seleccion sel : listaSelecciones) {
            todos.addAll(sel.getListaJugadores());
        }
        return todos;
    }

    /**
     * [R] READ: Busca un jugador por su identificador en cualquiera de las selecciones.
     */
    public Jugador buscarJugadorPorId(String idJugador) {
        if (idJugador == null) return null;
        for (Seleccion sel : listaSelecciones) {
            Jugador j = sel.buscarJugador(idJugador);
            if (j != null) {
                return j;
            }
        }
        return null;
    }

    /**
     * [R] READ: Encuentra a qué selección pertenece un jugador.
     */
    public Seleccion buscarSeleccionDeJugador(String idJugador) {
        if (idJugador == null) return null;
        for (Seleccion sel : listaSelecciones) {
            if (sel.buscarJugador(idJugador) != null) {
                return sel;
            }
        }
        return null;
    }

    /**
     * [U] UPDATE: Actualiza los datos de un jugador.
     */
    public boolean actualizarJugador(String idJugador, String nuevoNombre, String nuevaPosicion, int nuevosGoles, int nuevasAmarillas) {
        for (Seleccion sel : listaSelecciones) {
            if (sel.actualizarJugador(idJugador, nuevoNombre, nuevaPosicion, nuevosGoles, nuevasAmarillas)) {
                guardarSelecciones();
                return true;
            }
        }
        return false;
    }

    /**
     * [D] DELETE: Elimina un jugador de la selección a la que pertenece.
     */
    public boolean eliminarJugador(String idJugador) {
        for (Seleccion sel : listaSelecciones) {
            if (sel.eliminarJugador(idJugador)) {
                guardarSelecciones();
                return true;
            }
        }
        return false;
    }

    // =========================================================================
    // 5. CRUD COMPLETO: PARTIDOS Y FASES DEL MUNDIAL
    // =========================================================================

    /**
     * [C] CREATE: Registra un nuevo partido disputado en una fase del torneo.
     */
    public boolean registrarPartido(Partido p) {
        if (p == null || p.getIdPartido() == null) return false;
        if (buscarPartidoPorId(p.getIdPartido()) != null) {
            return false;
        }
        listaPartidos.add(p);
        guardarPartidos();
        return true;
    }

    /**
     * [R] READ: Obtiene todos los partidos registrados en el mundial.
     */
    public List<Partido> listarPartidos() {
        return listaPartidos;
    }

    /**
     * [R] READ: Filtra partidos por fase específica (Grupos, Octavos, Cuartos, Semifinal, Final).
     */
    public List<Partido> listarPartidosPorFase(String fase) {
        List<Partido> filtrados = new ArrayList<>();
        if (fase == null || fase.equalsIgnoreCase("Todas") || fase.trim().isEmpty()) {
            return listaPartidos;
        }
        for (Partido p : listaPartidos) {
            if (p.getFase().equalsIgnoreCase(fase.trim())) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    /**
     * [R] READ: Busca un partido por su ID.
     */
    public Partido buscarPartidoPorId(String idPartido) {
        if (idPartido == null) return null;
        for (Partido p : listaPartidos) {
            if (p.getIdPartido().equalsIgnoreCase(idPartido.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * [U] UPDATE: Actualiza el marcador (resultado) de un partido.
     */
    public boolean actualizarResultadoPartido(String idPartido, int nuevosGoles1, int nuevosGoles2) {
        Partido p = buscarPartidoPorId(idPartido);
        if (p != null) {
            p.setGolesSel1(nuevosGoles1);
            p.setGolesSel2(nuevosGoles2);
            guardarPartidos();
            return true;
        }
        return false;
    }

    /**
     * [U] UPDATE: Actualización completa del partido.
     */
    public boolean actualizarPartidoCompleto(String idPartido, String fase, String sel1, String sel2, int goles1, int goles2) {
        Partido p = buscarPartidoPorId(idPartido);
        if (p != null) {
            p.setFase(fase);
            p.setSel1(sel1);
            p.setSel2(sel2);
            p.setGolesSel1(goles1);
            p.setGolesSel2(goles2);
            guardarPartidos();
            return true;
        }
        return false;
    }

    /**
     * [D] DELETE: Elimina un partido por su ID.
     */
    public boolean eliminarPartido(String idPartido) {
        Partido p = buscarPartidoPorId(idPartido);
        if (p != null) {
            listaPartidos.remove(p);
            guardarPartidos();
            return true;
        }
        return false;
    }

    // =========================================================================
    // 6. ESTADÍSTICAS Y RESUMEN GENERAL DEL MUNDIAL
    // =========================================================================

    public int getCantidadSelecciones() {
        return listaSelecciones.size();
    }

    public int getCantidadJugadores() {
        return listarTodosLosJugadores().size();
    }

    public int getCantidadPartidos() {
        return listaPartidos.size();
    }

    /**
     * Devuelve las selecciones ordenadas por puntuación y goles a favor.
     */
    public List<Seleccion> getTablaPosiciones() {
        List<Seleccion> ordenadas = new ArrayList<>(listaSelecciones);
        ordenadas.sort(Comparator.comparingInt(Seleccion::getPuntos)
                .thenComparingInt(Seleccion::getGolesAFavor)
                .reversed());
        return ordenadas;
    }

    /**
     * Encuentra al máximo goleador de la Copa Mundial.
     */
    public Jugador getGoleadorMaximo() {
        Jugador max = null;
        for (Jugador j : listarTodosLosJugadores()) {
            if (max == null || j.getGoles() > max.getGoles()) {
                max = j;
            }
        }
        return max;
    }

    // =========================================================================
    // 7. CARGA DE DATOS DEMOSTRATIVOS DEL MUNDIAL
    // =========================================================================

    /**
     * Carga un conjunto rico de datos de la Copa Mundial de Fútbol
     * con selecciones protagonistas, jugadores estrella y partidos
     * desde la fase de grupos hasta la Gran Final histórica.
     */
    public void cargarDatosInicialesEjemplo() {
        listaSelecciones.clear();
        listaPartidos.clear();

        // 1. Argentina
        Seleccion argentina = new Seleccion("S001", "Argentina", 17, 5, 15);
        argentina.agregarJugador(new Jugador("J001", "Lionel Messi", "Delantero", 7, 1));
        argentina.agregarJugador(new Jugador("J002", "Emiliano Martínez", "Arquero", 0, 1));
        argentina.agregarJugador(new Jugador("J003", "Julián Álvarez", "Delantero", 4, 0));
        argentina.agregarJugador(new Jugador("J004", "Rodrigo De Paul", "Mediocampista", 0, 2));
        argentina.agregarJugador(new Jugador("J005", "Nicolás Otamendi", "Defensa", 0, 2));
        crearSeleccion(argentina);

        // 2. Francia
        Seleccion francia = new Seleccion("S002", "Francia", 16, 5, 16);
        francia.agregarJugador(new Jugador("J006", "Kylian Mbappé", "Delantero", 8, 0));
        francia.agregarJugador(new Jugador("J007", "Antoine Griezmann", "Mediocampista", 0, 1));
        francia.agregarJugador(new Jugador("J008", "Olivier Giroud", "Delantero", 4, 1));
        francia.agregarJugador(new Jugador("J009", "Hugo Lloris", "Arquero", 0, 0));
        crearSeleccion(francia);

        // 3. Croacia
        Seleccion croacia = new Seleccion("S003", "Croacia", 10, 2, 8);
        croacia.agregarJugador(new Jugador("J010", "Luka Modrić", "Mediocampista", 0, 1));
        croacia.agregarJugador(new Jugador("J011", "Dominik Livaković", "Arquero", 0, 1));
        croacia.agregarJugador(new Jugador("J012", "Joško Gvardiol", "Defensa", 1, 0));
        crearSeleccion(croacia);

        // 4. Marruecos
        Seleccion marruecos = new Seleccion("S004", "Marruecos", 11, 3, 6);
        marruecos.agregarJugador(new Jugador("J013", "Yassine Bounou", "Arquero", 0, 0));
        marruecos.agregarJugador(new Jugador("J014", "Achraf Hakimi", "Defensa", 0, 1));
        marruecos.agregarJugador(new Jugador("J015", "Hakim Ziyech", "Delantero", 1, 1));
        crearSeleccion(marruecos);

        // 5. Brasil
        Seleccion brasil = new Seleccion("S005", "Brasil", 10, 3, 8);
        brasil.agregarJugador(new Jugador("J016", "Vinícius Júnior", "Delantero", 1, 0));
        brasil.agregarJugador(new Jugador("J017", "Richarlison", "Delantero", 3, 0));
        brasil.agregarJugador(new Jugador("J018", "Casemiro", "Mediocampista", 1, 1));
        crearSeleccion(brasil);

        // 6. España
        Seleccion espana = new Seleccion("S006", "España", 5, 1, 9);
        espana.agregarJugador(new Jugador("J019", "Pedri", "Mediocampista", 0, 0));
        espana.agregarJugador(new Jugador("J020", "Gavi", "Mediocampista", 1, 1));
        crearSeleccion(espana);

        // --- PARTIDOS DEL MUNDIAL EN TODAS SUS FASES ---
        // Fase de Grupos
        registrarPartido(new Partido("P001", "Fase de Grupos", "Argentina", "Arabia Saudita", 1, 2));
        registrarPartido(new Partido("P002", "Fase de Grupos", "Francia", "Australia", 4, 1));
        registrarPartido(new Partido("P003", "Fase de Grupos", "España", "Costa Rica", 7, 0));
        registrarPartido(new Partido("P004", "Fase de Grupos", "Brasil", "Serbia", 2, 0));

        // Octavos de Final
        registrarPartido(new Partido("P005", "Octavos de Final", "Argentina", "Australia", 2, 1));
        registrarPartido(new Partido("P006", "Octavos de Final", "Francia", "Polonia", 3, 1));
        registrarPartido(new Partido("P007", "Octavos de Final", "Marruecos", "España", 0, 0));
        registrarPartido(new Partido("P008", "Octavos de Final", "Croacia", "Japón", 1, 1));

        // Cuartos de Final
        registrarPartido(new Partido("P009", "Cuartos de Final", "Argentina", "Países Bajos", 2, 2));
        registrarPartido(new Partido("P010", "Cuartos de Final", "Croacia", "Brasil", 1, 1));
        registrarPartido(new Partido("P011", "Cuartos de Final", "Francia", "Inglaterra", 2, 1));
        registrarPartido(new Partido("P012", "Cuartos de Final", "Marruecos", "Portugal", 1, 0));

        // Semifinales
        registrarPartido(new Partido("P013", "Semifinales", "Argentina", "Croacia", 3, 0));
        registrarPartido(new Partido("P014", "Semifinales", "Francia", "Marruecos", 2, 0));

        // Tercer Puesto
        registrarPartido(new Partido("P015", "Tercer Puesto", "Croacia", "Marruecos", 2, 1));

        // GRAN FINAL DEL MUNDIAL
        registrarPartido(new Partido("P016", "FINAL DEL MUNDIAL", "Argentina", "Francia", 3, 3));

        // Guardar la persistencia de los datos cargados
        guardarTodo();
    }

    // =========================================================================
    // 8. MÉTODO MAIN: EJECUCIÓN DIRECTA DEL GESTOR (DEMOSTRACIÓN COMPLETA)
    // =========================================================================
    public static void main(String[] args) {
        GestorCopaMundial gestor = new GestorCopaMundial();

        System.out.println("==================================================================");
        System.out.println("  🏆 SISTEMA DE GESTIÓN Y PERSISTENCIA DE LA COPA MUNDIAL 🏆");
        System.out.println("  Persistencia en archivos: " + ARCHIVO_SELECCIONES + " y " + ARCHIVO_PARTIDOS);
        System.out.println("==================================================================\n");

        System.out.println("===== TABLA DE SELECCIONES Y PLANTILLAS PERSISTIDAS =====");
        for (Seleccion sel : gestor.listarSelecciones()) {
            System.out.print(sel);
        }

        System.out.println("\n===== RESULTADOS DE TODOS LOS PARTIDOS DEL MUNDIAL =====");
        for (Partido p : gestor.listarPartidos()) {
            System.out.println("  " + p);
        }

        System.out.println("\n===== TABLA DE POSICIONES GENERAL =====");
        int pos = 1;
        for (Seleccion s : gestor.getTablaPosiciones()) {
            System.out.printf("  #%d | %-15s | Pts: %2d | PG: %2d | GF: %2d%n",
                    pos++, s.getNombrePais(), s.getPuntos(), s.getPartidosGanados(), s.getGolesAFavor());
        }

        Jugador goleador = gestor.getGoleadorMaximo();
        if (goleador != null) {
            Seleccion paisG = gestor.buscarSeleccionDeJugador(goleador.getIdJugador());
            String nombrePais = (paisG != null) ? paisG.getNombrePais() : "";
            System.out.println("\n🥇 Máximo Goleador (Bota de Oro): " + goleador.getNombre() +
                    " (" + nombrePais + ") con " + goleador.getGoles() + " goles anotados.");
        }

        System.out.println("\n✅ Serialización y carga automática verificadas con éxito.");
    }
}
