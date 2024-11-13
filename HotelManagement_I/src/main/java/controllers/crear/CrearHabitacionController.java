package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import controllers.gestionar.GestionarHabitacionesController;
import controllers.modificar.ModificarContraseniaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GestionHabitaciones;

import java.io.IOException;

public class CrearHabitacionController extends BaseController {

    @FXML
    private TextField tipoHabitacionField; // Tipo de habitación
    @FXML
    private TextField numeroField; // Numero de habitacion
    @FXML
    private TextField capacidadField; // Capacidad
    @FXML
    private TextField camasField; // Cantidad de camas
    @FXML
    private TextField disponibleField; // Disponibilidad
    @FXML
    private TextField estadoField; // Estado
    @FXML
    private TextField detalleEstadoField; // Detalle del estado
    @FXML
    private TextField ambientesField; // Ambientes
    @FXML
    private TextField cocinaField; // Cocina
    @FXML
    private TextField adicionalesField; // Adicionales
    @FXML
    private TextField dimensionField; // Dimension
    @FXML
    private TextField balconField; // Balcon
    @FXML
    private TextField comedorField; // Comedor

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    public void initialize() {
        if(tipoHabitacionField.getText().equalsIgnoreCase("Apartamento")) {
            adicionalesField.setVisible(false);
            dimensionField.setVisible(false);
            balconField.setVisible(false);
            comedorField.setVisible(false);
        }
        else if (tipoHabitacionField.getText().equalsIgnoreCase("Doble") || tipoHabitacionField.getText().equals("Individual")) {
            adicionalesField.setVisible(false);
            dimensionField.setVisible(false);
            balconField.setVisible(false);
            comedorField.setVisible(false);
            cocinaField.setVisible(false);
            ambientesField.setVisible(false);
        }
        else if (tipoHabitacionField.getText().equalsIgnoreCase("Presidencial")) {
            balconField.setVisible(false);
            comedorField.setVisible(false);
            cocinaField.setVisible(false);
            ambientesField.setVisible(false);
        }
        else if (tipoHabitacionField.getText().equalsIgnoreCase("Suite")) {
            adicionalesField.setVisible(false);
            dimensionField.setVisible(false);
            cocinaField.setVisible(false);
            ambientesField.setVisible(false);
        }

/*
        // Rellenar campo tipoHabitacion......
        tipoHabitacionField.setText();

*/
        // Limitar el número de caracteres en los campos de texto
        setTextFieldLimit(tipoHabitacionField, 20);
        setTextFieldLimit(numeroField, 30);
        setTextFieldLimit(capacidadField, 30);
        setTextFieldLimit(camasField, 15);
        setTextFieldLimit(disponibleField, 10);
        setTextFieldLimit(estadoField, 15);
        setTextFieldLimit(detalleEstadoField, 15);
        setTextFieldLimit(ambientesField, 15);
        setTextFieldLimit(cocinaField, 15);
        setTextFieldLimit(adicionalesField, 30);
        setTextFieldLimit(dimensionField, 30);
        setTextFieldLimit(balconField, 30);
        setTextFieldLimit(comedorField, 30);

    }
/*
    @FXML
    private void abrirVentanaModificarContrasenia() throws IOException {
        // Cargar el FXML de la ventana de cambiar contraseña
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/modificarContrasenia.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la nueva ventana
        ModificarContraseniaController controller = loader.getController();

        // Pasar el usuario actual al nuevo controlador
        controller.setUsuario(usuarioSeleccionado);

        // Pasar el controlador actual (ModificarMiUsuarioController) al nuevo controlador
        controller.setControllerAnterior(this);

        // Crear una nueva ventana (Stage) para mostrar la interfaz de modificar contraseña
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Modificar Contraseña");
        stage.show();
    }
    */

}
