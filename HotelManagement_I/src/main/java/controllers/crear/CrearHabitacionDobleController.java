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
import services.GestionHabitaciones;

import java.util.ArrayList;
import java.util.List;

public class CrearHabitacionDobleController extends BaseController {

    @FXML private ComboBox<String> comboBoxCama1;
    @FXML private ComboBox<String> comboBoxCama2;

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

        // Establecer "No seleccionado" por defecto
        comboBoxCama1.setValue("No seleccionado");
        comboBoxCama2.setValue("No seleccionado");

        // Inhabilitar la cama 2 por defecto
        comboBoxCama2.setDisable(true);

        // Listener para el ComboBox de la Cama 1
        comboBoxCama1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("No seleccionado")) {
                comboBoxCama2.setDisable(false);  // Habilitar Cama 2
            } else {
                comboBoxCama2.setDisable(true);   // Deshabilitar Cama 2 si Cama 1 está vacía
                comboBoxCama2.setValue("No seleccionado"); // Restablecer el valor de la cama 2
            }
        });
    }

    @FXML
    public void guardarHabitacionDoble() {
        List<String> errores = new ArrayList<>();  // Lista para acumular mensajes de error

        try {
            // Crear una lista para las camas seleccionadas
            List<String> camasSeleccionadas = new ArrayList<>();
            int capacidad = 0;

            // Lógica para agregar camas y calcular la capacidad
            String cama1 = comboBoxCama1.getValue();
            String cama2 = comboBoxCama2.getValue();

            // Si la cama 1 es válida (no "No seleccionado")
            if (cama1 != null && !cama1.equals("No seleccionado")) {
                camasSeleccionadas.add(cama1);
                capacidad += obtenerValorCama(cama1); // Se suma el valor asociado a cada tipo de cama
            }

            // Si la cama 2 es válida (no "No seleccionado")
            if (cama2 != null && !cama2.equals("No seleccionado")) {
                camasSeleccionadas.add(cama2);
                capacidad += obtenerValorCama(cama2); // Se suma el valor asociado a cada tipo de cama
            }

            // Validación para asegurar que no haya más de 2 camas
            if (capacidad > 2) {
                errores.add("No puede haber más de 2 camas en total o una doble.");
            }

            // Validación de las camas seleccionadas
            if (camasSeleccionadas.isEmpty()) {
                errores.add("Debe seleccionar al menos una cama.");
            }

            // Si hay errores, mostramos la alerta
            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores)); // Mostrar los errores acumulados
            } else {
                // Si no hay errores, pasamos los valores al metodo de creación
                String detalleEstado = "Disponible"; // Establecer estado por defecto como "Disponible"
                boolean disponible = true;  // Establecemos el estado de disponibilidad (true)

                // Crear la habitación Doble usando el metodo
                GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                gestionHabitacion.crearHabitacionDoble(camasSeleccionadas, disponible, detalleEstado);

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
