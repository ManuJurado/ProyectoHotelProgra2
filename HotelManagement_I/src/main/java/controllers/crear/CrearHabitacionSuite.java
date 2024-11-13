package controllers.crear;

import controllers.BaseController;
import controllers.gestionar.GestionarHabitacionesController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Habitacion.Suite;
import services.GestionHabitaciones;

import java.util.ArrayList;
import java.util.List;

public class CrearHabitacionSuite extends BaseController {

    @FXML private ComboBox<String> comboBoxCama1;
    @FXML private ComboBox<String> comboBoxCama2;
    @FXML private ComboBox<String> comboBoxCama3;
    @FXML private ComboBox<String> comboBoxCama4;
    @FXML private CheckBox comedorCheckBox;
    @FXML private CheckBox balconCheckBox;

    private Scene previousScene;
    private String tipoHabitacionSeleccionado;

    // Setters
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setTipoHabitacionSeleccionado(String tipoHabitacionSeleccionado) {
        this.tipoHabitacionSeleccionado = tipoHabitacionSeleccionado;
    }

    @FXML
    public void initialize() {
        // Inicializar ComboBox de camas con las opciones disponibles
        List<String> tiposCama = List.of("No seleccionado", "Simple", "Doble", "QueenSize", "KingSize");

        comboBoxCama1.getItems().addAll(tiposCama);
        comboBoxCama2.getItems().addAll(tiposCama);
        comboBoxCama3.getItems().addAll(tiposCama);
        comboBoxCama4.getItems().addAll(tiposCama);

        // Establecer "No seleccionado" por defecto
        comboBoxCama1.setValue("No seleccionado");
        comboBoxCama2.setValue("No seleccionado");
        comboBoxCama3.setValue("No seleccionado");
        comboBoxCama4.setValue("No seleccionado");

        // Inhabilitar las camas 2, 3 y 4 inicialmente
        comboBoxCama2.setDisable(true);
        comboBoxCama3.setDisable(true);
        comboBoxCama4.setDisable(true);

        // Listener para el ComboBox de la Cama 1
        comboBoxCama1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                // Si Cama 1 no está seleccionada, deshabilitar todas las siguientes
                comboBoxCama2.setDisable(true);
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                // Si Cama 1 está seleccionada, habilitar Cama 2
                comboBoxCama2.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 2
        comboBoxCama2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                // Si Cama 2 no está seleccionada, deshabilitar las siguientes
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                // Si Cama 2 está seleccionada, habilitar Cama 3
                comboBoxCama3.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 3
        comboBoxCama3.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                // Si Cama 3 no está seleccionada, deshabilitar la siguiente
                comboBoxCama4.setDisable(true);
            } else {
                // Si Cama 3 está seleccionada, habilitar Cama 4
                comboBoxCama4.setDisable(false);
            }
        });
    }

    @FXML
    public void guardarHabitacionSuite() {
        List<String> errores = new ArrayList<>();  // Lista para acumular mensajes de error

        try {
            // Crear una lista para las camas seleccionadas
            List<String> camasSeleccionadas = new ArrayList<>();
            int capacidad = 0;

            // Lógica para agregar camas y calcular la capacidad
            for (ComboBox<String> comboBox : List.of(comboBoxCama1, comboBoxCama2, comboBoxCama3, comboBoxCama4)) {
                String camaSeleccionada = comboBox.getValue();
                if (camaSeleccionada != null && !camaSeleccionada.equals("No seleccionado")) {
                    camasSeleccionadas.add(camaSeleccionada);
                    capacidad += obtenerValorCama(camaSeleccionada); // Se suma el valor asociado a cada tipo de cama
                }
            }

            // Validación para asegurar que no haya más de 4 camas
            if (capacidad > 4) {
                errores.add("Capacidad maxima excedida, reducir cantidad o tamaño de camas");
            }

            // Validación de las camas seleccionadas
            if (camasSeleccionadas.isEmpty()) {
                errores.add("Debe seleccionar al menos una cama.");
            }

            // Validar el campo de comedor y balcón
            boolean comedor = comedorCheckBox.isSelected();
            boolean balcon = balconCheckBox.isSelected();

            // Si hay errores, mostramos la alerta
            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores)); // Mostrar los errores acumulados
            } else {
                // Si no hay errores, pasamos los valores al metodo de creación
                String detalleEstado = "Disponible"; // Establecer estado por defecto como "Disponible"
                boolean disponible = true;  // Establecemos el estado de disponibilidad (true)

                // Llamar al metodo para crear la habitación Suite
                GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                gestionHabitacion.crearHabitacionSuite(capacidad, camasSeleccionadas, disponible, detalleEstado, balcon, comedor);

                // Llamar a actualizarListaHabitaciones() en el controlador de la escena anterior
                if (previousScene != null && previousScene.getUserData() instanceof GestionarHabitacionesController) {
                    GestionarHabitacionesController gestionarHabitacionesController = (GestionarHabitacionesController) previousScene.getUserData();
                    gestionarHabitacionesController.actualizarListaHabitaciones(); // Actualiza la lista de habitaciones si es necesario
                }

                mostrarMensajeTemporal("Habitación Guardada correctamente");

                // Cerrar la ventana
                cerrarVentana();
            }
        } catch (IllegalArgumentException e) {
            // Mostrar error si el setter de camas lanza una excepción
            showAlert(e.getMessage());
        }
    }


    // Metodo de validación general para los campos
    private void validarCampo(String value, String campo, List<String> errores) {
        if (value == null || value.trim().isEmpty()) {
            errores.add(campo + " es obligatorio.");
        }
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

    // Metodo para cerrar la ventana
    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) comboBoxCama1.getScene().getWindow();
        stage.close();
    }

    // Metodo para mostrar un mensaje temporal
    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Mostrar el alert
        alert.show();

        // Usamos un Timeline para cerrar el alert después de 2 segundos (2000 milisegundos)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    // Metodo para mostrar alertas con errores
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
