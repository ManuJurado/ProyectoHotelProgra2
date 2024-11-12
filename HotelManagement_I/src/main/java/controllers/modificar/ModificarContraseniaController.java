package controllers.modificar;

import com.fasterxml.jackson.databind.ser.Serializers;
import controllers.BaseController;
import controllers.details.DatosUsuario;
import exceptions.UsuarioDuplicadoException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Usuarios.Usuario;
import services.GestionUsuario;

public class ModificarContraseniaController extends BaseController {

    @FXML
    private PasswordField contraseniaActualField;
    @FXML
    private PasswordField nuevaContraseniaField;
    @FXML
    private PasswordField confirmarContraseniaField;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private ModificarMiUsuarioController controllerAnterior;

    public void setControllerAnterior(ModificarMiUsuarioController controller) {
        this.controllerAnterior = controller;
    }

    @FXML
    public void initialize(){
            // Limitar el número de caracteres en los campos de texto
        setTextFieldLimit(contraseniaActualField, 30);
        setTextFieldLimit(nuevaContraseniaField, 30);
        setTextFieldLimit(confirmarContraseniaField, 30);
    }

    @FXML
    private void guardarNuevaContrasenia() {
        String contraseniaActual = contraseniaActualField.getText();
        String nuevaContrasenia = nuevaContraseniaField.getText();
        String confirmarContrasenia = confirmarContraseniaField.getText();

        // Verificar que las contraseñas coinciden
        if (!nuevaContrasenia.equals(confirmarContrasenia)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.");
            return;
        }

        // Verificar que la contraseña actual es correcta
        if (!usuario.getContrasenia().equals(contraseniaActual)) {
            mostrarAlerta("Error", "La contraseña actual es incorrecta.");
            return;
        }

        // Intentar actualizar la contraseña dentro de un try-catch
        try {
            // Actualizar la contraseña
            usuario.setContrasenia(nuevaContrasenia);

            // Guardar el usuario actualizado
            boolean exito = false;
            try {
                exito = GestionUsuario.getInstancia("HotelManagement_I/usuarios.json").actualizarUsuario(usuario);
            } catch (UsuarioDuplicadoException e) {
                mostrarAlerta("Error", "Hubo un problema al actualizar el usuario.");
                e.printStackTrace(); // Para ver el detalle de la excepción
                return;
            }

            if (exito) {
                mostrarAlerta("Éxito", "Contraseña modificada correctamente.");
                // Notificar al controlador anterior para que actualice la contraseña
                if (controllerAnterior != null) {
                    System.out.println("Actualizando contraseña en el controlador anterior...");
                    controllerAnterior.actualizarContrasenia(usuario.getContrasenia());
                } else {
                    System.out.println("Error: No se pudo acceder al controlador anterior.");
                }
                cerrarVentana();
            } else {
                mostrarAlerta("Error", "No se pudo guardar la nueva contraseña.");
            }

        } catch (IllegalArgumentException e) {
            // Captura la excepción de longitud de contraseña y muestra el mensaje de error
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    private void cancelarCambioContrasenia() {
        cerrarVentana();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) contraseniaActualField.getScene().getWindow();
        stage.close();
    }
}
