package controllers.gestionar;

import controllers.BaseController;
import controllers.GlobalData;
import controllers.crear.SeleccionarTipoHabitacionController;
import controllers.modificar.ModificarHabitacionController;
import enums.EstadoHabitacion;
import enums.TipoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Habitacion.*;
import models.Usuarios.Usuario;
import services.GestionHabitaciones;
import services.GestionUsuario;
import services.Sesion;

import java.io.IOException;
import java.util.List;

import static java.lang.String.valueOf;

public class GestionarHabitacionesController extends BaseController {

    @FXML
    private TableView<Habitacion> tablaHabitaciones;
    @FXML
    private TextField idHabitacionField;
    @FXML
    private ComboBox<String> tipoHabitacionComboBox;
    @FXML
    private Button crearNuevaHabitacionButton;
    @FXML
    private Button modificarHabitacionButton;
    @FXML
    private Button eliminarHabitacionButton;
    @FXML
    private Button verDetallesHabitacionButton;


    @FXML
    private TableColumn<Habitacion, Integer> columnaNro;
    @FXML
    private TableColumn<Habitacion, String> columnaTipo;
    @FXML
    private TableColumn<Habitacion, Integer> columnaCapacidad;
    @FXML
    private TableColumn<Habitacion, EstadoHabitacion> columnaEstado;
    @FXML
    private CheckBox disponibleCheckBox;


    private ObservableList<Habitacion> habitacionesOriginales;

    private GestionHabitaciones gestionHabitaciones;

