package controllers.crear;

import controllers.BaseController;
import controllers.gestionar.GestionarUsuariosController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GestionUsuario; // Asegúrate de tener un servicio para gestionar usuarios
import models.Usuarios.*; // Asegúrate de tener un modelo de Usuario
import services.*; // Asegúrate de tener un modelo de Usuario

public class CrearUsuarioController extends BaseController {

    private GestionUsuario gestionarUsuarios;
    private GestionarUsuariosController gestionarUsuariosController;


    @FXML
    private TextField nombreField; // Nombre del usuario
    @FXML
    private TextField emailField; // Email del usuario
    @FXML
    private ComboBox<String> rolChoiceBox; // Combo para seleccionar rol (Cliente/Conserje)

    @FXML
    private void initialize() {
        rolChoiceBox.getItems().addAll("Cliente", "Conserje", "Administrador"); // Agregar roles disponibles
    }

    public void setGestionarUsuarios(GestionUsuario gestionarUsuarios, GestionarUsuariosController gestionarUsuariosController) {
        this.gestionarUsuarios = gestionarUsuarios;
        this.gestionarUsuariosController = gestionarUsuariosController;
    }

    @FXML
    private void guardar() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String rol = rolChoiceBox.getValue();

        if (nombre.isEmpty() || email.isEmpty() || rol == null) {
            mostrarAlerta("Error", "Todos los campos deben ser completados.");
            return;
        }

        // Actualiza la lista de usuarios en el controlador principal
        gestionarUsuariosController.actualizarListaUsuarios();

        // Cierra la ventana actual
        ((Stage) nombreField.getScene().getWindow()).close();
    }

    private void crearUsuario(ActionEvent event) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String rol = rolChoiceBox.getValue();

        if (nombre.isEmpty() || email.isEmpty() || rol == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        mostrarAlerta("Éxito", "Usuario creado con éxito.");
        cerrarVentana(event); // Cerrar la ventana al finalizar
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}