import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * ============================================================================
 * MODO 3: INTERFAZ GRÁFICA JAVAFX (Ventana Moderna)
 * ============================================================================
 * Implementación de la interfaz gráfica de usuario con JavaFX (OpenJFX).
 * Cumple con los lineamientos del profesor:
 *   - Hereda de javafx.application.Application
 *   - Implementa pestañas (TabPane) para Selecciones, Jugadores, Partidos y Posiciones
 *   - Tablas reactivas con TableView y ObservableList
 *   - Persistencia automática en archivos .dat a través de GestorCopaMundial
 *   - Cuadros de diálogo y alertas nativas de JavaFX (Alert)
 *
 * Para ejecutar en entornos sin JavaFX integrado en el JDK:
 *   java --module-path <ruta-openjfx\lib> --add-modules javafx.controls VentanaJavaFX
 */
public class VentanaJavaFX extends Application {

    private static GestorCopaMundial gestorCompartido;

    // Listas observables para las tablas
    private final ObservableList<Seleccion> datosSelecciones = FXCollections.observableArrayList();
    private final ObservableList<JugadorConPais> datosJugadores = FXCollections.observableArrayList();
    private final ObservableList<Partido> datosPartidos = FXCollections.observableArrayList();
    private final ObservableList<Seleccion> datosPosiciones = FXCollections.observableArrayList();

    // Tablas JavaFX
    private TableView<Seleccion> tablaSelecciones;
    private TableView<JugadorConPais> tablaJugadores;
    private TableView<Partido> tablaPartidos;
    private TableView<Seleccion> tablaPosiciones;

    // Componentes de formulario Selecciones
    private TextField txtSelId;
    private TextField txtSelPais;
    private TextField txtSelPuntos;
    private TextField txtSelPG;
    private TextField txtSelGF;

    // Componentes de formulario Jugadores
    private ComboBox<String> cbJugadorSeleccion;
    private ComboBox<String> cbFiltroJugadores;
    private TextField txtJugId;
    private TextField txtJugNombre;
    private ComboBox<String> cbJugPosicion;
    private TextField txtJugGoles;
    private TextField txtJugAmarillas;

    // Componentes de formulario Partidos
    private ComboBox<String> cbFiltroFasePartidos;
    private TextField txtParId;
    private ComboBox<String> cbParFase;
    private TextField txtParSel1;
    private TextField txtParSel2;
    private Spinner<Integer> spGoles1;
    private Spinner<Integer> spGoles2;

    // Etiquetas de estadísticas
    private Label lblTotalSelecciones;
    private Label lblTotalJugadores;
    private Label lblTotalPartidos;
    private Label lblGoleador;

