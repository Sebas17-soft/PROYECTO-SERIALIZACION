import java.io.Serializable;

/**
 * ============================================================================
 * PROYECTO: Sistema de Gestión de la Copa Mundial de Fútbol
 * CONCEPTO: Serialización y Persistencia de Objetos en Java
 * ============================================================================
 * Clase Partido:
 * Representa un encuentro deportivo disputado en cualquiera de las fases
 * de la Copa Mundial (Fase de Grupos, Octavos, Cuartos, Semifinal, Final).
 * Implementa Serializable para persistir los resultados en archivos .dat.
 */
public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;

    // Atributos privados
    private String idPartido;    // Identificador único del encuentro (ej: P001)
    private String fase;         // Fase: Fase de Grupos, Octavos de Final, Cuartos de Final, Semifinal, Final
    private String sel1;         // Nombre de la Selección 1
    private String sel2;         // Nombre de la Selección 2
    private int golesSel1;       // Goles anotados por la Selección 1
    private int golesSel2;       // Goles anotados por la Selección 2

    /**
     * Constructor con todos los campos del partido.
     */
    public Partido(String idPartido, String fase, String sel1, String sel2, int golesSel1, int golesSel2) {
        this.idPartido = idPartido;
        this.fase = fase;
        this.sel1 = sel1;
        this.sel2 = sel2;
        this.golesSel1 = golesSel1;
        this.golesSel2 = golesSel2;
    }

    /**
     * Constructor por defecto.
     */
    public Partido() {
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getSel1() {
        return sel1;
    }

    public void setSel1(String sel1) {
        this.sel1 = sel1;
    }

    public String getSel2() {
        return sel2;
    }

    public void setSel2(String sel2) {
        this.sel2 = sel2;
    }

    public int getGolesSel1() {
        return golesSel1;
    }

    public void setGolesSel1(int golesSel1) {
        this.golesSel1 = Math.max(0, golesSel1);
    }

    public int getGolesSel2() {
        return golesSel2;
    }

    public void setGolesSel2(int golesSel2) {
        this.golesSel2 = Math.max(0, golesSel2);
    }

    /**
     * Obtiene el resultado o ganador del partido.
     */
    public String getGanador() {
        if (golesSel1 > golesSel2) {
            return sel1;
        } else if (golesSel2 > golesSel1) {
            return sel2;
        } else {
            return "Empate";
        }
    }

    @Override
    public String toString() {
        return String.format("Partido [ID: %-5s | Fase: %-18s] %-15s %d - %d %-15s (Ganador: %s)",
                idPartido, fase, sel1, golesSel1, golesSel2, sel2, getGanador());
    }
}
