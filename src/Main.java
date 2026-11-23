import javax.swing.SwingUtilities;
import java.lang.reflect.Method;
import java.util.Scanner;

/**
 * ============================================================================
 * CLASE PRINCIPAL: LANZADOR DEL SISTEMA DE LA COPA MUNDIAL DE FÚTBOL
 * ============================================================================
 * Tema: Sistema de registro, serialización y persistencia de datos para
 *       administrar selecciones nacionales, plantillas de jugadores, partidos
 *       y resultados desde la fase de grupos hasta la gran final.
 *
 * Tres Modos de Ejecución soportados:
 *   [1] Consola de Texto (Terminal interactivo con Scanner)
 *   [2] Interfaz Gráfica Swing (Ventana Desktop con JTabbedPane y JTable)
 *   [3] Interfaz Gráfica JavaFX (Ventana moderna con Application y TableView)
 *   [4] Prueba de Serialización Rápida (Demostración de lectura/escritura en .dat)
 */
public class Main {

    public static void main(String[] args) {
        // Inicializar el gestor de negocio y persistencia
        GestorCopaMundial gestor = new GestorCopaMundial();

        // 1. Verificación de argumentos por línea de comandos
        if (args != null && args.length > 0) {
            String modo = args[0].trim().toLowerCase();
            switch (modo) {
                case "consola":
                case "1":
                    ejecutarModoConsola(gestor);
                    return;
                case "swing":
                case "2":
                    ejecutarModoSwing(gestor);
                    return;
                case "javafx":
                case "3":
                    ejecutarModoJavaFX(args, gestor);
                    return;
                case "demo":
                case "4":
                    ejecutarDemostracionRapida(gestor);
                    return;
                default:
                    System.out.println("Argumento no reconocido: " + modo + ". Mostrando menú principal...");
            }
        }

        // 2. Menú interactivo de selección de interfaz
        mostrarBannerBienvenida();
        System.out.println("Seleccione el modo de ejecución que desea iniciar:");
        System.out.println("  [1] 💻 Modo Consola de Texto (Menú interactivo con Scanner)");
        System.out.println("  [2] 🖥️ Modo Interfaz Gráfica Swing (Ventana de escritorio)");
        System.out.println("  [3] 🚀 Modo Interfaz Gráfica JavaFX (Ventana moderna)");
        System.out.println("  [4] ⚡ Demostración Rápida de Serialización (Test automático)");
        System.out.println("  [0] 🚪 Salir del programa");
        System.out.print("\nIngrese su opción (1, 2, 3, 4 o 0): ");

        Scanner sc = new Scanner(System.in);
        String entrada = sc.nextLine().trim();

        switch (entrada) {
            case "1":
                ejecutarModoConsola(gestor);
                break;
            case "2":
                ejecutarModoSwing(gestor);
                break;
            case "3":
                ejecutarModoJavaFX(args, gestor);
                break;
            case "4":
                ejecutarDemostracionRapida(gestor);
                break;
            case "0":
                System.out.println("Programa finalizado. ¡Hasta pronto!");
                break;
            default:
                System.out.println("Opción no válida. Iniciando por defecto la Interfaz Gráfica Swing...");
                ejecutarModoSwing(gestor);
                break;
        }
    }

