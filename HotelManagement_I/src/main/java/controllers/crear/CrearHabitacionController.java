package controllers.crear;

import controllers.BaseController;
import controllers.gestionar.GestionarHabitacionesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GestionHabitaciones;

public class CrearHabitacionController extends BaseController {

    private GestionHabitaciones gestionHabitaciones;

    @FXML
    private TextField tipoHabitacionField; // Tipo de habitación
    @FXML
    private TextField estadoField; // Estado de la habitación
    @FXML
    private TextField metrosCuadradosField; // Metros cuadrados
    @FXML
    private TextField cantidadCamasField; // Cantidad de camas
    @FXML
    private TextField nroHabitacionField; // Cambié el nombre para evitar confusiones

    private GestionarHabitacionesController gestionarHabitacionesController;

    public void setGestionarHabitaciones(services.GestionHabitaciones gestionHabitaciones) {
        this.gestionHabitaciones = gestionHabitaciones;
    }

    public void setGestionarHabitacionesController(GestionarHabitacionesController gestionarHabitacionesController) {
        this.gestionarHabitacionesController = gestionarHabitacionesController;
    }

    /*@FXML
    private void crearHabitacion(ActionEvent event) {
        String tipoHabitacion = tipoHabitacionField.getText();
        String estado = estadoField.getText();
        int metrosCuadrados = Integer.parseInt(metrosCuadradosField.getText());
        int cantidadCamas = Integer.parseInt(cantidadCamasField.getText());
        Integer nroHabitacion = Integer.parseInt(nroHabitacionField.getText()); // Asegúrate de usar el nombre correcto del campo

        Habitacion nuevaHabitacion = new Habitacion(tipoHabitacion, estado, metrosCuadrados, cantidadCamas);
        gestionarHabitaciones.agregarHabitacion(nuevaHabitacion);

        mostrarAlerta("Éxito", "Habitación creada con éxito.");
    }*/

    /*@FXML
    private void guardarHabitacion(ActionEvent event) {
        // Crear una nueva habitación usando los datos del formulario
        Habitacion nuevaHabitacion = new Habitacion(
                tipoHabitacionField.getText(), // Tipo de habitación
                estadoField.getText(), // Estado
                Integer.parseInt(metrosCuadradosField.getText()), // Metros cuadrados
                Integer.parseInt(cantidadCamasField.getText())// Cantidad de camas
        );

        // Llama al método para agregar la nueva habitación
        gestionarHabitaciones.agregarHabitacion(nuevaHabitacion);

        // Aquí llamamos directamente al método de actualización de la tabla
        if (gestionarHabitacionesController != null) {
            gestionarHabitacionesController.cargarHabitaciones();
        }

        // Actualiza la tabla en el controlador de gestionarHabitaciones
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage parentStage = (Stage) stage.getOwner(); // Obtener el escenario padre

        if (parentStage != null) {
            GestionarHabitacionesController gestionarHabitacionesController = (GestionarHabitacionesController) parentStage.getScene().getUserData();
            if (gestionarHabitacionesController != null) {
                gestionarHabitacionesController.cargarHabitaciones(); // Cambia a cargarHabitaciones()
            }
        }

        // Muestra un mensaje de éxito y cierra la ventana
        mostrarAlerta("Éxito", "La habitación fue creada con éxito.");
        stage.close(); // Cerrar la ventana actual
    }*/


    @FXML
    private void volverAlMenu(ActionEvent event) {
        // Aquí puedes implementar la lógica para volver al menú anterior
        // Por ejemplo, puedes usar el método cambiarEscena similar al anterior
        cambiarEscena("/views/gestionarHabitaciones.fxml", "Gestión de Habitaciones", (Node) event.getSource());
    }

    @FXML
    private void cerrar(ActionEvent event) {
        // Cerrar la ventana actual después de guardar
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Metodo para mostrar una alerta con los errores
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
