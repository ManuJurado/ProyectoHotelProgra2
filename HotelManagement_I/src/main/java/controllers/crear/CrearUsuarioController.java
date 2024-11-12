package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import enums.TipoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class CrearUsuarioController extends BaseController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField dniField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<TipoUsuario> rolChoiceBox;

    private TipoUsuario tipoUsuarioSeleccionado;  // TipoUsuario en lugar de String

    @FXML
    public void initialize() {
        // Limitar la cantidad de caracteres de cada TextField
        setTextFieldLimit(nombreField, 30);   // Limita a 50 caracteres
        setTextFieldLimit(apellidoField, 30); // Limita a 50 caracteres
        setTextFieldLimit(dniField, 10);      // Limita a 10 caracteres (para el DNI)
        setTextFieldLimit(emailField, 30);   // Limita a 100 caracteres (para el correo)

        // Rellenamos el ComboBox con los valores del enum TipoUsuario
        rolChoiceBox.getItems().addAll(TipoUsuario.CLIENTE, TipoUsuario.ADMINISTRADOR, TipoUsuario.CONSERJE);
    }



    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Metodo que se llama cuando se hace clic en el botón "Crear Usuario"
    @FXML
    public void crear(ActionEvent event) {
        // Obtenemos los valores de los campos
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String dni = dniField.getText();
        String email = emailField.getText();
        TipoUsuario rol = rolChoiceBox.getValue();  // TipoUsuario en lugar de String

        // Validamos si el rol ha sido seleccionado
        if (rol != null) {
            tipoUsuarioSeleccionado = rol;
            // Guardar los datos en DatosUsuario
            DatosUsuario.setTipoUsuario(rol);  // Guardamos el enum directamente
            DatosUsuario.setNombre(nombre);
            DatosUsuario.setApellido(apellido);
            DatosUsuario.setDni(dni);
            DatosUsuario.setEmail(email);

            // Cerrar la ventana después de seleccionar el tipo de usuario
            cerrarVentana();
        } else {
            showAlert("Por favor, selecciona un rol.");
        }
    }

    // Metodo para mostrar una alerta con los errores
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Metodo para cerrar la ventana
    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    // Getter para obtener el tipo de usuario seleccionado
    public TipoUsuario getTipoUsuarioSeleccionado() {
        return tipoUsuarioSeleccionado;
    }
}
