package controllers;

import enums.TipoUsuario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import models.Usuarios.Usuario;
import org.json.JSONException;
import services.GestionUsuario;
import services.Sesion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button iniciarSesionButton; // Declara el botón en el controlador

    @FXML
    public void initialize() {
        // Limitar el número de caracteres en el usernameField a 20
        usernameField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 20 ? change : null));

        // Limitar el número de caracteres en el passwordField a 20
        passwordField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 20 ? change : null));
        iniciarSesionButton.setDefaultButton(true); // Configura el botón como predeterminado

        // Usamos Platform.runLater() para garantizar que la escena esté completamente cargada antes de modificar el Stage
        Platform.runLater(() -> {
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Cambié usuarioField por usernameField
            stage.setTitle("Inicio de sesión"); // Título de la ventana
            stage.setWidth(800);  // Ancho
            stage.setHeight(600); // Alto
            stage.setResizable(false); // Hacemos que la ventana no sea redimensionable
        });
    }

    private List<Usuario> usuarios = new ArrayList<>();

    // Constructor modificado para obtener la lista de usuarios a través del Singleton
    public LoginController() throws JSONException {
        // Usamos el Singleton de GestionUsuario para obtener la lista de usuarios
        usuarios = GestionUsuario.getInstancia("ProyectoHotelProgra2/HotelManagement_I/usuarios.json").getUsuarios();
        System.out.println(usuarios);
        System.out.printf("\n");
    }

    @FXML
    private void iniciarSesion(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validar si los campos están vacíos
        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error de autenticación", "Por favor, ingresa usuario y contraseña.");
            return;
        }

        // Buscar el usuario en la lista y verificar credenciales
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(username) && usuario.getContrasenia().equals(password)) {
                // Almacenar el usuario logueado en la sesión
                Sesion.setUsuarioLogueado(usuario);
                abrirMenuPorTipo(usuario.getTipoUsuario(), event);
                return;
            }
        }
        // Si no se encontró el usuario o las credenciales son incorrectas
        mostrarAlerta("Error de autenticación", "Credenciales incorrectas, inténtelo nuevamente.");
    }

    @FXML
    private void crearCliente(ActionEvent event) throws IOException {
        cambiarEscenaConSceneAnterior("/views/crear/crearCliente.fxml", "Creación de Cliente", (Node) event.getSource());
    }

    private void abrirMenuPorTipo(TipoUsuario tipoUsuario, ActionEvent event) {
        switch (tipoUsuario) {
            case ADMINISTRADOR:
                System.out.println("Abriendo menú de Administrador...");
                cambiarEscena("/views/menu/menuAdministrador.fxml", "Menú Administrador", (Node) event.getSource());
                break;
            case CLIENTE:
                System.out.println("Abriendo menú de Cliente...");
                cambiarEscena("/views/menu/menuCliente.fxml", "Menú Cliente", (Node) event.getSource());
                break;
            case CONSERJE:
                System.out.println("Abriendo menú de Conserje...");
                cambiarEscena("/views/menu/menuConserje.fxml", "Menú Cliente", (Node) event.getSource());
                break;
        }
    }

    @FXML
    private void salir() {
        System.exit(0);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

