package controllers;

import controllers.gestionar.GestionarReservasController;
import controllers.gestionar.GestionarUsuariosController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Reserva;
import services.GestionReservas;
import services.GestionUsuario;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;

public class AdministradorController extends BaseController {

    // Suponiendo que estás cargando la escena actual
    @FXML
    private void gestionarUsuarios(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarUsuarios.fxml", "Gestionar Usuarios", (Node) event.getSource());
    }

    @FXML
    private void gestionarReservas(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarReservas.fxml", "Gestión de Reservas", (Node) event.getSource());
    }

    @FXML
    private void gestionarServicios(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarServicios.fxml", "Gestión de Servicios", (Node) event.getSource());
    }

    @FXML
    private void crearBackup() {
        // Lógica para crear backup
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        cambiarEscena("/views/menu/login.fxml", "Login", (Node) event.getSource());
    }

    @FXML
    private void salir() {
        System.exit(0);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
