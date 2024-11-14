package controllers.crear;

import controllers.BaseController;
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
import java.util.Map;
import java.util.stream.Collectors;

public class CrearReservaController extends BaseController {

    @FXML
    private DatePicker datePickerFechaEntrada, datePickerFechaSalida;
    @FXML
    private ChoiceBox<Integer> choiceBoxCantidadPersonas;
    @FXML
    private VBox vboxPasajeros;
    @FXML
    private TableView<Habitacion> tableViewHabitaciones;
    @FXML
    private CheckBox checkBoxWiFi, checkBoxDesayuno, checkBoxPiscina, checkBoxSpa, checkBoxEstacionamiento;

    private List<String> serviciosAdicionalesSeleccionados = new ArrayList<>();

    private void configurarCheckBoxesServiciosAdicionales() {
        // Mapeo de cada CheckBox con su nombre de servicio correspondiente
        Map<CheckBox, String> serviciosMap = Map.of(
                checkBoxWiFi, "WiFi",
                checkBoxDesayuno, "Desayuno",
                checkBoxPiscina, "Piscina",
                checkBoxSpa, "Spa",
                checkBoxEstacionamiento, "Estacionamiento"
        );

        // Configuración de cada CheckBox
        for (Map.Entry<CheckBox, String> entry : serviciosMap.entrySet()) {
            CheckBox checkBox = entry.getKey();
            String servicioNombre = entry.getValue();

            // Listener para agregar el servicio a la lista si está seleccionado
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    if (!serviciosAdicionalesSeleccionados.contains(servicioNombre)) {
                        serviciosAdicionalesSeleccionados.add(servicioNombre);
                    }
                } else {
                    serviciosAdicionalesSeleccionados.remove(servicioNombre);
                }
            });
        }
    }

    private List<HBox> camposPasajeros = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    private Habitacion habitacionSeleccionada;

    private GestionHabitaciones gestionHabitaciones = GestionHabitaciones.getInstancia("habitaciones.json");
    private GestionReservas gestionReservas = GestionReservas.getInstancia("reservas.json");
    private Reserva reservaParaModificar;

    @FXML
    public void initialize() {
        setTextFieldLimit(datePickerFechaSalida.getEditor(),10);
        setTextFieldLimit(datePickerFechaEntrada.getEditor(),10);

        configurarCheckBoxesServiciosAdicionales();
        configurarChoiceBoxCantidadPersonas();
        configurarDatePicker();
        cargarHabitacionesDisponibles();
        configurarTableViewHabitaciones();
        actualizarCamposPasajeros(1);

        if (reservaParaModificar == null) {
            reservaParaModificar = new Reserva();
        }
    }

    private void cargarHabitacionesDisponibles() {
        List<Habitacion> habitacionesDisponibles = gestionHabitaciones.getHabitaciones().stream()
                .filter(habitacion -> "disponible".equalsIgnoreCase(habitacion.getEstado().toString()))
                .collect(Collectors.toList());

        ObservableList<Habitacion> habitacionesObservable = FXCollections.observableArrayList(habitacionesDisponibles);
        tableViewHabitaciones.setItems(habitacionesObservable);
    }

    private void configurarTableViewHabitaciones() {
        TableColumn<Habitacion, String> numeroColumna = new TableColumn<>("Número");
        numeroColumna.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<Habitacion, String> tipoColumna = new TableColumn<>("Tipo");
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Habitacion, String> estadoColumna = new TableColumn<>("Estado");
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableViewHabitaciones.getColumns().addAll(numeroColumna, tipoColumna, estadoColumna);

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
            setTextFieldLimit(tfNombre,20);

            TextField tfApellido = new TextField();
            tfApellido.setPromptText("Apellido");
            setTextFieldLimit(tfApellido,20);

            TextField tfDni = new TextField();
            setTextFieldLimit(tfDni,10);
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

    @FXML
    private void onConfirmarReserva() throws UsuarioNoEncontradoException, FechaInvalidaException {
        LocalDate fechaEntrada = datePickerFechaEntrada.getValue();
        LocalDate fechaSalida = datePickerFechaSalida.getValue();
        int cantidadPersonas = choiceBoxCantidadPersonas.getValue();

        if (fechaEntrada == null || fechaSalida == null || cantidadPersonas <= 0) {
            mostrarAlerta("Advertencia", "Por favor, completa todos los campos requeridos.");
            return;
        }

        try {
            gestionReservas.crearReserva(Sesion.getUsuarioLogueado().getNombre(), String.valueOf(habitacionSeleccionada.getNumero()),fechaEntrada,fechaSalida,"Disfruta la vida que es una sola",cantidadPersonas,serviciosAdicionalesSeleccionados,obtenerPasajeros());
            mostrarAlerta("Éxito", "La reserva se ha creado exitosamente.");
            Stage stage = (Stage) choiceBoxCantidadPersonas.getScene().getWindow();
            stage.close();
        }catch (RuntimeException e){
            showAlert(e.getMessage());
        }


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

    // Metodo para mostrar una alerta con los errores acumulados
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
