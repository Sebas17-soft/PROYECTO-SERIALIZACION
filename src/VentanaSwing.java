import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * MODO 2: INTERFAZ GRÁFICA SWING (Ventana Desktop)
 * ============================================================================
 * Ventana gráfica completa desarrollada con la biblioteca estándar javax.swing.
 * Proporciona una experiencia visual intuitiva para administrar todas las
 * selecciones, jugadores y partidos del Mundial, guardando y cargando
 * automáticamente la información en archivos binarios serializados (.dat).
 */
public class VentanaSwing extends JFrame {

    private final GestorCopaMundial gestor;

    // Modelos de tabla
    private DefaultTableModel modeloSelecciones;
    private DefaultTableModel modeloJugadores;
    private DefaultTableModel modeloPartidos;
    private DefaultTableModel modeloPosiciones;

    // Componentes de formulario - Selecciones
    private JTextField txtSelId;
    private JTextField txtSelPais;
    private JTextField txtSelPuntos;
    private JTextField txtSelPG;
    private JTextField txtSelGF;
    private JTable tablaSelecciones;

    // Componentes de formulario - Jugadores
    private JComboBox<String> cbJugadorSeleccion;
    private JComboBox<String> cbFiltroJugadores;
    private JTextField txtJugId;
    private JTextField txtJugNombre;
    private JComboBox<String> cbJugPosicion;
    private JTextField txtJugGoles;
    private JTextField txtJugAmarillas;
    private JTable tablaJugadores;

    // Componentes de formulario - Partidos
    private JComboBox<String> cbFiltroFasePartidos;
    private JTextField txtParId;
    private JComboBox<String> cbParFase;
    private JTextField txtParSel1;
    private JTextField txtParSel2;
    private JSpinner spGoles1;
    private JSpinner spGoles2;
    private JTable tablaPartidos;

    // Componentes de resumen y estadísticas
    private JLabel lblTotalSelecciones;
    private JLabel lblTotalJugadores;
    private JLabel lblTotalPartidos;
    private JLabel lblMaxGoleador;
    private JTable tablaPosiciones;

    public VentanaSwing(GestorCopaMundial gestor) {
        this.gestor = gestor;
        configurarVentana();
        inicializarComponentes();
        refrescarTodo();
    }