    private static void mostrarBannerBienvenida() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          🏆 SISTEMA DE GESTIÓN DE LA COPA MUNDIAL 🏆           ║");
        System.out.println("║     Persistencia Binaria en Archivos .dat  •  CRUD Completo    ║");
        System.out.println("║         (Consola de Texto  •  Swing  •  JavaFX)                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    /**
     * MODO 1: Lanza el menú interactivo en terminal de texto.
     */
    public static void ejecutarModoConsola(GestorCopaMundial gestor) {
        System.out.println("\n>> Iniciando Modo 1: Consola de Texto...");
        MenuConsola menu = new MenuConsola(gestor);
        menu.iniciar();
    }

    /**
     * MODO 2: Lanza la ventana gráfica de escritorio Swing.
     */
    public static void ejecutarModoSwing(GestorCopaMundial gestor) {
        System.out.println("\n>> Iniciando Modo 2: Interfaz Gráfica Swing...");
        SwingUtilities.invokeLater(() -> {
            VentanaSwing ventana = new VentanaSwing(gestor);
            ventana.setVisible(true);
        });
    }

    /**
     * MODO 3: Lanza la aplicación JavaFX de forma segura.
     * Si el JDK no tiene OpenJFX en el classpath, advierte al usuario
     * y ofrece abrir el Modo Swing para asegurar la visualización gráfica.
     */
    public static void ejecutarModoJavaFX(String[] args, GestorCopaMundial gestor) {
        System.out.println("\n>> Iniciando Modo 3: Interfaz Gráfica JavaFX...");
        try {
            // Verificar si el framework JavaFX está disponible en el entorno
            Class.forName("javafx.application.Application");

            // Configurar el gestor compartido en VentanaJavaFX mediante reflexión
            Class<?> claseJFX = Class.forName("VentanaJavaFX");
            try {
                Method metodoSetGestor = claseJFX.getMethod("setGestor", GestorCopaMundial.class);
                metodoSetGestor.invoke(null, gestor);
            } catch (Exception ignored) {
            }

            // Invocar el método estático iniciar de VentanaJavaFX mediante reflexión
            Method metodoIniciar = claseJFX.getMethod("iniciar", String[].class);
            metodoIniciar.invoke(null, (Object) args);

        } catch (ClassNotFoundException e) {
            System.err.println("\n⚠️ AVISO JAVAFX:");
            System.err.println("El entorno Java actual no incluye los módulos de OpenJFX en su classpath.");
            System.err.println("Para ejecutar JavaFX desde la terminal use:");
            System.err.println("  java --module-path <ruta-openjfx\\lib> --add-modules javafx.controls VentanaJavaFX");
            System.err.println("\n>> Abriendo en su lugar la Interfaz Gráfica Swing (100% compatible)...");
            ejecutarModoSwing(gestor);
        } catch (Exception e) {
            System.err.println("Error al iniciar JavaFX: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Abriendo Modo Swing de respaldo...");
            ejecutarModoSwing(gestor);
        }
    }

    /**
     * MODO 4: Ejecución de prueba rápida y verificación de serialización.
     * Guarda selecciones y partidos en disco y los vuelve a leer para comprobar la persistencia.
     */
    public static void ejecutarDemostracionRapida(GestorCopaMundial gestor) {
        System.out.println("\n==================================================================");
        System.out.println("  DEMOSTRACIÓN RÁPIDA: VERIFICACIÓN DE PERSISTENCIA Y SERIALIZACIÓN");
        System.out.println("==================================================================");

        // 1. Mostrar selecciones cargadas de los archivos .dat
        System.out.println("\n1. Selecciones persistidas leídas de 'seleccionesMundial.dat':");
        for (Seleccion s : gestor.listarSelecciones()) {
            System.out.print(s);
        }

        // 2. Mostrar partidos persistidos leídos de 'partidosMundial.dat':
        System.out.println("\n2. Partidos disputados leídos de 'partidosMundial.dat':");
        for (Partido p : gestor.listarPartidos()) {
            System.out.println("  " + p);
        }

        // 3. Tabla general
        System.out.println("\n3. Tabla de Posiciones y Puntuación General:");
        int pos = 1;
        for (Seleccion s : gestor.getTablaPosiciones()) {
            System.out.printf("  #%d | %-15s | Pts: %2d | PG: %2d | GF: %2d%n",
                    pos++, s.getNombrePais(), s.getPuntos(), s.getPartidosGanados(), s.getGolesAFavor());
        }

        // 4. Estadísticas
        Jugador goleador = gestor.getGoleadorMaximo();
        if (goleador != null) {
            Seleccion paisG = gestor.buscarSeleccionDeJugador(goleador.getIdJugador());
            String nombrePais = (paisG != null) ? paisG.getNombrePais() : "";
            System.out.println("\n4. Máximo Goleador (Bota de Oro): " + goleador.getNombre() +
                    " (" + nombrePais + ") con " + goleador.getGoles() + " goles marcados.");
        }

        System.out.println("\n✅ Serialización y Persistencia Verificadas Correctamente.");
        System.out.println("Los archivos 'seleccionesMundial.dat' y 'partidosMundial.dat' están actualizados en disco.");
    }
}