    private Usuario usuarioLogueado;

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    private void initialize() {
        // Configuración inicial
        setTextFieldLimit(idHabitacionField, 3);
        cargarHabitaciones();  // Cargar las habitaciones en la tabla

        disponibleCheckBox.setSelected(false); // Inicialmente no está seleccionado

        // Cargar las opciones en el ComboBox
        tipoHabitacionComboBox.setItems(FXCollections.observableArrayList("Todos", "Individual", "Doble", "Apartamento", "Suite", "Presidencial"));
        tipoHabitacionComboBox.getSelectionModel().select("Todos");

        // Agregar listeners para los filtros
        idHabitacionField.textProperty().addListener((observable, oldValue, newValue) -> filtrarHabitaciones());
        tipoHabitacionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filtrarHabitaciones());
        disponibleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> filtrarHabitaciones());

        // Configurar las columnas de la tabla
        columnaNro.setCellValueFactory(new PropertyValueFactory<>("numero"));
        columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Ocultar botones para clientes
        if (usuarioLogueado.getTipoUsuario() == TipoUsuario.CLIENTE) {
            crearNuevaHabitacionButton.setVisible(false);
            modificarHabitacionButton.setVisible(false);
            eliminarHabitacionButton.setVisible(false);
        }else if (usuarioLogueado.getTipoUsuario() == TipoUsuario.CONSERJE){
            eliminarHabitacionButton.setVisible(false);
            crearNuevaHabitacionButton.setVisible(false);
        }
    }




    private void cargarHabitaciones() {

        // Obtener la instancia de GestionUsuario para cargar los usuarios
        this.gestionHabitaciones = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");

        // Obtener el usuario logueado
        usuarioLogueado = Sesion.getUsuarioLogueado();

        // Obtener los usuarios desde el singleton de GestionUsuario
        List<Habitacion> habitaciones = gestionHabitaciones.getHabitaciones();

        // Limpiar la tabla antes de agregar nuevos elementos
        tablaHabitaciones.getItems().clear();

        // Guardar la lista original de usuarios
        habitacionesOriginales = FXCollections.observableArrayList(habitaciones);

        // Actualizar la tabla con las habitaciones obtenidos (filtrados si es necesario)
        tablaHabitaciones.setItems(habitacionesOriginales); // Actualiza la tabla en la UI
    }

    private String tipoHabitacionSeleccionado;

    @FXML
    private void crearNuevaHabitacion(ActionEvent event) {
        try {
            // Guardar la escena actual antes de cambiarla
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Cargar el FXML para la ventana de selección del tipo de habitación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/seleccionarTipoHabitacion.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            SeleccionarTipoHabitacionController seleccionarTipoController = loader.getController();

            // Pasar el controlador principal (GestionarHabitacionesController) al nuevo controlador
            seleccionarTipoController.setPreviousController(this);

            // Crear una nueva ventana (Stage) para seleccionar el tipo de habitación
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Seleccionar Tipo de Habitación");
            newStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal hasta que se cierre esta
            newStage.showAndWait();

            // Después de que se cierra la ventana auxiliar, actualizar la lista de habitaciones en el controlador principal
            // Solo si se ha seleccionado un tipo de habitación
            if (tipoHabitacionSeleccionado != null) {
                System.out.println("SELECCIONADO EL TIPO DE HABITACION: " + tipoHabitacionSeleccionado);

                // Aquí puedes agregar la lógica para actualizar la lista de habitaciones.
                // Suponiendo que tienes un metodo `actualizarListaHabitaciones()` en tu controlador principal:
            }
            actualizarListaHabitaciones();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarListaHabitaciones() {
        // Recargar los usuarios desde el archivo JSON
        cargarHabitaciones(); // Recarga la lista de habitaciones desde el archivo JSON

        // Actualiza la tabla con los usuarios obtenidos
        tablaHabitaciones.setItems(habitacionesOriginales); // Actualiza la tabla en la UI
    }

    public void setTipoHabitacionSeleccionado(String tipoHabitacionSeleccionado) {
        this.tipoHabitacionSeleccionado = tipoHabitacionSeleccionado;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void modificarHabitacion() {
        // Obtener la habitación seleccionada en la tabla
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();

        if (habitacionSeleccionada != null) {
            // Crear el Stage para la ventana de modificación
            try {
                GlobalData.setHabitacionSeleccionada(habitacionSeleccionada);
                // Cargar el FXML correspondiente a la ventana de modificación
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/ModificarHabitacion.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la ventana de modificación
                ModificarHabitacionController modificarHabitacionController = loader.getController();

                // Mostrar la ventana en un nuevo Stage
                Stage modificarStage = new Stage();
                modificarStage.setScene(new Scene(root));
                modificarStage.setWidth(600);
                modificarStage.setHeight(600);
                modificarStage.setTitle("Modificar Habitación");
                modificarStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta que se cierre esta
                modificarStage.showAndWait();

                // Actualizar la lista de habitaciones después de que se haya modificado la habitación
                actualizarListaHabitaciones();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Hubo un problema al abrir la ventana de modificación.");
            }
        } else {
            // Si no se ha seleccionado una habitación, mostrar una alerta
            mostrarAlerta("Selección requerida", "Por favor, selecciona una habitación para modificar.");
        }
    }


    // Metodo para ver los detalles de una habitación seleccionada
    // Metodo para ver los detalles de una habitación seleccionada
    @FXML
    private void verDetallesHabitacion(ActionEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();

        if (habitacionSeleccionada != null) {
            // Crear la ventana para los detalles
            Stage detallesStage = new Stage();
            detallesStage.setHeight(400);
            detallesStage.setWidth(600);
            detallesStage.setTitle("Detalles de habitación");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(20));

            // Detalles comunes
            Label nroLabel = new Label("Nro habitación: " + habitacionSeleccionada.getNumero());
            Label tipoLabel = new Label("Tipo de habitación: " + habitacionSeleccionada.getTipo());
            Label capLabel = new Label("Capacidad: " + habitacionSeleccionada.getCapacidad());
            vbox.getChildren().addAll(nroLabel, tipoLabel, capLabel);

            // Mostrar lista de camas
            if (!habitacionSeleccionada.getCamas().isEmpty()) {
                Label camasLabel = new Label("Camas: " + String.join(", ", habitacionSeleccionada.getCamas()));
                vbox.getChildren().add(camasLabel);
            }

            // Si es un Apartamento, agregar detalles específicos
            if (habitacionSeleccionada instanceof Apartamento) {
                Apartamento apartamento = (Apartamento) habitacionSeleccionada;

                // Detalles específicos para Apartamento
                Label ambientesLabel = new Label("Ambientes: " + apartamento.getAmbientes());
                Label cocinaLabel = new Label("Cocina: " + (apartamento.isCocina() ? "Sí" : "No"));
                vbox.getChildren().addAll(ambientesLabel, cocinaLabel);
            }
            // Si es una Suite, agregar detalles específicos
            else if (habitacionSeleccionada instanceof Suite) {
                Suite suite = (Suite) habitacionSeleccionada;

                // Detalles específicos para Suite
                Label balconLabel = new Label("Balcon: " + (suite.isBalcon() ? "Sí" : "No"));
                Label comedorLabel = new Label("Comedor: " + (suite.isComedor() ? "Sí" : "No"));
                vbox.getChildren().addAll(balconLabel, comedorLabel);
            }
            // Si es una Presidencial, agregar detalles específicos
            else if (habitacionSeleccionada instanceof Presidencial) {
                Presidencial presidencial = (Presidencial) habitacionSeleccionada;

                // Detalles específicos para Presidencial
                Label adicionalesLabel = new Label("Adicionales: " + String.join(", ", presidencial.getAdicionales()));
                Label dimensionLabel = new Label("Dimensión: " + presidencial.getDimension() + " m²");
                vbox.getChildren().addAll(adicionalesLabel, dimensionLabel);
            }

            // Agregar detalles comunes para todos los tipos de habitación
            Label estadoLabel = new Label("Estado: " + habitacionSeleccionada.getEstado());
            vbox.getChildren().add(estadoLabel);

            // Botón para cerrar
            Button cerrarButton = new Button("Cerrar");
            cerrarButton.setOnAction(e -> detallesStage.close());
            vbox.getChildren().add(cerrarButton);

            // Crear y mostrar la ventana
            Scene scene = new Scene(vbox, 300, 350);
            detallesStage.setScene(scene);
            detallesStage.initModality(Modality.APPLICATION_MODAL);
            detallesStage.showAndWait();
        } else {
            mostrarAlerta("Por favor, selecciona una habitación para ver los detalles.");
        }
    }

    @FXML
    private void eliminarHabitacion() {
        // Obtener la habitación seleccionada en la tabla
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();

        if (habitacionSeleccionada != null) {
            // Mostrar alerta de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Está seguro de eliminar esta habitación?");
            alert.setContentText("Esta acción no se puede deshacer.");

            // Si el administrador acepta
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Llamar al servicio para eliminar la habitación
                    boolean eliminada = gestionHabitaciones.eliminar(valueOf(habitacionSeleccionada.getNumero()));

                    if (eliminada) {
                        // Mostrar una alerta de éxito
                        mostrarAlerta("Éxito", "La habitación ha sido eliminada correctamente.");
                        // Actualizar la lista de habitaciones
                        cargarHabitaciones();
                    } else {
                        // Mostrar una alerta de error si no se pudo eliminar
                        mostrarAlerta("Error", "Hubo un problema al eliminar la habitación.");
                    }
                }
            });
        } else {
            // Si no se seleccionó ninguna habitación
            mostrarAlerta("Selección requerida", "Por favor, seleccione una habitación para eliminar.");
        }
    }


    @FXML
    private void filtrarHabitacionesPorTipo() {
        // Desmarcar el CheckBox de disponibilidad cada vez que cambies el tipo
        disponibleCheckBox.setSelected(false);

        String filtroTipo = tipoHabitacionComboBox.getValue(); // Obtener el valor seleccionado en el ComboBox de tipo de habitación
        String filtroId = idHabitacionField.getText().toLowerCase(); // Obtener el texto del filtro de ID de habitación

        // Filtrar la lista original de habitaciones
        ObservableList<Habitacion> habitacionesFiltradas = FXCollections.observableArrayList();

        for (Habitacion habitacion : habitacionesOriginales) {
            // Filtrar primero por tipo, si es "Todos", mostrar todos los tipos
            boolean coincideConTipo = "Todos".equals(filtroTipo) || habitacion.getTipo().toString().equalsIgnoreCase(filtroTipo);
            boolean coincideConId = valueOf(habitacion.getNumero()).contains(filtroId);

            // Si la habitación cumple con ambas condiciones (coincide con tipo y ID), la agregamos a la lista filtrada
            if (coincideConTipo && coincideConId) {
                habitacionesFiltradas.add(habitacion);
            }
        }

        // Actualizar la tabla con las habitaciones filtradas
        tablaHabitaciones.setItems(habitacionesFiltradas);
    }