    private void configurarVentana() {
        setTitle("🏆 Copa Mundial de Fútbol - Sistema de Gestión y Persistencia (.dat)");
        setSize(1080, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // 1. BANNER SUPERIOR CON ESTILO DEL MUNDIAL
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(15, 32, 67)); // Azul FIFA profundo
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("⚽ SISTEMA DE GESTIÓN DE LA COPA MUNDIAL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 215, 0)); // Dorado

        JLabel lblSubtitulo = new JLabel("Persistencia Binaria en Archivos .dat  •  Operaciones CRUD Completas  •  Consola, Swing & JavaFX");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(220, 230, 245));

        pnlHeader.add(lblTitulo, BorderLayout.NORTH);
        pnlHeader.add(lblSubtitulo, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. CONTENEDOR CON PESTAÑAS (JTabbedPane)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.addTab("🌍 Selecciones Nacionales", crearPanelSelecciones());
        tabbedPane.addTab("⚽ Plantilla de Jugadores", crearPanelJugadores());
        tabbedPane.addTab("🏟️ Partidos y Resultados", crearPanelPartidos());
        tabbedPane.addTab("📊 Posiciones y Estadísticas", crearPanelPosiciones());

        add(tabbedPane, BorderLayout.CENTER);

        // 3. BARRA INFERIOR DE ESTADO
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        pnlFooter.setBackground(new Color(240, 244, 250));
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 220, 235)));

        JLabel lblInfoDat = new JLabel("💾 Archivos de persistencia: 'seleccionesMundial.dat' y 'partidosMundial.dat' activos");
        lblInfoDat.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfoDat.setForeground(new Color(80, 90, 105));
        pnlFooter.add(lblInfoDat);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    // =========================================================================
    // PESTAÑA 1: CRUD SELECCIONES
    // =========================================================================

    private JPanel crearPanelSelecciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // TABLA DE SELECCIONES
        String[] columnas = {"ID", "País", "Puntos", "Partidos Ganados", "Goles a Favor", "Jugadores"};
        modeloSelecciones = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSelecciones = new JTable(modeloSelecciones);
        tablaSelecciones.setRowHeight(26);
        tablaSelecciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaSelecciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        alinearCentro(tablaSelecciones, new int[]{0, 2, 3, 4, 5});

        tablaSelecciones.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaSelecciones.getSelectedRow();
            if (fila >= 0) {
                txtSelId.setText(modeloSelecciones.getValueAt(fila, 0).toString());
                txtSelPais.setText(modeloSelecciones.getValueAt(fila, 1).toString());
                txtSelPuntos.setText(modeloSelecciones.getValueAt(fila, 2).toString());
                txtSelPG.setText(modeloSelecciones.getValueAt(fila, 3).toString());
                txtSelGF.setText(modeloSelecciones.getValueAt(fila, 4).toString());
            }
        });

        JScrollPane scroll = new JScrollPane(tablaSelecciones);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Selecciones Participantes"));
        panel.add(scroll, BorderLayout.CENTER);

        // FORMULARIO CRUD
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Formulario de Selección (CRUD)"),
                new EmptyBorder(10, 10, 10, 10)));
        pnlForm.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtSelId = new JTextField(10);
        txtSelPais = new JTextField(15);
        txtSelPuntos = new JTextField("0", 6);
        txtSelPG = new JTextField("0", 6);
        txtSelGF = new JTextField("0", 6);

        agregarFilaFormulario(pnlForm, g, 0, "ID Selección:", txtSelId);
        agregarFilaFormulario(pnlForm, g, 1, "Nombre del País:", txtSelPais);
        agregarFilaFormulario(pnlForm, g, 2, "Puntos:", txtSelPuntos);
        agregarFilaFormulario(pnlForm, g, 3, "Partidos Ganados:", txtSelPG);
        agregarFilaFormulario(pnlForm, g, 4, "Goles a Favor:", txtSelGF);

        // BOTONES CRUD
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnActualizar = new JButton("✏️ Actualizar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnLimpiar = new JButton("🧹 Limpiar");

        btnAgregar.setBackground(new Color(40, 167, 69));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(23, 162, 184));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        btnAgregar.addActionListener(e -> accionAgregarSeleccion());
        btnActualizar.addActionListener(e -> accionActualizarSeleccion());
        btnEliminar.addActionListener(e -> accionEliminarSeleccion());
        btnLimpiar.addActionListener(e -> limpiarFormSeleccion());

        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        g.gridx = 0;
        g.gridy = 5;
        g.gridwidth = 2;
        pnlForm.add(pnlBotones, g);

        panel.add(pnlForm, BorderLayout.EAST);
        return panel;
    }

    private void accionAgregarSeleccion() {
        String id = txtSelId.getText().trim();
        String pais = txtSelPais.getText().trim();
        if (id.isEmpty() || pais.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el ID y el País de la Selección.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int pts = Integer.parseInt(txtSelPuntos.getText().trim());
            int pg = Integer.parseInt(txtSelPG.getText().trim());
            int gf = Integer.parseInt(txtSelGF.getText().trim());

            Seleccion nueva = new Seleccion(id, pais, pts, pg, gf);
            if (gestor.crearSeleccion(nueva)) {
                JOptionPane.showMessageDialog(this, "Selección guardada y persistida en disco.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
                limpiarFormSeleccion();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una selección con el ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Puntos, PG y Goles deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizarSeleccion() {
        String id = txtSelId.getText().trim();
        String pais = txtSelPais.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione o ingrese el ID de la selección a actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int pts = Integer.parseInt(txtSelPuntos.getText().trim());
            int pg = Integer.parseInt(txtSelPG.getText().trim());
            int gf = Integer.parseInt(txtSelGF.getText().trim());

            if (gestor.actualizarSeleccionCompleta(id, pais, pts, pg, gf)) {
                JOptionPane.showMessageDialog(this, "Selección actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la selección con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los campos numéricos no son válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminarSeleccion() {
        String id = txtSelId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione la selección que desea eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar esta selección y su plantilla?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestor.eliminarSeleccion(id)) {
                JOptionPane.showMessageDialog(this, "Selección eliminada y archivo .dat actualizado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
                limpiarFormSeleccion();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la selección.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormSeleccion() {
        txtSelId.setText("");
        txtSelPais.setText("");
        txtSelPuntos.setText("0");
        txtSelPG.setText("0");
        txtSelGF.setText("0");
        tablaSelecciones.clearSelection();
    }

    // =========================================================================
    // PESTAÑA 2: CRUD JUGADORES
    // =========================================================================

    private JPanel crearPanelJugadores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // BARRA SUPERIOR DE FILTRO
        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFiltro.add(new JLabel("Filtrar por Selección:"));
        cbFiltroJugadores = new JComboBox<>();
        cbFiltroJugadores.setPreferredSize(new Dimension(200, 26));
        cbFiltroJugadores.addActionListener(e -> refrescarTablaJugadores());
        pnlFiltro.add(cbFiltroJugadores);
        panel.add(pnlFiltro, BorderLayout.NORTH);

        // TABLA DE JUGADORES
        String[] columnas = {"ID", "Nombre", "Posición", "Goles", "Amarillas", "Selección"};
        modeloJugadores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaJugadores = new JTable(modeloJugadores);
        tablaJugadores.setRowHeight(26);
        tablaJugadores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaJugadores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        alinearCentro(tablaJugadores, new int[]{0, 2, 3, 4});

        tablaJugadores.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaJugadores.getSelectedRow();
            if (fila >= 0) {
                txtJugId.setText(modeloJugadores.getValueAt(fila, 0).toString());
                txtJugNombre.setText(modeloJugadores.getValueAt(fila, 1).toString());
                cbJugPosicion.setSelectedItem(modeloJugadores.getValueAt(fila, 2).toString());
                txtJugGoles.setText(modeloJugadores.getValueAt(fila, 3).toString());
                txtJugAmarillas.setText(modeloJugadores.getValueAt(fila, 4).toString());
                String pais = modeloJugadores.getValueAt(fila, 5).toString();
                cbJugadorSeleccion.setSelectedItem(pais);
            }
        });

        JScrollPane scroll = new JScrollPane(tablaJugadores);
        scroll.setBorder(BorderFactory.createTitledBorder("Plantilla de Jugadores Convocados"));
        panel.add(scroll, BorderLayout.CENTER);

        // FORMULARIO CRUD JUGADORES
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Formulario de Jugador (CRUD)"),
                new EmptyBorder(10, 10, 10, 10)));
        pnlForm.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        cbJugadorSeleccion = new JComboBox<>();
        txtJugId = new JTextField(10);
        txtJugNombre = new JTextField(15);
        cbJugPosicion = new JComboBox<>(new String[]{"Arquero", "Defensa", "Mediocampista", "Delantero"});
        txtJugGoles = new JTextField("0", 6);
        txtJugAmarillas = new JTextField("0", 6);

        agregarFilaFormulario(pnlForm, g, 0, "Selección:", cbJugadorSeleccion);
        agregarFilaFormulario(pnlForm, g, 1, "ID Jugador:", txtJugId);
        agregarFilaFormulario(pnlForm, g, 2, "Nombre Completo:", txtJugNombre);
        agregarFilaFormulario(pnlForm, g, 3, "Posición en Campo:", cbJugPosicion);
        agregarFilaFormulario(pnlForm, g, 4, "Goles Anotados:", txtJugGoles);
        agregarFilaFormulario(pnlForm, g, 5, "Tarjetas Amarillas:", txtJugAmarillas);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        JButton btnAgregar = new JButton("➕ Registrar");
        JButton btnActualizar = new JButton("✏️ Actualizar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnLimpiar = new JButton("🧹 Limpiar");

        btnAgregar.setBackground(new Color(40, 167, 69));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(23, 162, 184));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        btnAgregar.addActionListener(e -> accionAgregarJugador());
        btnActualizar.addActionListener(e -> accionActualizarJugador());
        btnEliminar.addActionListener(e -> accionEliminarJugador());
        btnLimpiar.addActionListener(e -> limpiarFormJugador());

        pnlBotones.add(btnAgregar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        pnlForm.add(pnlBotones, g);

        panel.add(pnlForm, BorderLayout.EAST);
        return panel;
    }

    private void accionAgregarJugador() {
        String id = txtJugId.getText().trim();
        String nombre = txtJugNombre.getText().trim();
        String pos = (String) cbJugPosicion.getSelectedItem();
        String paisSel = (String) cbJugadorSeleccion.getSelectedItem();

        if (id.isEmpty() || nombre.isEmpty() || paisSel == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos del jugador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Seleccion sel = gestor.buscarSeleccionPorPais(paisSel);
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Selección destino no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int goles = Integer.parseInt(txtJugGoles.getText().trim());
            int amarillas = Integer.parseInt(txtJugAmarillas.getText().trim());

            Jugador j = new Jugador(id, nombre, pos, goles, amarillas);
            if (gestor.agregarJugadorASeleccion(sel.getIdSeleccion(), j)) {
                JOptionPane.showMessageDialog(this, "Jugador incorporado a " + sel.getNombrePais() + " y persistido.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
                limpiarFormJugador();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un jugador con el ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Goles y tarjetas deben ser enteros.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizarJugador() {
        String id = txtJugId.getText().trim();
        String nombre = txtJugNombre.getText().trim();
        String pos = (String) cbJugPosicion.getSelectedItem();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un jugador para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int goles = Integer.parseInt(txtJugGoles.getText().trim());
            int amarillas = Integer.parseInt(txtJugAmarillas.getText().trim());

            if (gestor.actualizarJugador(id, nombre, pos, goles, amarillas)) {
                JOptionPane.showMessageDialog(this, "Jugador actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el jugador con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Goles y tarjetas deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminarJugador() {
        String id = txtJugId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un jugador para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este jugador de la plantilla?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestor.eliminarJugador(id)) {
                JOptionPane.showMessageDialog(this, "Jugador eliminado y archivo .dat actualizado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
                limpiarFormJugador();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormJugador() {
        txtJugId.setText("");
        txtJugNombre.setText("");
        txtJugGoles.setText("0");
        txtJugAmarillas.setText("0");
        tablaJugadores.clearSelection();
    }

    // =========================================================================
    // PESTAÑA 3: CRUD PARTIDOS
    // =========================================================================

    private JPanel crearPanelPartidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // FILTRO DE FASE
        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFiltro.add(new JLabel("Filtrar por Fase del Mundial:"));
        cbFiltroFasePartidos = new JComboBox<>(new String[]{
                "Todas las Fases", "Fase de Grupos", "Octavos de Final",
                "Cuartos de Final", "Semifinales", "Tercer Puesto", "FINAL DEL MUNDIAL"
        });
        cbFiltroFasePartidos.setPreferredSize(new Dimension(200, 26));
        cbFiltroFasePartidos.addActionListener(e -> refrescarTablaPartidos());
        pnlFiltro.add(cbFiltroFasePartidos);
        panel.add(pnlFiltro, BorderLayout.NORTH);

        // TABLA DE PARTIDOS
        String[] columnas = {"ID", "Fase", "Selección 1", "Marcador", "Selección 2", "Resultado"};
        modeloPartidos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPartidos = new JTable(modeloPartidos);
        tablaPartidos.setRowHeight(26);
        tablaPartidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPartidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        alinearCentro(tablaPartidos, new int[]{0, 1, 3});

        tablaPartidos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPartidos.getSelectedRow();
            if (fila >= 0) {
                String idP = modeloPartidos.getValueAt(fila, 0).toString();
                Partido p = gestor.buscarPartidoPorId(idP);
                if (p != null) {
                    txtParId.setText(p.getIdPartido());
                    cbParFase.setSelectedItem(p.getFase());
                    txtParSel1.setText(p.getSel1());
                    txtParSel2.setText(p.getSel2());
                    spGoles1.setValue(p.getGolesSel1());
                    spGoles2.setValue(p.getGolesSel2());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaPartidos);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados y Calendario de Partidos"));
        panel.add(scroll, BorderLayout.CENTER);

        // FORMULARIO CRUD PARTIDOS
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Registro de Encuentro (CRUD)"),
                new EmptyBorder(10, 10, 10, 10)));
        pnlForm.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtParId = new JTextField(10);
        cbParFase = new JComboBox<>(new String[]{
                "Fase de Grupos", "Octavos de Final", "Cuartos de Final",
                "Semifinales", "Tercer Puesto", "FINAL DEL MUNDIAL"
        });
        txtParSel1 = new JTextField(12);
        txtParSel2 = new JTextField(12);
        spGoles1 = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        spGoles2 = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));

        agregarFilaFormulario(pnlForm, g, 0, "ID Partido:", txtParId);
        agregarFilaFormulario(pnlForm, g, 1, "Fase del Torneo:", cbParFase);
        agregarFilaFormulario(pnlForm, g, 2, "Selección 1:", txtParSel1);
        agregarFilaFormulario(pnlForm, g, 3, "Goles Sel 1:", spGoles1);
        agregarFilaFormulario(pnlForm, g, 4, "Selección 2:", txtParSel2);
        agregarFilaFormulario(pnlForm, g, 5, "Goles Sel 2:", spGoles2);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, 8, 8));
        JButton btnRegistrar = new JButton("➕ Registrar");
        JButton btnActualizar = new JButton("✏️ Actualizar");
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        JButton btnLimpiar = new JButton("🧹 Limpiar");

        btnRegistrar.setBackground(new Color(40, 167, 69));
        btnRegistrar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(23, 162, 184));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);

        btnRegistrar.addActionListener(e -> accionRegistrarPartido());
        btnActualizar.addActionListener(e -> accionActualizarPartido());
        btnEliminar.addActionListener(e -> accionEliminarPartido());
        btnLimpiar.addActionListener(e -> limpiarFormPartido());

        pnlBotones.add(btnRegistrar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        pnlForm.add(pnlBotones, g);

        panel.add(pnlForm, BorderLayout.EAST);
        return panel;
    }

    private void accionRegistrarPartido() {
        String id = txtParId.getText().trim();
        String fase = (String) cbParFase.getSelectedItem();
        String s1 = txtParSel1.getText().trim();
        String s2 = txtParSel2.getText().trim();
        int g1 = (Integer) spGoles1.getValue();
        int g2 = (Integer) spGoles2.getValue();

        if (id.isEmpty() || s1.isEmpty() || s2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos del partido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Partido p = new Partido(id, fase, s1, s2, g1, g2);
        if (gestor.registrarPartido(p)) {
            JOptionPane.showMessageDialog(this, "Partido registrado y guardado en 'partidosMundial.dat'.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTodo();
            limpiarFormPartido();
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un partido con el ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionActualizarPartido() {
        String id = txtParId.getText().trim();
        String fase = (String) cbParFase.getSelectedItem();
        String s1 = txtParSel1.getText().trim();
        String s2 = txtParSel2.getText().trim();
        int g1 = (Integer) spGoles1.getValue();
        int g2 = (Integer) spGoles2.getValue();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un partido para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestor.actualizarPartidoCompleto(id, fase, s1, s2, g1, g2)) {
            JOptionPane.showMessageDialog(this, "Resultado actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el partido con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminarPartido() {
        String id = txtParId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un partido para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este partido?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestor.eliminarPartido(id)) {
                JOptionPane.showMessageDialog(this, "Partido eliminado y archivo actualizado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                refrescarTodo();
                limpiarFormPartido();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar partido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormPartido() {
        txtParId.setText("");
        txtParSel1.setText("");
        txtParSel2.setText("");
        spGoles1.setValue(0);
        spGoles2.setValue(0);
        tablaPartidos.clearSelection();
    }

    // =========================================================================
    // PESTAÑA 4: POSICIONES Y ESTADÍSTICAS GENERALES
    // =========================================================================

    private JPanel crearPanelPosiciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // TARJETAS INFORMATIVAS (KPIs)
        JPanel pnlKpis = new JPanel(new GridLayout(1, 4, 12, 12));

        lblTotalSelecciones = new JLabel("0", SwingConstants.CENTER);
        lblTotalJugadores = new JLabel("0", SwingConstants.CENTER);
        lblTotalPartidos = new JLabel("0", SwingConstants.CENTER);
        lblMaxGoleador = new JLabel("N/A", SwingConstants.CENTER);

        pnlKpis.add(crearTarjetaKpi("🌍 Selecciones", lblTotalSelecciones, new Color(24, 119, 242)));
        pnlKpis.add(crearTarjetaKpi("⚽ Jugadores", lblTotalJugadores, new Color(40, 167, 69)));
        pnlKpis.add(crearTarjetaKpi("🏟️ Partidos Disputados", lblTotalPartidos, new Color(255, 152, 0)));
        pnlKpis.add(crearTarjetaKpi("🥇 Goleador (Bota de Oro)", lblMaxGoleador, new Color(156, 39, 176)));

        panel.add(pnlKpis, BorderLayout.NORTH);

        // TABLA DE POSICIONES
        String[] columnas = {"Posición", "País", "Puntos", "Partidos Ganados", "Goles a Favor", "Jugadores"};
        modeloPosiciones = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPosiciones = new JTable(modeloPosiciones);
        tablaPosiciones.setRowHeight(28);
        tablaPosiciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPosiciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        alinearCentro(tablaPosiciones, new int[]{0, 2, 3, 4, 5});

        JScrollPane scroll = new JScrollPane(tablaPosiciones);
        scroll.setBorder(BorderFactory.createTitledBorder("Tabla Oficial de Rendimiento de la Copa Mundial"));
        panel.add(scroll, BorderLayout.CENTER);

        // BOTONES DE CONTROL DE PERSISTENCIA
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnGuardarTodo = new JButton("💾 Guardar Todo en Archivos .dat");
        JButton btnRecargarTodo = new JButton("🔄 Recargar desde Disco");
        JButton btnCargarDemo = new JButton("⭐ Cargar Datos de Demostración del Mundial");

        btnGuardarTodo.setBackground(new Color(15, 32, 67));
        btnGuardarTodo.setForeground(Color.WHITE);
        btnRecargarTodo.setBackground(new Color(90, 100, 115));
        btnRecargarTodo.setForeground(Color.WHITE);
        btnCargarDemo.setBackground(new Color(255, 193, 7));
        btnCargarDemo.setForeground(Color.BLACK);

        btnGuardarTodo.addActionListener(e -> {
            gestor.guardarTodo();
            JOptionPane.showMessageDialog(this, "Datos serializados exitosamente en 'seleccionesMundial.dat' y 'partidosMundial.dat'.", "Persistencia OK", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRecargarTodo.addActionListener(e -> {
            gestor.cargarSelecciones();
            gestor.cargarPartidos();
            refrescarTodo();
            JOptionPane.showMessageDialog(this, "Datos recargados desde archivos .dat.", "Recargado", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCargarDemo.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "¿Restaurar selecciones y partidos de demostración?", "Restaurar Demo", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                gestor.cargarDatosInicialesEjemplo();
                refrescarTodo();
                JOptionPane.showMessageDialog(this, "Datos de la Copa Mundial restaurados y guardados.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pnlAcciones.add(btnGuardarTodo);
        pnlAcciones.add(btnRecargarTodo);
        pnlAcciones.add(btnCargarDemo);

        panel.add(pnlAcciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTarjetaKpi(String titulo, JLabel valorLabel, Color colorBorde) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 2, true),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTit.setForeground(new Color(70, 80, 95));

        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valorLabel.setForeground(colorBorde);

        card.add(lblTit, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        return card;
    }

    // =========================================================================
    // MÉTODOS DE REFRESCO Y SINCRONIZACIÓN VISUAL
    // =========================================================================

    public void refrescarTodo() {
        refrescarTablaSelecciones();
        refrescarCombosSeleccion();
        refrescarTablaJugadores();
        refrescarTablaPartidos();
        refrescarTablaPosiciones();
        refrescarEstadisticas();
    }

    private void refrescarTablaSelecciones() {
        modeloSelecciones.setRowCount(0);
        for (Seleccion s : gestor.listarSelecciones()) {
            modeloSelecciones.addRow(new Object[]{
                    s.getIdSeleccion(),
                    s.getNombrePais(),
                    s.getPuntos(),
                    s.getPartidosGanados(),
                    s.getGolesAFavor(),
                    s.getListaJugadores().size()
            });
        }
    }

    private void refrescarCombosSeleccion() {
        String seleccionadoFiltro = (String) cbFiltroJugadores.getSelectedItem();
        String seleccionadoForm = (String) cbJugadorSeleccion.getSelectedItem();

        cbFiltroJugadores.removeAllItems();
        cbJugadorSeleccion.removeAllItems();

        cbFiltroJugadores.addItem("Todas las Selecciones");
        for (Seleccion s : gestor.listarSelecciones()) {
            cbFiltroJugadores.addItem(s.getNombrePais());
            cbJugadorSeleccion.addItem(s.getNombrePais());
        }

        if (seleccionadoFiltro != null) cbFiltroJugadores.setSelectedItem(seleccionadoFiltro);
        if (seleccionadoForm != null) cbJugadorSeleccion.setSelectedItem(seleccionadoForm);
    }

    private void refrescarTablaJugadores() {
        modeloJugadores.setRowCount(0);
        String filtro = (String) cbFiltroJugadores.getSelectedItem();

        for (Seleccion s : gestor.listarSelecciones()) {
            if (filtro == null || filtro.equals("Todas las Selecciones") || filtro.equalsIgnoreCase(s.getNombrePais())) {
                for (Jugador j : s.getListaJugadores()) {
                    modeloJugadores.addRow(new Object[]{
                            j.getIdJugador(),
                            j.getNombre(),
                            j.getPosicion(),
                            j.getGoles(),
                            j.getTarjetasAmarillas(),
                            s.getNombrePais()
                    });
                }
            }
        }
    }

    private void refrescarTablaPartidos() {
        modeloPartidos.setRowCount(0);
        String filtro = (String) cbFiltroFasePartidos.getSelectedItem();
        String faseFiltro = (filtro == null || filtro.equals("Todas las Fases")) ? "Todas" : filtro;

        for (Partido p : gestor.listarPartidosPorFase(faseFiltro)) {
            modeloPartidos.addRow(new Object[]{
                    p.getIdPartido(),
                    p.getFase(),
                    p.getSel1(),
                    p.getGolesSel1() + " - " + p.getGolesSel2(),
                    p.getSel2(),
                    p.getGanador()
            });
        }
    }

    private void refrescarTablaPosiciones() {
        modeloPosiciones.setRowCount(0);
        List<Seleccion> tabla = gestor.getTablaPosiciones();
        int pos = 1;
        for (Seleccion s : tabla) {
            modeloPosiciones.addRow(new Object[]{
                    "#" + (pos++),
                    s.getNombrePais(),
                    s.getPuntos(),
                    s.getPartidosGanados(),
                    s.getGolesAFavor(),
                    s.getListaJugadores().size()
            });
        }
    }

    private void refrescarEstadisticas() {
        lblTotalSelecciones.setText(String.valueOf(gestor.getCantidadSelecciones()));
        lblTotalJugadores.setText(String.valueOf(gestor.getCantidadJugadores()));
        lblTotalPartidos.setText(String.valueOf(gestor.getCantidadPartidos()));

        Jugador max = gestor.getGoleadorMaximo();
        if (max != null) {
            Seleccion pais = gestor.buscarSeleccionDeJugador(max.getIdJugador());
            String nombrePais = (pais != null) ? " (" + pais.getNombrePais() + ")" : "";
            lblMaxGoleador.setText(max.getNombre() + nombrePais + " - " + max.getGoles() + " ⚽");
        } else {
            lblMaxGoleador.setText("Sin registros");
        }
    }

    // =========================================================================
    // MÉTODOS DE UTILIDAD
    // =========================================================================

    private void agregarFilaFormulario(JPanel pnl, GridBagConstraints g, int fila, String etiqueta, JComponent comp) {
        g.gridx = 0;
        g.gridy = fila;
        g.gridwidth = 1;
        g.weightx = 0.3;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnl.add(lbl, g);

        g.gridx = 1;
        g.weightx = 0.7;
        pnl.add(comp, g);
    }

    private void alinearCentro(JTable tabla, int[] columnas) {
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c : columnas) {
            if (c < tabla.getColumnCount()) {
                tabla.getColumnModel().getColumn(c).setCellRenderer(centro);
            }
        }
    }
}
