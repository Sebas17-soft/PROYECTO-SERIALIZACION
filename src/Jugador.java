import java.io.Serializable;

/**
 * ============================================================================
 * PROYECTO: Sistema de Gestión de la Copa Mundial de Fútbol
 * CONCEPTO: Serialización y Persistencia de Objetos en Java
 * ============================================================================
 * Clase Jugador:
 * Representa a un futbolista convocado por su selección nacional.
 * Implementa la interfaz java.io.Serializable para poder convertirse
 * en un flujo de bytes y almacenarse en archivos de datos binarios (.dat).
 */
public class Jugador implements Serializable {
    // serialVersionUID garantiza la compatibilidad durante la deserialización
    private static final long serialVersionUID = 1L;

    // Atributos privados (Encapsulamiento)
    private String idJugador;          // Identificador único (ej: J001)
    private String nombre;             // Nombre completo del jugador
    private String posicion;           // Arquero, Defensa, Mediocampista, Delantero
    private int goles;                 // Cantidad de goles marcados en la Copa
    private int tarjetasAmarillas;     // Cantidad de tarjetas amarillas acumuladas

    /**
     * Constructor con parámetros para instanciar un nuevo jugador.
     */
    public Jugador(String idJugador, String nombre, String posicion, int goles, int tarjetasAmarillas) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.posicion = posicion;
        this.goles = goles;
        this.tarjetasAmarillas = tarjetasAmarillas;
    }

    /**
     * Constructor por defecto.
     */
    public Jugador() {
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public String getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(String idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = Math.max(0, goles);
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public void setTarjetasAmarillas(int tarjetasAmarillas) {
        this.tarjetasAmarillas = Math.max(0, tarjetasAmarillas);
    }

    /**
     * Representación textual formateada del jugador.
     */
    @Override
    public String toString() {
        return String.format("  Jugador [ID: %-5s | Nombre: %-20s | Posición: %-13s | Goles: %2d | Amarillas: %d]",
                idJugador, nombre, posicion, goles, tarjetasAmarillas);
    }
}
