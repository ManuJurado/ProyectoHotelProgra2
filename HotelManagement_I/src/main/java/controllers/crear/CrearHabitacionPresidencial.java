package controllers.crear;

import controllers.BaseController;
import controllers.gestionar.GestionarHabitacionesController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.GestionHabitaciones;

import java.util.ArrayList;
import java.util.List;

public class CrearHabitacionPresidencial extends BaseController {
    @FXML private CheckBox mesaPoolCheckBox;
    @FXML private CheckBox jacuzziCheckBox;
    @FXML private CheckBox cineCheckBox;
    @FXML private CheckBox entretenimientoCheckBox;
    @FXML private CheckBox terrazaCheckBox;
    @FXML private CheckBox saunaCheckBox;

    @FXML private TextField dimensionField;

    @FXML private ComboBox<String> comboBoxCama1;
    @FXML private ComboBox<String> comboBoxCama2;
    @FXML private ComboBox<String> comboBoxCama3;
    @FXML private ComboBox<String> comboBoxCama4;

    private Scene previousScene;
    private String tipoHabitacionSeleccionado;

    @FXML
    public void initialize() {
        // Inicializar ComboBox de camas con las opciones disponibles
        List<String> tiposCama = List.of("No seleccionado", "Simple", "Doble", "QueenSize", "KingSize");
        comboBoxCama1.getItems().addAll(tiposCama);
        comboBoxCama2.getItems().addAll(tiposCama);
        comboBoxCama3.getItems().addAll(tiposCama);
        comboBoxCama4.getItems().addAll(tiposCama);

        setTextFieldLimit(dimensionField,3);

        comboBoxCama1.setValue("No seleccionado");
        comboBoxCama2.setValue("No seleccionado");
        comboBoxCama3.setValue("No seleccionado");
        comboBoxCama4.setValue("No seleccionado");

        comboBoxCama2.setDisable(true);
        comboBoxCama3.setDisable(true);
        comboBoxCama4.setDisable(true);

        // Listener para el ComboBox de la Cama 1
        comboBoxCama1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama2.setDisable(true);
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama2.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 2
        comboBoxCama2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama3.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 3
        comboBoxCama3.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama4.setDisable(false);
            }
        });
    }

    // Metodo para guardar la habitación presidencial
    @FXML
    public void guardarHabitacionPresidencial() {
        List<String> errores = new ArrayList<>();

        try {
            // Crear una lista para las camas seleccionadas
            List<String> camasSeleccionadas = new ArrayList<>();
            int capacidad = 0;

            for (ComboBox<String> comboBox : List.of(comboBoxCama1, comboBoxCama2, comboBoxCama3, comboBoxCama4)) {
                String camaSeleccionada = comboBox.getValue();
                if (camaSeleccionada != null && !camaSeleccionada.equals("No seleccionado")) {
                    camasSeleccionadas.add(camaSeleccionada);
                    capacidad += obtenerValorCama(camaSeleccionada);
                }
            }

            if (capacidad > 4) {
                errores.add("Capacidad máxima excedida, reducir cantidad o tamaño de camas.");
            }

            if (camasSeleccionadas.isEmpty()) {
                errores.add("Debe seleccionar al menos una cama.");
            }

            List<String> adicionalesSeleccionados = new ArrayList<>();
            // Revisar los CheckBox de adicionales
            if (mesaPoolCheckBox.isSelected()) adicionalesSeleccionados.add("Mesa de pool");
            if (jacuzziCheckBox.isSelected()) adicionalesSeleccionados.add("Jacuzzi");
            if (cineCheckBox.isSelected()) adicionalesSeleccionados.add("Cine");
            if (entretenimientoCheckBox.isSelected()) adicionalesSeleccionados.add("Servicios de entretenimiento");
            if (terrazaCheckBox.isSelected()) adicionalesSeleccionados.add("Terraza");
            if (saunaCheckBox.isSelected()) adicionalesSeleccionados.add("Sauna");

            double dimensionHabitacion = 0;
            try {
                dimensionHabitacion = Double.parseDouble(dimensionField.getText());
            } catch (NumberFormatException e) {
                errores.add("La dimensión debe ser un número válido.");
            }

            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores));
            } else {
                String detalleEstado = "Disponible";
                boolean disponible = true;

                // Crear la habitación presidencial
                GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                gestionHabitacion.crearHabitacionPresidencial(capacidad, camasSeleccionadas, disponible, detalleEstado, adicionalesSeleccionados, dimensionHabitacion);

                // Actualizar la lista de habitaciones en el controlador anterior
                if (previousScene != null && previousScene.getUserData() instanceof GestionarHabitacionesController) {
                    GestionarHabitacionesController gestionarHabitacionesController = (GestionarHabitacionesController) previousScene.getUserData();
                    gestionarHabitacionesController.actualizarListaHabitaciones();
                }

                mostrarMensajeTemporal("Habitación Guardada correctamente");

                cerrarVentana();
            }
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    // Metodo para cerrar la ventana
    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) comboBoxCama1.getScene().getWindow();
        stage.close();
    }

    // Mostrar mensaje temporal
    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    // Mostrar alertas de errores
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Metodo para obtener el valor asociado a cada tipo de cama
    private int obtenerValorCama(String tipoCama) {
        switch (tipoCama) {
            case "Simple":
                return 1;
            case "Doble":
            case "QueenSize":
            case "KingSize":
                return 2;
            default:
                return 0;
        }
    }
}
