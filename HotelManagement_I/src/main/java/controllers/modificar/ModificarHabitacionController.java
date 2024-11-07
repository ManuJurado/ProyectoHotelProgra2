package controllers.modificar;

import controllers.BaseController;
import controllers.gestionar.GestionarHabitacionesController; // Controlador para gestionar habitaciones
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Habitacion.Habitacion;

public class ModificarHabitacionController extends BaseController {

    @FXML
    private ComboBox<String> tipoHabitacionField; // Campo para el tipo de habitación
    @FXML
    private ComboBox<String> estadoField; // Campo para el estado de la habitación
    @FXML
    private TextField metrosCuadradosField; // Campo para los metros cuadrados
    @FXML
    private ComboBox<Integer> cantidadCamasField; // Campo para la cantidad de camas

    private Habitacion habitacionOriginal; // Habitación que vamos a modificar
    private GestionarHabitacionesController gestionarHabitacionesController; // Controlador principal

    @FXML
    public void initialize() {
        // Inicialización adicional si es necesario
    }

/*    // Método para configurar la habitación y el controlador principal
    public void setHabitacion(Habitacion habitacion, GestionarHabitacionesController gestionarHabitacionesController) {
        this.habitacionOriginal = habitacion;
        this.gestionarHabitacionesController = gestionarHabitacionesController;

        // Llenar los campos con la información actual de la habitación
        tipoHabitacionField.getItems().addAll("Simple","Doble","Suite");
        estadoField.getItems().addAll("Disponible","No Disponible");
        metrosCuadradosField.setText(String.valueOf(habitacion.getMetrosCuadrados()));
        cantidadCamasField.getItems().addAll(2,5,4,1);

        // Establecer valores seleccionados en los ComboBox
        tipoHabitacionField.setValue(habitacion.getTipoHabitacion());
        estadoField.setValue(habitacion.getEstado());
        cantidadCamasField.setValue(habitacion.getCantidadCamas());
    }*/

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

/*    @FXML
    private void guardarCambios(ActionEvent event) {
        // Obtener los valores seleccionados en los ComboBox
        String nuevoTipo = tipoHabitacionField.getValue(); // Cambiado de getText() a getValue()
        String nuevoEstado = estadoField.getValue(); // Cambiado de getText() a getValue()
        int nuevosMetrosCuadrados = Integer.parseInt(metrosCuadradosField.getText());
        Integer nuevaCantidadCamas = cantidadCamasField.getValue(); // Cambiado de getText() a getValue()

        if (nuevoTipo != null && nuevoEstado != null && nuevaCantidadCamas != null) { // Comprobar que los campos no sean nulos
            // Actualiza la habitación original
            habitacionOriginal.modificarHabitacion(nuevoTipo, nuevoEstado, nuevosMetrosCuadrados, nuevaCantidadCamas);

            // Llama al método para actualizar la lista de habitaciones en el controlador principal
            gestionarHabitacionesController.actualizarListaHabitaciones();

            mostrarAlerta("Éxito", "Cambios guardados correctamente.");
            cerrarVentana(event);
        } else {
            mostrarAlerta("Advertencia", "Por favor, completa todos los campos.");
        }
    }*/

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