//    public void filtradoDisponibles(){
//        tablaHabitaciones.
//    }

    @FXML
    private void filtrarHabitacionesPorId() {
        // Desmarcar el CheckBox de disponibilidad cada vez que cambies el ID
        disponibleCheckBox.setSelected(false);

        String filtroId = idHabitacionField.getText().toLowerCase(); // Obtener el texto del filtro de ID de habitación
        String filtroTipo = tipoHabitacionComboBox.getValue(); // Obtener el valor seleccionado en el ComboBox de tipo de habitación

        // Filtrar la lista original de habitaciones
        ObservableList<Habitacion> habitacionesFiltradas = FXCollections.observableArrayList();

        for (Habitacion habitacion : habitacionesOriginales) {
            // Filtrar primero por tipo, si es "Todos", mostrar todos los tipos
            boolean coincideConTipo = "Todos".equals(filtroTipo) || habitacion.getTipo().toString().equalsIgnoreCase(filtroTipo);

            // Filtrar por ID de habitación
            boolean coincideConId = valueOf(habitacion.getNumero()).contains(filtroId);

            // Si la habitación cumple con ambas condiciones (coincide con tipo y ID), la agregamos a la lista filtrada
            if (coincideConTipo && coincideConId) {
                habitacionesFiltradas.add(habitacion);
            }
        }

        // Actualizar la tabla con las habitaciones filtradas
        tablaHabitaciones.setItems(habitacionesFiltradas);
    }

    @FXML
    private void filtrarHabitaciones() {
        String filtroId = idHabitacionField.getText();  // Filtro por ID
        String tipoHabitacion = tipoHabitacionComboBox.getValue();  // Filtro por tipo de habitación
        boolean disponible = disponibleCheckBox.isSelected();  // Filtro por disponibilidad

        // ObservableList para almacenar las habitaciones filtradas
        ObservableList<Habitacion> habitacionesFiltradas = FXCollections.observableArrayList();

        // Filtrar por ID y tipo de habitación primero
        for (Habitacion habitacion : habitacionesOriginales) {
            boolean idCoincide = filtroId.isEmpty() || valueOf(habitacion.getNumero()).contains(filtroId);
            boolean tipoCoincide = "Todos".equals(tipoHabitacion) || habitacion.getTipo().equalsIgnoreCase(tipoHabitacion);

            // Aplicar la lógica de disponibilidad después de filtrar por ID y tipo
            boolean disponibilidadCoincide = !disponible || habitacion.getEstado() == EstadoHabitacion.DISPONIBLE;

            // Si todas las condiciones coinciden, añadimos la habitación a la lista filtrada
            if (idCoincide && tipoCoincide && disponibilidadCoincide) {
                habitacionesFiltradas.add(habitacion);
            }
        }

        // Actualizar la tabla con las habitaciones filtradas
        tablaHabitaciones.setItems(habitacionesFiltradas);
    }


    // Metodo para volver al menú anterior
    @FXML
    private void volverAlMenuAdmin(ActionEvent event) {
        volverAEscenaAnterior(event,previousScene);
    }
}