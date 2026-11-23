import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PROYECTO: Sistema de Gestión de la Copa Mundial de Fútbol
 * CONCEPTO: Serialización y Persistencia de Objetos en Java
 * ============================================================================
 * Clase Seleccion:
 * Representa a un país participante en la Copa Mundial de Fútbol.
 * Mantiene sus estadísticas generales de competición y contiene su
 * propia plantilla de jugadores (relación uno a muchos: 1 Selección -> N Jugadores).
 * Al ser Serializable, cuando una Seleccion se serializa, toda su lista
 * de jugadores se guarda automáticamente en cascada en el archivo .dat.
 */
public class Seleccion implements Serializable {
    private static final long serialVersionUID = 1L;

    // Atributos de la selección
    private String idSeleccion;           // Identificador único (ej: S001)
    private String nombrePais;            // Nombre de la nación
    private int puntos;                   // Puntos acumulados en la tabla
    private int partidosGanados;          // Victorias conseguidas
    private int golesAFavor;              // Goles a favor anotados
    private List<Jugador> listaJugadores; // Plantilla de jugadores convocados

    /**
     * Constructor completo para registrar una selección.
     */
    public Seleccion(String idSeleccion, String nombrePais, int puntos, int partidosGanados, int golesAFavor) {
        this.idSeleccion = idSeleccion;
        this.nombrePais = nombrePais;
        this.puntos = puntos;
        this.partidosGanados = partidosGanados;
        this.golesAFavor = golesAFavor;
        this.listaJugadores = new ArrayList<>();
    }

    /**
     * Constructor por defecto inicializando la lista de jugadores.
     */
    public Seleccion() {
        this.listaJugadores = new ArrayList<>();
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public String getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(String idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = Math.max(0, puntos);
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = Math.max(0, partidosGanados);
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(int golesAFavor) {
        this.golesAFavor = Math.max(0, golesAFavor);
    }

    public List<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void setListaJugadores(List<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    // ==========================================
    // GESTIÓN DE PLANTILLA DE JUGADORES
    // ==========================================

    /**
     * Agrega un nuevo jugador a la plantilla de la selección.
     */
    public void agregarJugador(Jugador j) {
        if (j != null) {
            this.listaJugadores.add(j);
        }
    }

    /**
     * Busca un jugador por su identificador en esta selección.
     */
    public Jugador buscarJugador(String idJugador) {
        for (Jugador j : listaJugadores) {
            if (j.getIdJugador().equalsIgnoreCase(idJugador)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Elimina un jugador de la plantilla por su identificador.
     */
    public boolean eliminarJugador(String idJugador) {
        return listaJugadores.removeIf(j -> j.getIdJugador().equalsIgnoreCase(idJugador));
    }

    /**
     * Actualiza los datos estadísticos de un jugador en la plantilla.
     */
    public boolean actualizarJugador(String idJugador, String nombre, String posicion, int goles, int amarillas) {
        Jugador j = buscarJugador(idJugador);
        if (j != null) {
            j.setNombre(nombre);
            j.setPosicion(posicion);
            j.setGoles(goles);
            j.setTarjetasAmarillas(amarillas);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append(String.format("Selección [ID: %-5s | País: %-15s | Pts: %2d | PG: %2d | GF: %2d | Jugadores: %d]\n",
                idSeleccion, nombrePais, puntos, partidosGanados, golesAFavor, listaJugadores.size()));
        for (Jugador j : listaJugadores) {
            texto.append(j).append("\n");
        }
        return texto.toString();
    }
}
