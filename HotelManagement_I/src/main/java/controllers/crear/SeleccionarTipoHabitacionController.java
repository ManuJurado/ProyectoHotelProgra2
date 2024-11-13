package controllers.crear;

import controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controllers.gestionar.GestionarHabitacionesController;

import java.io.IOException;

public class SeleccionarTipoHabitacionController extends BaseController {

    @FXML
    private ComboBox<String> comboBoxTipoHabitacion;

    private GestionarHabitacionesController gestionarHabitacionesController;

    // Metodo para pasar el controlador principal, si es necesario
    public void setPreviousController(GestionarHabitacionesController gestionarHabitacionesController) {
        this.gestionarHabitacionesController = gestionarHabitacionesController;
    }

    // Metodo para manejar el evento de "Aceptar"
    @FXML
    public void handleAceptar(ActionEvent event) {
        String tipoHabitacionSeleccionado = comboBoxTipoHabitacion.getValue();

        if (tipoHabitacionSeleccionado != null) {
            // Pasar la selección al controlador principal (GestionarHabitacionesController)
            if (gestionarHabitacionesController != null) {
                gestionarHabitacionesController.setTipoHabitacionSeleccionado(tipoHabitacionSeleccionado);
            }

            if (tipoHabitacionSeleccionado != null) {
                // Cargar el formulario correspondiente en la ventana principal
                switch (tipoHabitacionSeleccionado) {
                    case "Apartamento":
                        cambiarEscenaConSceneAnterior("/views/crear/crearHabitacionApartamento.fxml", "Creación de Apartamento", (Node) event.getSource());
                        break;
                    case "Suite":
                        cambiarEscenaConSceneAnterior("/views/crear/crearHabitacionSuite.fxml", "Creación de Suite", (Node) event.getSource());
                        break;
                    case "Presidencial":
                        cambiarEscenaConSceneAnterior("/views/crear/crearHabitacionPresidencial.fxml", "Creación de Presidencial", (Node) event.getSource());
                        break;
                    case "Doble":
                        cambiarEscenaConSceneAnterior("/views/crear/crearHabitacionDoble.fxml", "Creación de Habitacion Doble", (Node) event.getSource());
                        break;
                    case "Individual":
                        cambiarEscenaConSceneAnterior("/views/crear/crearHabitacionSimple.fxml", "Creación de Habitacion Simple", (Node) event.getSource());
                        break;
                    default:
                        System.out.println("Habitacion no válida");
                }
            }
            else {
            // Si no se selecciona ninguna opción
            System.out.println("Debe seleccionar un tipo de habitación.");
            }
        }
    }

    // Metodo para manejar el evento de "Cancelar"
    @FXML
    public void handleCancelar(ActionEvent event) {
        // Cerrar la ventana sin hacer nada
        Stage stage = (Stage) comboBoxTipoHabitacion.getScene().getWindow();
        stage.close();
    }
}

