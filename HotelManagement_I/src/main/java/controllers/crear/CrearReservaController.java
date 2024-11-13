package controllers.crear;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import controllers.BaseController;
import enums.EstadoHabitacion;
import exceptions.FechaInvalidaException;
import exceptions.UsuarioNoEncontradoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Habitacion.Habitacion;
import models.Pasajero;
import models.Reserva;
import services.GestionHabitaciones;
import services.GestionReservas;
import services.Sesion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrearReservaController extends BaseController {

    @FXML
    private DatePicker datePickerFechaEntrada;
    @FXML
    private DatePicker datePickerFechaSalida;
    @FXML
    private ChoiceBox<Integer> choiceBoxCantidadPersonas;
    @FXML
    private VBox vboxPasajeros;
    @FXML
    private TableView<Habitacion> tableViewHabitaciones;
    @FXML
    private ComboBox<String> servicios;
    @FXML
    private ComboBox<String> comboBoxServiciosAdicionales;

    private List<String> serviciosAdicionales = new ArrayList<>();  // Ya tienes esta lista declarada


    private List<HBox> camposPasajeros = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    private Habitacion habitacionSeleccionada;

    // Instancia de GestionHabitaciones para cargar desde JSON
    private GestionHabitaciones gestionHabitaciones = GestionHabitaciones.getInstancia("habitaciones.json");
    private GestionReservas gestionReservas = GestionReservas.getInstancia("reservas.json");
    private Reserva reservaParaModificar;  // Variable para almacenar la reserva que se va a modificar



    @FXML
    public void initialize() {
        configurarChoiceBoxCantidadPersonas();
        configurarDatePicker();
        cargarHabitacionesDisponibles();
        configurarTableViewHabitaciones();
        actualizarCamposPasajeros(1);


        // Inicializa la reserva como nueva si no se está modificando una
        if (reservaParaModificar == null) {
            reservaParaModificar = new Reserva();
        }
    }

    private void configurarComboBoxServiciosAdicionales() {
        // Lista de servicios adicionales
        List<String> serviciosDisponibles = List.of("WiFi", "Desayuno", "Piscina", "Spa", "Estacionamiento");

        // Asignar la lista de servicios al ComboBox
        comboBoxServiciosAdicionales.setItems(FXCollections.observableArrayList(serviciosDisponibles));

        // Establecer una acción cuando se seleccione un servicio
        comboBoxServiciosAdicionales.setOnAction(event -> {
            String servicioSeleccionado = comboBoxServiciosAdicionales.getSelectionModel().getSelectedItem();
            if (servicioSeleccionado != null && !serviciosAdicionales.contains(servicioSeleccionado)) {
                serviciosAdicionales.add(servicioSeleccionado);
            }
        });
    }

    private void cargarHabitacionesDisponibles() {
        // Filtrar habitaciones disponibles y cargarlas en la tabla
        List<Habitacion> habitacionesDisponibles = gestionHabitaciones.getHabitaciones().stream()
                .filter(habitacion -> "disponible".equalsIgnoreCase(habitacion.getEstado().toString()))
                .collect(Collectors.toList());

        ObservableList<Habitacion> habitacionesObservable = FXCollections.observableArrayList(habitacionesDisponibles);
        tableViewHabitaciones.setItems(habitacionesObservable);
    }

    private void configurarTableViewHabitaciones() {
        // Configurar columnas de la tabla
        TableColumn<Habitacion, String> numeroColumna = new TableColumn<>("Número");
        numeroColumna.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<Habitacion, String> tipoColumna = new TableColumn<>("Tipo");
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Habitacion, String> estadoColumna = new TableColumn<>("Estado");
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableViewHabitaciones.getColumns().addAll(numeroColumna, tipoColumna, estadoColumna);

        // Listener para capturar la habitación seleccionada
        tableViewHabitaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                habitacionSeleccionada = newSelection;
            }
        });
    }

    private void configurarChoiceBoxCantidadPersonas() {
        choiceBoxCantidadPersonas.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
        choiceBoxCantidadPersonas.setValue(1);
        choiceBoxCantidadPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            actualizarCamposPasajeros(newVal);
        });
    }

    private void actualizarCamposPasajeros(int cantidad) {
        vboxPasajeros.getChildren().clear();
        camposPasajeros.clear();

        for (int i = 0; i < cantidad; i++) {
            HBox hboxPasajero = new HBox(10);
            TextField tfNombre = new TextField();
            tfNombre.setPromptText("Nombre");

            TextField tfApellido = new TextField();
            tfApellido.setPromptText("Apellido");

            TextField tfDni = new TextField();
            tfDni.setPromptText("DNI");

            hboxPasajero.getChildren().addAll(tfNombre, tfApellido, tfDni);
            vboxPasajeros.getChildren().add(hboxPasajero);
            camposPasajeros.add(hboxPasajero);
        }
    }

    private void configurarDatePicker() {
        datePickerFechaEntrada.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        datePickerFechaSalida.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(datePickerFechaEntrada.getValue().plusDays(1)));
            }
        });

        datePickerFechaEntrada.valueProperty().addListener((obs, oldDate, newDate) -> {
            datePickerFechaSalida.setValue(newDate.plusDays(1));
        });
    }

    // Metodo para cargar los datos de la reserva seleccionada
    public void setReservaParaModificar(Reserva reserva) {
        this.reservaParaModificar = reserva;

        // Cargar los datos de la reserva en los campos
        datePickerFechaEntrada.setValue(reserva.getFechaEntrada());
        datePickerFechaSalida.setValue(reserva.getFechaSalida());
        choiceBoxCantidadPersonas.setValue(reserva.getCantidadPersonas());
        // Aquí puedes agregar los servicios adicionales si es necesario
        // Y cargar la habitación seleccionada
    }

    @FXML
    private void onConfirmarReserva() throws UsuarioNoEncontradoException, FechaInvalidaException {
        LocalDate fechaEntrada = datePickerFechaEntrada.getValue();
        LocalDate fechaSalida = datePickerFechaSalida.getValue();
        int cantidadPersonas = choiceBoxCantidadPersonas.getValue();


        if (fechaEntrada == null || fechaSalida == null || cantidadPersonas <= 0) {
            mostrarAlerta("Advertencia", "Por favor, completa todos los campos requeridos.");
            return;
        }

        // Si es una nueva reserva, asigna los valores directamente
        if (reservaParaModificar == null) {
            reservaParaModificar = new Reserva();  // Aseguramos que la reserva sea una nueva
        }

        // Actualizar la reserva con los nuevos valores
        reservaParaModificar.setPasajeros(obtenerPasajeros());
        reservaParaModificar.setFechaReserva(LocalDate.now());
        reservaParaModificar.setFechaEntrada(fechaEntrada);
        reservaParaModificar.setFechaSalida(fechaSalida);
        reservaParaModificar.setServiciosAdicionales(serviciosAdicionales);
        reservaParaModificar.setComentario("Disfruta la vida que es una sola");
        reservaParaModificar.setCantidadPersonas(cantidadPersonas);
        reservaParaModificar.setEstadoReserva("Reservada");
        reservaParaModificar.setUsuario(Sesion.getUsuarioLogueado());
        reservaParaModificar.setHabitacion(habitacionSeleccionada);


        // Aquí puedes actualizar los otros detalles de la reserva (habitaciones, servicios, etc.)

        // Guardar la reserva (modificada o nueva)
        gestionReservas.guardar(reservaParaModificar);  // Metodo para guardar una nueva reserva

        // Cerrar la ventana
        Stage stage = (Stage) choiceBoxCantidadPersonas.getScene().getWindow();
        stage.close();

        mostrarAlerta("Éxito", "La reserva se ha creado exitosamente.");
    }

    private void configurarServicios() {
        servicios.setItems(FXCollections.observableArrayList("WiFi", "Desayuno", "Piscina", "Spa")); // Ejemplo de servicios
        servicios.setOnAction(event -> {
            String servicioSeleccionado = servicios.getSelectionModel().getSelectedItem();
            if (servicioSeleccionado != null && !serviciosAdicionales.contains(servicioSeleccionado)) {
                serviciosAdicionales.add(servicioSeleccionado);
            }
        });
    }

    private List<Pasajero> obtenerPasajeros() {
        List<Pasajero> listaPasajeros = new ArrayList<>();
        for (HBox hbox : camposPasajeros) {
            TextField nombre = (TextField) hbox.getChildren().get(0);
            TextField apellido = (TextField) hbox.getChildren().get(1);
            TextField dni = (TextField) hbox.getChildren().get(2);
            if (!nombre.getText().isEmpty() && !apellido.getText().isEmpty() && !dni.getText().isEmpty()) {
                listaPasajeros.add(new Pasajero(nombre.getText(), apellido.getText(), dni.getText()));
            }
        }
        return listaPasajeros;
    }

    @FXML
    private void onCancelar() {
        Stage stage = (Stage) choiceBoxCantidadPersonas.getScene().getWindow();
        stage.close();
    }



    private LocalDate obtenerFechaActual() {
        return LocalDate.now();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