    /**
     * Método de inicio del ciclo de vida JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        if (gestorCompartido == null) {
            gestorCompartido = new GestorCopaMundial();
        }

        primaryStage.setTitle("🏆 Copa Mundial de Fútbol - Gestión JavaFX");

        BorderPane raiz = new BorderPane();

        // 1. Encabezado superior
        VBox encabezado = new VBox(5);
        encabezado.setPadding(new Insets(15, 20, 15, 20));
        encabezado.setStyle("-fx-background-color: #0f2043;");

        Label titulo = new Label("⚽ SISTEMA DE GESTIÓN DE LA COPA MUNDIAL");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffd700;");

        Label subtitulo = new Label("Modo 3: Interfaz Gráfica JavaFX  •  Persistencia en Archivos Binarios .dat  •  CRUD Completo");
        subtitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #dce6f5;");

        encabezado.getChildren().addAll(titulo, subtitulo);
        raiz.setTop(encabezado);

        // 2. TabPane con las 4 pestañas requeridas
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(crearPestanaSelecciones());
        tabPane.getTabs().add(crearPestanaJugadores());
        tabPane.getTabs().add(crearPestanaPartidos());
        tabPane.getTabs().add(crearPestanaPosiciones());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        raiz.setCenter(tabPane);

        // 3. Barra de estado inferior
        HBox pie = new HBox();
        pie.setPadding(new Insets(8, 15, 8, 15));
        pie.setAlignment(Pos.CENTER_RIGHT);
        pie.setStyle("-fx-background-color: #f0f4fa; -fx-border-color: #d2dceb; -fx-border-width: 1 0 0 0;");
        Label lblPie = new Label("💾 Persistencia activa: 'seleccionesMundial.dat' y 'partidosMundial.dat'");
        lblPie.setStyle("-fx-font-size: 11px; -fx-text-fill: #505a69; -fx-font-style: italic;");
        pie.getChildren().add(lblPie);
        raiz.setBottom(pie);

        Scene scene = new Scene(raiz, 1050, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        refrescarTodo();
    }

    // =========================================================================
    // PESTAÑA 1: CRUD SELECCIONES (JavaFX)
    // =========================================================================

    private Tab crearPestanaSelecciones() {
        Tab tab = new Tab("🌍 Selecciones Nacionales");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Tabla de Selecciones
        tablaSelecciones = new TableView<>();
        TableColumn<Seleccion, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIdSeleccion()));
        colId.setPrefWidth(70);

        TableColumn<Seleccion, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombrePais()));
        colPais.setPrefWidth(160);

        TableColumn<Seleccion, Number> colPts = new TableColumn<>("Puntos");
        colPts.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPuntos()));
        colPts.setPrefWidth(90);

        TableColumn<Seleccion, Number> colPG = new TableColumn<>("Partidos Ganados");
        colPG.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPartidosGanados()));
        colPG.setPrefWidth(130);

        TableColumn<Seleccion, Number> colGF = new TableColumn<>("Goles a Favor");
        colGF.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getGolesAFavor()));
        colGF.setPrefWidth(110);

        TableColumn<Seleccion, Number> colJug = new TableColumn<>("Jugadores");
        colJug.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getListaJugadores().size()));
        colJug.setPrefWidth(100);

        tablaSelecciones.getColumns().addAll(colId, colPais, colPts, colPG, colGF, colJug);
        tablaSelecciones.setItems(datosSelecciones);

        tablaSelecciones.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                txtSelId.setText(nuevo.getIdSeleccion());
                txtSelPais.setText(nuevo.getNombrePais());
                txtSelPuntos.setText(String.valueOf(nuevo.getPuntos()));
                txtSelPG.setText(String.valueOf(nuevo.getPartidosGanados()));
                txtSelGF.setText(String.valueOf(nuevo.getGolesAFavor()));
            }
        });

        layout.setCenter(tablaSelecciones);

        // Formulario CRUD lateral
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #d0d7de; -fx-border-radius: 5; -fx-background-color: #ffffff;");

        Label lblTitForm = new Label("Formulario de Selección (CRUD)");
        lblTitForm.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        txtSelId = new TextField();
        txtSelId.setPromptText("Ej: S007");
        txtSelPais = new TextField();
        txtSelPais.setPromptText("Ej: Colombia");
        txtSelPuntos = new TextField("0");
        txtSelPG = new TextField("0");
        txtSelGF = new TextField("0");

        form.getChildren().addAll(
                lblTitForm,
                new Label("ID Selección:"), txtSelId,
                new Label("País:"), txtSelPais,
                new Label("Puntos:"), txtSelPuntos,
                new Label("Partidos Ganados:"), txtSelPG,
                new Label("Goles a Favor:"), txtSelGF
        );

        // Botones CRUD
        GridPane gridBtns = new GridPane();
        gridBtns.setHgap(8);
        gridBtns.setVgap(8);

        Button btnAgregar = new Button("➕ Agregar");
        btnAgregar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAgregar.setOnAction(e -> accionAgregarSeleccion());

        Button btnActualizar = new Button("✏️ Actualizar");
        btnActualizar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold;");
        btnActualizar.setOnAction(e -> accionActualizarSeleccion());

        Button btnEliminar = new Button("🗑️ Eliminar");
        btnEliminar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setOnAction(e -> accionEliminarSeleccion());

        Button btnLimpiar = new Button("🧹 Limpiar");
        btnLimpiar.setOnAction(e -> limpiarFormSeleccion());

        gridBtns.add(btnAgregar, 0, 0);
        gridBtns.add(btnActualizar, 1, 0);
        gridBtns.add(btnEliminar, 0, 1);
        gridBtns.add(btnLimpiar, 1, 1);

        form.getChildren().add(gridBtns);
        layout.setRight(form);

        tab.setContent(layout);
        return tab;
    }

    private void accionAgregarSeleccion() {
        String id = txtSelId.getText().trim();
        String pais = txtSelPais.getText().trim();
        if (id.isEmpty() || pais.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Complete ID y País.");
            return;
        }
        try {
            int pts = Integer.parseInt(txtSelPuntos.getText().trim());
            int pg = Integer.parseInt(txtSelPG.getText().trim());
            int gf = Integer.parseInt(txtSelGF.getText().trim());

            Seleccion s = new Seleccion(id, pais, pts, pg, gf);
            if (gestorCompartido.crearSeleccion(s)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Selección registrada y persistida en disco (.dat).");
                refrescarTodo();
                limpiarFormSeleccion();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ya existe una selección con el ID " + id);
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Valores numéricos no válidos.");
        }
    }

    private void accionActualizarSeleccion() {
        String id = txtSelId.getText().trim();
        String pais = txtSelPais.getText().trim();
        if (id.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Seleccione una selección a actualizar.");
            return;
        }
        try {
            int pts = Integer.parseInt(txtSelPuntos.getText().trim());
            int pg = Integer.parseInt(txtSelPG.getText().trim());
            int gf = Integer.parseInt(txtSelGF.getText().trim());

            if (gestorCompartido.actualizarSeleccionCompleta(id, pais, pts, pg, gf)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Selección actualizada.");
                refrescarTodo();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se encontró selección.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Valores numéricos no válidos.");
        }
    }

    private void accionEliminarSeleccion() {
        String id = txtSelId.getText().trim();
        if (id.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Seleccione una selección a eliminar.");
            return;
        }
        if (gestorCompartido.eliminarSeleccion(id)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Selección eliminada de la base de datos.");
            refrescarTodo();
            limpiarFormSeleccion();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar.");
        }
    }

    private void limpiarFormSeleccion() {
        txtSelId.setText("");
        txtSelPais.setText("");
        txtSelPuntos.setText("0");
        txtSelPG.setText("0");
        txtSelGF.setText("0");
        tablaSelecciones.getSelectionModel().clearSelection();
    }

    // =========================================================================
    // PESTAÑA 2: CRUD JUGADORES (JavaFX)
    // =========================================================================

    private Tab crearPestanaJugadores() {
        Tab tab = new Tab("⚽ Plantilla de Jugadores");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Barra superior con filtro
        HBox pnlFiltro = new HBox(10);
        pnlFiltro.setPadding(new Insets(0, 0, 10, 0));
        pnlFiltro.setAlignment(Pos.CENTER_LEFT);
        cbFiltroJugadores = new ComboBox<>();
        cbFiltroJugadores.setOnAction(e -> refrescarTablaJugadores());
        pnlFiltro.getChildren().addAll(new Label("Filtrar por Selección:"), cbFiltroJugadores);
        layout.setTop(pnlFiltro);

        // Tabla de Jugadores
        tablaJugadores = new TableView<>();
        TableColumn<JugadorConPais, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().jugador.getIdJugador()));
        colId.setPrefWidth(70);

        TableColumn<JugadorConPais, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().jugador.getNombre()));
        colNombre.setPrefWidth(180);

        TableColumn<JugadorConPais, String> colPos = new TableColumn<>("Posición");
        colPos.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().jugador.getPosicion()));
        colPos.setPrefWidth(120);

        TableColumn<JugadorConPais, Number> colGoles = new TableColumn<>("Goles");
        colGoles.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().jugador.getGoles()));
        colGoles.setPrefWidth(80);

        TableColumn<JugadorConPais, Number> colTarjetas = new TableColumn<>("Amarillas");
        colTarjetas.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().jugador.getTarjetasAmarillas()));
        colTarjetas.setPrefWidth(90);

        TableColumn<JugadorConPais, String> colPais = new TableColumn<>("Selección");
        colPais.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().pais));
        colPais.setPrefWidth(140);

        tablaJugadores.getColumns().addAll(colId, colNombre, colPos, colGoles, colTarjetas, colPais);
        tablaJugadores.setItems(datosJugadores);

        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, v, n) -> {
            if (n != null) {
                txtJugId.setText(n.jugador.getIdJugador());
                txtJugNombre.setText(n.jugador.getNombre());
                cbJugPosicion.setValue(n.jugador.getPosicion());
                txtJugGoles.setText(String.valueOf(n.jugador.getGoles()));
                txtJugAmarillas.setText(String.valueOf(n.jugador.getTarjetasAmarillas()));
                cbJugadorSeleccion.setValue(n.pais);
            }
        });

        layout.setCenter(tablaJugadores);

        // Formulario CRUD Jugadores
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #d0d7de; -fx-border-radius: 5; -fx-background-color: #ffffff;");

        Label lblTit = new Label("Formulario de Jugador (CRUD)");
        lblTit.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        cbJugadorSeleccion = new ComboBox<>();
        txtJugId = new TextField();
        txtJugNombre = new TextField();
        cbJugPosicion = new ComboBox<>(FXCollections.observableArrayList("Arquero", "Defensa", "Mediocampista", "Delantero"));
        cbJugPosicion.setValue("Delantero");
        txtJugGoles = new TextField("0");
        txtJugAmarillas = new TextField("0");

        form.getChildren().addAll(
                lblTit,
                new Label("Selección Asignada:"), cbJugadorSeleccion,
                new Label("ID Jugador:"), txtJugId,
                new Label("Nombre Completo:"), txtJugNombre,
                new Label("Posición:"), cbJugPosicion,
                new Label("Goles Marcados:"), txtJugGoles,
                new Label("Tarjetas Amarillas:"), txtJugAmarillas
        );

        GridPane gridBtns = new GridPane();
        gridBtns.setHgap(8);
        gridBtns.setVgap(8);

        Button btnAgregar = new Button("➕ Registrar");
        btnAgregar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAgregar.setOnAction(e -> accionAgregarJugador());

        Button btnActualizar = new Button("✏️ Actualizar");
        btnActualizar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold;");
        btnActualizar.setOnAction(e -> accionActualizarJugador());

        Button btnEliminar = new Button("🗑️ Eliminar");
        btnEliminar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setOnAction(e -> accionEliminarJugador());

        Button btnLimpiar = new Button("🧹 Limpiar");
        btnLimpiar.setOnAction(e -> limpiarFormJugador());

        gridBtns.add(btnAgregar, 0, 0);
        gridBtns.add(btnActualizar, 1, 0);
        gridBtns.add(btnEliminar, 0, 1);
        gridBtns.add(btnLimpiar, 1, 1);

        form.getChildren().add(gridBtns);
        layout.setRight(form);

        tab.setContent(layout);
        return tab;
    }

    private void accionAgregarJugador() {
        String id = txtJugId.getText().trim();
        String nombre = txtJugNombre.getText().trim();
        String pos = cbJugPosicion.getValue();
        String paisSel = cbJugadorSeleccion.getValue();

        if (id.isEmpty() || nombre.isEmpty() || paisSel == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Complete todos los campos del jugador.");
            return;
        }

        Seleccion sel = gestorCompartido.buscarSeleccionPorPais(paisSel);
        if (sel == null) return;

        try {
            int g = Integer.parseInt(txtJugGoles.getText().trim());
            int a = Integer.parseInt(txtJugAmarillas.getText().trim());
            Jugador nuevo = new Jugador(id, nombre, pos, g, a);

            if (gestorCompartido.agregarJugadorASeleccion(sel.getIdSeleccion(), nuevo)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador incorporado a " + sel.getNombrePais());
                refrescarTodo();
                limpiarFormJugador();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "ID de jugador duplicado.");
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Formato numérico inválido.");
        }
    }

    private void accionActualizarJugador() {
        String id = txtJugId.getText().trim();
        String nombre = txtJugNombre.getText().trim();
        String pos = cbJugPosicion.getValue();
        if (id.isEmpty() || nombre.isEmpty()) return;

        try {
            int g = Integer.parseInt(txtJugGoles.getText().trim());
            int a = Integer.parseInt(txtJugAmarillas.getText().trim());

            if (gestorCompartido.actualizarJugador(id, nombre, pos, g, a)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador actualizado.");
                refrescarTodo();
            }
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Datos inválidos.");
        }
    }

    private void accionEliminarJugador() {
        String id = txtJugId.getText().trim();
        if (id.isEmpty()) return;
        if (gestorCompartido.eliminarJugador(id)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Jugador eliminado.");
            refrescarTodo();
            limpiarFormJugador();
        }
    }

    private void limpiarFormJugador() {
        txtJugId.setText("");
        txtJugNombre.setText("");
        txtJugGoles.setText("0");
        txtJugAmarillas.setText("0");
        tablaJugadores.getSelectionModel().clearSelection();
    }

    // =========================================================================
    // PESTAÑA 3: CRUD PARTIDOS (JavaFX)
    // =========================================================================

    private Tab crearPestanaPartidos() {
        Tab tab = new Tab("🏟️ Partidos y Resultados");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Filtro de Fase
        HBox pnlFiltro = new HBox(10);
        pnlFiltro.setPadding(new Insets(0, 0, 10, 0));
        pnlFiltro.setAlignment(Pos.CENTER_LEFT);
        cbFiltroFasePartidos = new ComboBox<>(FXCollections.observableArrayList(
                "Todas las Fases", "Fase de Grupos", "Octavos de Final",
                "Cuartos de Final", "Semifinales", "Tercer Puesto", "FINAL DEL MUNDIAL"
        ));
        cbFiltroFasePartidos.setValue("Todas las Fases");
        cbFiltroFasePartidos.setOnAction(e -> refrescarTablaPartidos());
        pnlFiltro.getChildren().addAll(new Label("Filtrar por Fase del Mundial:"), cbFiltroFasePartidos);
        layout.setTop(pnlFiltro);

        // Tabla de Partidos
        tablaPartidos = new TableView<>();
        TableColumn<Partido, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIdPartido()));
        colId.setPrefWidth(70);

        TableColumn<Partido, String> colFase = new TableColumn<>("Fase");
        colFase.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFase()));
        colFase.setPrefWidth(140);

        TableColumn<Partido, String> colS1 = new TableColumn<>("Selección 1");
        colS1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSel1()));
        colS1.setPrefWidth(140);

        TableColumn<Partido, String> colMarcador = new TableColumn<>("Marcador");
        colMarcador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGolesSel1() + " - " + d.getValue().getGolesSel2()));
        colMarcador.setPrefWidth(100);

        TableColumn<Partido, String> colS2 = new TableColumn<>("Selección 2");
        colS2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSel2()));
        colS2.setPrefWidth(140);

        TableColumn<Partido, String> colGanador = new TableColumn<>("Ganador");
        colGanador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGanador()));
        colGanador.setPrefWidth(140);

        tablaPartidos.getColumns().addAll(colId, colFase, colS1, colMarcador, colS2, colGanador);
        tablaPartidos.setItems(datosPartidos);

        tablaPartidos.getSelectionModel().selectedItemProperty().addListener((obs, v, n) -> {
            if (n != null) {
                txtParId.setText(n.getIdPartido());
                cbParFase.setValue(n.getFase());
                txtParSel1.setText(n.getSel1());
                txtParSel2.setText(n.getSel2());
                spGoles1.getValueFactory().setValue(n.getGolesSel1());
                spGoles2.getValueFactory().setValue(n.getGolesSel2());
            }
        });

        layout.setCenter(tablaPartidos);

        // Formulario de Partidos
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #d0d7de; -fx-border-radius: 5; -fx-background-color: #ffffff;");

        Label lblTit = new Label("Registro de Encuentro (CRUD)");
        lblTit.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        txtParId = new TextField();
        cbParFase = new ComboBox<>(FXCollections.observableArrayList(
                "Fase de Grupos", "Octavos de Final", "Cuartos de Final",
                "Semifinales", "Tercer Puesto", "FINAL DEL MUNDIAL"
        ));
        cbParFase.setValue("Fase de Grupos");
        txtParSel1 = new TextField();
        txtParSel2 = new TextField();
        spGoles1 = new Spinner<>(0, 50, 0);
        spGoles2 = new Spinner<>(0, 50, 0);

        form.getChildren().addAll(
                lblTit,
                new Label("ID Partido:"), txtParId,
                new Label("Fase:"), cbParFase,
                new Label("Selección 1:"), txtParSel1,
                new Label("Goles Sel 1:"), spGoles1,
                new Label("Selección 2:"), txtParSel2,
                new Label("Goles Sel 2:"), spGoles2
        );

        GridPane gridBtns = new GridPane();
        gridBtns.setHgap(8);
        gridBtns.setVgap(8);

        Button btnRegistrar = new Button("➕ Registrar");
        btnRegistrar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        btnRegistrar.setOnAction(e -> accionRegistrarPartido());

        Button btnActualizar = new Button("✏️ Actualizar");
        btnActualizar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold;");
        btnActualizar.setOnAction(e -> accionActualizarPartido());

        Button btnEliminar = new Button("🗑️ Eliminar");
        btnEliminar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setOnAction(e -> accionEliminarPartido());

        Button btnLimpiar = new Button("🧹 Limpiar");
        btnLimpiar.setOnAction(e -> limpiarFormPartido());

        gridBtns.add(btnRegistrar, 0, 0);
        gridBtns.add(btnActualizar, 1, 0);
        gridBtns.add(btnEliminar, 0, 1);
        gridBtns.add(btnLimpiar, 1, 1);

        form.getChildren().add(gridBtns);
        layout.setRight(form);

        tab.setContent(layout);
        return tab;
    }

    private void accionRegistrarPartido() {
        String id = txtParId.getText().trim();
        String fase = cbParFase.getValue();
        String s1 = txtParSel1.getText().trim();
        String s2 = txtParSel2.getText().trim();
        int g1 = spGoles1.getValue();
        int g2 = spGoles2.getValue();

        if (id.isEmpty() || s1.isEmpty() || s2.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Complete los datos del partido.");
            return;
        }

        Partido p = new Partido(id, fase, s1, s2, g1, g2);
        if (gestorCompartido.registrarPartido(p)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partido guardado y persistido.");
            refrescarTodo();
            limpiarFormPartido();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "ID de partido duplicado.");
        }
    }

    private void accionActualizarPartido() {
        String id = txtParId.getText().trim();
        String fase = cbParFase.getValue();
        String s1 = txtParSel1.getText().trim();
        String s2 = txtParSel2.getText().trim();
        int g1 = spGoles1.getValue();
        int g2 = spGoles2.getValue();

        if (id.isEmpty()) return;
        if (gestorCompartido.actualizarPartidoCompleto(id, fase, s1, s2, g1, g2)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Resultado actualizado.");
            refrescarTodo();
        }
    }

    private void accionEliminarPartido() {
        String id = txtParId.getText().trim();
        if (id.isEmpty()) return;
        if (gestorCompartido.eliminarPartido(id)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Partido eliminado.");
            refrescarTodo();
            limpiarFormPartido();
        }
    }

    private void limpiarFormPartido() {
        txtParId.setText("");
        txtParSel1.setText("");
        txtParSel2.setText("");
        spGoles1.getValueFactory().setValue(0);
        spGoles2.getValueFactory().setValue(0);
        tablaPartidos.getSelectionModel().clearSelection();
    }

    // =========================================================================
    // PESTAÑA 4: POSICIONES Y ESTADÍSTICAS (JavaFX)
    // =========================================================================

    private Tab crearPestanaPosiciones() {
        Tab tab = new Tab("📊 Posiciones y Resumen");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Tarjetas KPIs
        HBox pnlKpis = new HBox(15);
        pnlKpis.setPadding(new Insets(10, 0, 15, 0));
        pnlKpis.setAlignment(Pos.CENTER);

        lblTotalSelecciones = new Label("0");
        lblTotalJugadores = new Label("0");
        lblTotalPartidos = new Label("0");
        lblGoleador = new Label("N/A");

        pnlKpis.getChildren().addAll(
                crearTarjetaKpi("🌍 Selecciones", lblTotalSelecciones, "#1877f2"),
                crearTarjetaKpi("⚽ Jugadores", lblTotalJugadores, "#28a745"),
                crearTarjetaKpi("🏟️ Partidos", lblTotalPartidos, "#ff9800"),
                crearTarjetaKpi("🥇 Goleador", lblGoleador, "#9c27b0")
        );
        layout.setTop(pnlKpis);

        // Tabla de posiciones
        tablaPosiciones = new TableView<>();
        TableColumn<Seleccion, String> colPais = new TableColumn<>("País");
        colPais.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombrePais()));
        colPais.setPrefWidth(200);

        TableColumn<Seleccion, Number> colPts = new TableColumn<>("Puntos");
        colPts.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPuntos()));
        colPts.setPrefWidth(100);

        TableColumn<Seleccion, Number> colPG = new TableColumn<>("PG");
        colPG.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPartidosGanados()));
        colPG.setPrefWidth(100);

        TableColumn<Seleccion, Number> colGF = new TableColumn<>("Goles a Favor");
        colGF.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getGolesAFavor()));
        colGF.setPrefWidth(120);

        TableColumn<Seleccion, Number> colPlantilla = new TableColumn<>("Jugadores");
        colPlantilla.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getListaJugadores().size()));
        colPlantilla.setPrefWidth(120);

        tablaPosiciones.getColumns().addAll(colPais, colPts, colPG, colGF, colPlantilla);
        tablaPosiciones.setItems(datosPosiciones);

        layout.setCenter(tablaPosiciones);

        // Botones de persistencia
        HBox pnlBtns = new HBox(15);
        pnlBtns.setPadding(new Insets(15, 0, 5, 0));
        pnlBtns.setAlignment(Pos.CENTER);

        Button btnGuardar = new Button("💾 Guardar Todo (.dat)");
        btnGuardar.setStyle("-fx-background-color: #0f2043; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            gestorCompartido.guardarTodo();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Persistencia", "Datos guardados en archivos .dat.");
        });

        Button btnRecargar = new Button("🔄 Recargar de Disco");
        btnRecargar.setOnAction(e -> {
            gestorCompartido.cargarSelecciones();
            gestorCompartido.cargarPartidos();
            refrescarTodo();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Recargar", "Datos actualizados desde archivos .dat.");
        });

        Button btnDemo = new Button("⭐ Restaurar Demostración");
        btnDemo.setOnAction(e -> {
            gestorCompartido.cargarDatosInicialesEjemplo();
            refrescarTodo();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Demostración", "Datos de muestra del Mundial cargados.");
        });

        pnlBtns.getChildren().addAll(btnGuardar, btnRecargar, btnDemo);
        layout.setBottom(pnlBtns);

        tab.setContent(layout);
        return tab;
    }

    private VBox crearTarjetaKpi(String titulo, Label valor, String colorHex) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10, 15, 10, 15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-border-color: " + colorHex + "; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label lblTit = new Label(titulo);
        lblTit.setStyle("-fx-font-weight: bold; -fx-text-fill: #444; -fx-font-size: 11px;");

        valor.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + colorHex + ";");

        card.getChildren().addAll(lblTit, valor);
        return card;
    }

    // =========================================================================
    // MÉTODOS DE SINCRONIZACIÓN Y REFRESCO JAVAFX
    // =========================================================================

    private void refrescarTodo() {
        // Selecciones
        datosSelecciones.setAll(gestorCompartido.listarSelecciones());

        // Combos
        String filtroAnt = cbFiltroJugadores.getValue();
        String selAnt = cbJugadorSeleccion.getValue();

        cbFiltroJugadores.getItems().clear();
        cbJugadorSeleccion.getItems().clear();

        cbFiltroJugadores.getItems().add("Todas las Selecciones");
        for (Seleccion s : gestorCompartido.listarSelecciones()) {
            cbFiltroJugadores.getItems().add(s.getNombrePais());
            cbJugadorSeleccion.getItems().add(s.getNombrePais());
        }

        if (filtroAnt != null && cbFiltroJugadores.getItems().contains(filtroAnt)) {
            cbFiltroJugadores.setValue(filtroAnt);
        } else {
            cbFiltroJugadores.setValue("Todas las Selecciones");
        }

        if (selAnt != null && cbJugadorSeleccion.getItems().contains(selAnt)) {
            cbJugadorSeleccion.setValue(selAnt);
        } else if (!cbJugadorSeleccion.getItems().isEmpty()) {
            cbJugadorSeleccion.setValue(cbJugadorSeleccion.getItems().get(0));
        }

        refrescarTablaJugadores();
        refrescarTablaPartidos();

        // Posiciones
        datosPosiciones.setAll(gestorCompartido.getTablaPosiciones());

        // KPIs
        lblTotalSelecciones.setText(String.valueOf(gestorCompartido.getCantidadSelecciones()));
        lblTotalJugadores.setText(String.valueOf(gestorCompartido.getCantidadJugadores()));
        lblTotalPartidos.setText(String.valueOf(gestorCompartido.getCantidadPartidos()));

        Jugador max = gestorCompartido.getGoleadorMaximo();
        if (max != null) {
            lblGoleador.setText(max.getNombre() + " (" + max.getGoles() + " ⚽)");
        } else {
            lblGoleador.setText("N/A");
        }
    }

    private void refrescarTablaJugadores() {
        datosJugadores.clear();
        String filtro = cbFiltroJugadores.getValue();
        for (Seleccion s : gestorCompartido.listarSelecciones()) {
            if (filtro == null || filtro.equals("Todas las Selecciones") || filtro.equalsIgnoreCase(s.getNombrePais())) {
                for (Jugador j : s.getListaJugadores()) {
                    datosJugadores.add(new JugadorConPais(j, s.getNombrePais()));
                }
            }
        }
    }

    private void refrescarTablaPartidos() {
        String filtro = cbFiltroFasePartidos.getValue();
        String fase = (filtro == null || filtro.equals("Todas las Fases")) ? "Todas" : filtro;
        datosPartidos.setAll(gestorCompartido.listarPartidosPorFase(fase));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Clase auxiliar interna para presentar el jugador junto al nombre de su selección.
     */
    public static class JugadorConPais {
        public final Jugador jugador;
        public final String pais;

        public JugadorConPais(Jugador jugador, String pais) {
            this.jugador = jugador;
            this.pais = pais;
        }
    }

    /**
     * Permite inyectar la instancia compartida del gestor antes de iniciar JavaFX.
     */
    public static void setGestor(GestorCopaMundial gestor) {
        gestorCompartido = gestor;
    }

    /**
     * Lanzador estático para invocar JavaFX desde el selector Main.
     */
    public static void iniciar(String[] args) {
        launch(args);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
