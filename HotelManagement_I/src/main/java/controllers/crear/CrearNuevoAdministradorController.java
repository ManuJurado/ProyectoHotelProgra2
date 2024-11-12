package controllers.crear;

import controllers.BaseController;
import controllers.gestionar.GestionarUsuariosController; // Asegúrate de importar este controlador
import controllers.details.DatosUsuario;
import exceptions.AtributoFaltanteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.Usuarios.Administrador;
import services.GestionUsuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CrearNuevoAdministradorController extends BaseController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField dniField;
    @FXML
    private PasswordField contraseniaField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button verContraseñaButton;
    @FXML
    private TextField correoElectronicoField;
    @FXML
    private PasswordField confirmarContraseniaField;
    @FXML
    private TextField confirmarPasswordTextField;
    @FXML
    private Button verConfirmarContraseñaButton;

    private Scene previousScene;

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    private GestionarUsuariosController gestionarUsuariosController;

    public void setGestionarUsuariosController(GestionarUsuariosController gestionarUsuariosController) {
        this.gestionarUsuariosController = gestionarUsuariosController;
    }

    @FXML
    public void initialize() {
        passwordTextField.setVisible(false);

        // Rellenar los campos con los datos guardados en DatosUsuario
        nombreField.setText(DatosUsuario.getNombre());
        correoElectronicoField.setText(DatosUsuario.getEmail());
        apellidoField.setText(DatosUsuario.getApellido());
        dniField.setText(DatosUsuario.getDni());

        // Limitar el número de caracteres en los campos de texto
        setTextFieldLimit(nombreField, 30);
        setTextFieldLimit(apellidoField, 30);
        setTextFieldLimit(dniField, 10);
        setTextFieldLimit(contraseniaField, 15);
        setTextFieldLimit(passwordTextField, 15);
        setTextFieldLimit(confirmarContraseniaField, 15);
        setTextFieldLimit(confirmarPasswordTextField, 15);
        setTextFieldLimit(correoElectronicoField, 30);

    }

    // Metodo para alternar la visibilidad de la contraseña
    @FXML
    public void togglePasswordVisibility() {
        if (contraseniaField.isVisible()) {
            contraseniaField.setVisible(false);
            passwordTextField.setVisible(true);
            passwordTextField.setText(contraseniaField.getText()); // Sincronizar el texto
            verContraseñaButton.setText("Ocultar");
        } else {
            passwordTextField.setVisible(false);
            contraseniaField.setVisible(true);
            contraseniaField.setText(passwordTextField.getText()); // Sincronizar el texto
            verContraseñaButton.setText("Ver");
        }
    }

    // Controlador para alternar la visibilidad de la confirmación de la contraseña
    @FXML
    public void toggleConfirmarPasswordVisibility() {
        if (confirmarContraseniaField.isVisible()) {
            confirmarContraseniaField.setVisible(false);
            confirmarPasswordTextField.setVisible(true);
            confirmarPasswordTextField.setText(confirmarContraseniaField.getText());
            verConfirmarContraseñaButton.setText("Ocultar");
        } else {
            confirmarPasswordTextField.setVisible(false);
            confirmarContraseniaField.setVisible(true);
            confirmarContraseniaField.setText(confirmarPasswordTextField.getText());
            verConfirmarContraseñaButton.setText("Ver");
        }
    }

    @FXML
    public void guardarAdministrador(ActionEvent event) {
        List<String> errores = new ArrayList<>();

        try {
            // Crear instancia de Administrador y asignar valores
            Administrador administrador = new Administrador();

            // Validaciones
            validarCampo(administrador::setNombre, nombreField.getText(), "Nombre", errores);
            validarCampo(administrador::setApellido, apellidoField.getText(), "Apellido", errores);
            validarCampo(administrador::setDni, dniField.getText(), "DNI", errores);
            validarCampo(administrador::setContrasenia, contraseniaField.getText(), "Contraseña", errores);
            validarCampo(administrador::setContrasenia, confirmarContraseniaField.getText(), "Confirmar Contraseña", errores);
            validarCampo(administrador::setCorreoElectronico, correoElectronicoField.getText(), "Correo Electrónico", errores);

            // Validación de las contraseñas
            String contrasenia = contraseniaField.getText();
            String confirmarContrasenia = confirmarContraseniaField.getText();
            if (contrasenia == null || confirmarContrasenia == null || !contrasenia.equals(confirmarContrasenia)) {
                errores.add("Las contraseñas no coinciden.");
            } else {
                try {
                    administrador.setContrasenia(contrasenia);
                } catch (IllegalArgumentException e) {
                    errores.add(e.getMessage());
                }
            }

            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores));
            } else {
                GestionUsuario gestionUsuario = GestionUsuario.getInstancia("usuarios.json");
                if (gestionUsuario.existeUsuarioConDni(administrador.getDni())) {
                    throw new AtributoFaltanteException("El DNI ya está registrado.");
                }
                if (gestionUsuario.existeUsuarioConCorreo(administrador.getCorreoElectronico())) {
                    throw new AtributoFaltanteException("El correo electrónico ya está registrado.");
                }

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimientoString = null;

                gestionUsuario.crearAdministrador(
                        administrador.getNombre(),
                        administrador.getApellido(),
                        administrador.getDni(),
                        administrador.getContrasenia(),
                        administrador.getCorreoElectronico()
                );

                // Llamar a actualizarListaUsuarios() en el controlador de la escena anterior, si aplica
                if (previousScene != null && previousScene.getUserData() instanceof GestionarUsuariosController) {
                    GestionarUsuariosController gestionarUsuariosController = (GestionarUsuariosController) previousScene.getUserData();
                    gestionarUsuariosController.actualizarListaUsuarios();
                }

                System.out.println("Administrador guardado con éxito");
                volverAEscenaAnterior(event, previousScene);
            }
        } catch (AtributoFaltanteException e) {
            showAlert(e.getMessage());
        }
    }


    private void validarCampo(Consumer<String> setter, String value, String campo, List<String> errores) {
        try {
            setter.accept(value);
        } catch (IllegalArgumentException e) {
            errores.add(campo + ": " + e.getMessage());
        }
    }

    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrarVentana(ActionEvent event) {
        DatosUsuario.limpiarDatos();
        volverAEscenaAnterior(event, previousScene);
    }
}
