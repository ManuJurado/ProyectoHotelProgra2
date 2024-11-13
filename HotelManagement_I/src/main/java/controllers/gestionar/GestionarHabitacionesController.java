package controllers.gestionar;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class GestionarHabitacionesController extends BaseController {

    @FXML
    private TableView tablaHabitaciones;
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

    // Metodo para crear nueva habitación
    @FXML
    private void crearNuevaHabitacion() {
        System.out.println("Crear nueva habitación");
        // Aquí iría la lógica para crear una nueva habitación
    }

    // Metodo para modificar una habitación seleccionada
    @FXML
    private void modificarHabitacion() {
        System.out.println("Modificar habitación");
        // Aquí iría la lógica para modificar la habitación seleccionada
    }

    // Metodo para ver los detalles de una habitación seleccionada
    @FXML
    private void verDetallesHabitacion() {
        System.out.println("Ver detalles de la habitación");
        // Aquí iría la lógica para ver los detalles de la habitación seleccionada
    }

    // Metodo para eliminar una habitación seleccionada
    @FXML
    private void eliminarHabitacion() {
        System.out.println("Eliminar habitación");
        // Aquí iría la lógica para eliminar la habitación seleccionada
    }

    // Metodo para filtrar las habitaciones por ID
    @FXML
    private void filtrarHabitacionesPorId() {
        String idHabitacion = idHabitacionField.getText();
        System.out.println("Filtrar habitaciones por ID: " + idHabitacion);
        // Aquí iría la lógica para filtrar las habitaciones por ID
    }

    // Metodo para filtrar las habitaciones por tipo
    @FXML
    private void filtrarHabitacionesPorTipo() {
        String tipoHabitacion = tipoHabitacionComboBox.getValue();
        System.out.println("Filtrar habitaciones por tipo: " + tipoHabitacion);
        // Aquí iría la lógica para filtrar las habitaciones por tipo
    }

    // Metodo para volver al menú anterior
    @FXML
    private void volverAlMenuAdmin() {
        Stage stage = (Stage) crearNuevaHabitacionButton.getScene().getWindow();
        stage.close();
    }
}