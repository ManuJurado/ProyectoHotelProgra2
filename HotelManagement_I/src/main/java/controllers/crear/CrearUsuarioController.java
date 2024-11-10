package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import enums.TipoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CrearUsuarioController extends BaseController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<TipoUsuario> rolChoiceBox;

    private TipoUsuario tipoUsuarioSeleccionado;  // TipoUsuario en lugar de String
    private Stage stageAnterior; // Variable para almacenar el Stage de la ventana anterior


    @FXML
    public void initialize() {
        // Rellenamos el ComboBox con los valores del enum TipoUsuario
        rolChoiceBox.getItems().addAll(TipoUsuario.CLIENTE, TipoUsuario.ADMINISTRADOR, TipoUsuario.CONSERJE);
    }


    // Metodo para pasar el Stage de la ventana anterior
    public void setStageAnterior(Stage stage) {
        this.stageAnterior = stage;
    }
    // Metodo para regresar a la ventana anterior
    public void regresarAVentanaAnterior() {
        if (stageAnterior != null) {
            stageAnterior.show(); // Mostrar la ventana anterior
        }
    }

    // Metodo que se llama cuando se hace clic en el botón "Crear Usuario"
    @FXML
    public void crear(ActionEvent event) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        TipoUsuario rol = rolChoiceBox.getValue();  // TipoUsuario en lugar de String

        if (rol != null) {
            tipoUsuarioSeleccionado = rol;
            // Guardar los datos en DatosUsuario
            DatosUsuario.setTipoUsuario(rol);  // Guardamos el enum directamente
            DatosUsuario.setNombre(nombre);
            DatosUsuario.setEmail(email);
            cerrarVentana();  // Cierra la ventana después de seleccionar el tipo de usuario
        } else {
            showAlert("Por favor, selecciona un rol.");
        }
    }

    // Dentro de CrearUsuarioController
    public void seleccionarTipoUsuario(TipoUsuario tipoUsuario) {
        // Guardar los datos en DatosUsuario
        DatosUsuario.setTipoUsuario(tipoUsuario);
        DatosUsuario.setNombre(nombreField.getText());
        DatosUsuario.setEmail(emailField.getText());
    }

    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    public TipoUsuario getTipoUsuarioSeleccionado() {
        return tipoUsuarioSeleccionado;
    }
}
