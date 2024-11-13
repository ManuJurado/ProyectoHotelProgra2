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

public class CrearHabitacionSimple extends BaseController {

    @FXML
    private Button btnCerrar;

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

    }

    @FXML
    public void guardarHabitacionSimple() {
        List<String> errores = new ArrayList<>();  // Lista para acumular mensajes de error

        try {
            // Crear una lista para las camas seleccionadas
            List<String> camasSeleccionadas = new ArrayList<>();
            int capacidad = 1;

            // Validación de las camas seleccionadas
            camasSeleccionadas.add("Simple");

            // Si hay errores, mostramos la alerta
            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores)); // Mostrar los errores acumulados
            } else {
                // Si no hay errores, pasamos los valores al metodo de creación
                String detalleEstado = "Disponible"; // Establecer estado por defecto como "Disponible"
                boolean disponible = true;  // Establecemos el estado de disponibilidad (true)

                // Crear la habitación Doble usando el metodo
                GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                gestionHabitacion.crearHabitacionIndividual(camasSeleccionadas, disponible, detalleEstado);

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

    @FXML
    public void cerrarVentana() {
        // Obtener el Stage actual a partir de btnCerrar (o cualquier nodo en la escena)
        Stage stage = (Stage) btnCerrar.getScene().getWindow(); // Usamos btnCerrar o cualquier otro nodo presente en la escena
        stage.close();  // Cerrar la ventana
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
