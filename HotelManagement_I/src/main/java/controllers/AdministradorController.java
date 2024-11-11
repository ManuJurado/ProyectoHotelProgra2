package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import services.Sesion;

public class AdministradorController extends BaseController {

    @FXML
    private void modificarMiUsuario(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/modificar/modificarUsuario.fxml", "Modificar Mi Usuario", (Node) event.getSource());
    }

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
    private void gestionarHabitaciones(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarHabitaciones.fxml", "Gestión de Servicios", (Node) event.getSource());
    }

    @FXML
    private void crearBackup() {
        // Lógica para crear backup
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Sesion.setUsuarioLogueado(null); // Limpiar el usuario logueado
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
