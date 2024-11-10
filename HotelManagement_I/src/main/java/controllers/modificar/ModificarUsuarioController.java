package controllers.modificar;

import controllers.BaseController;
import controllers.gestionar.GestionarUsuariosController;
import enums.TipoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Usuarios.Usuario;

public class ModificarUsuarioController extends BaseController {

    @FXML
    private ComboBox<String> rolChoiceBox; // Combo para seleccionar rol (Cliente/Conserje)
    @FXML
    private TextField nombreUsuarioField; // Campo para el nombre del usuario
    @FXML
    private TextField emailUsuarioField;  // Campo para el email del usuario (opcional)

    private Usuario usuarioOriginal; // Usuario que vamos a modificar
    private GestionarUsuariosController gestionarUsuariosController; // Controlador principal

    @FXML
    public void initialize() {
        // Inicialización adicional si es necesario
    }

    // Método para configurar el usuario y el controlador principal
    public void setUsuario(Usuario usuario, GestionarUsuariosController gestionarUsuariosController) {
        this.usuarioOriginal = usuario;
        this.gestionarUsuariosController = gestionarUsuariosController;

        // Llenar los campos con la información actual del usuario
        nombreUsuarioField.setText(usuario.getNombre());
        emailUsuarioField.setText(usuario.getCorreoElectronico());
        rolChoiceBox.getItems().addAll("Cliente", "Conserje", "Administrador"); // Agregar roles disponibles
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        String nuevoNombre = nombreUsuarioField.getText();
        String nuevoEmail = emailUsuarioField.getText();
        TipoUsuario nuevoRol = TipoUsuario.valueOf(rolChoiceBox.getValue()); // TipoUsuario directamente, no necesitas .name()

        if (!nuevoNombre.isEmpty() && !nuevoEmail.isEmpty() && nuevoRol != null) { // Comprobar que el rol no esté vacío
            gestionarUsuariosController.actualizarUsuario(usuarioOriginal, nuevoNombre, nuevoEmail, nuevoRol); // Pasar el nuevo rol
            mostrarAlerta("Éxito", "Cambios guardados correctamente.");
            cerrarVentana(event);
        } else {
            mostrarAlerta("Advertencia", "El nombre, el email y el rol no pueden estar vacíos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
