package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.GestionHabitaciones;
import services.Sesion;

public class ConserjeController extends BaseController{

    // Métodos para cada acción del menú

    private Scene previousScene;

    @Override
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }


    @FXML
    private void verListaReservas(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarReservas.fxml","Gestionar Reservas",(Node) event.getSource());
    }

    @FXML
    private void verListaHabitaciones(ActionEvent event) {
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarHabitaciones.fxml","Gestionar Habitaciones", (Node) event.getSource());
    }

    @FXML
    private void verListaClientes(ActionEvent event) {
        // Lógica para ver la lista de clientes
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarUsuarios.fxml", "Gestionar Usuarios", (Node) event.getSource());
    }

    @FXML
    private void modificarDatosUsuario(ActionEvent event) {
        // Lógica para modificar los datos del usuario
        cambiarEscenaConSceneAnterior("/views/modificar/modificarUsuario.fxml","Modificar Conserje", (Node) event.getSource());
        // Lógica para modificar datos de usuario aquí
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Sesion.setUsuarioLogueado(null); // Limpiar el usuario logueado
        cambiarEscena("/views/menu/login.fxml", "Login", (Node) event.getSource());    }

    @FXML
    private void salir(ActionEvent event) {
        // Lógica para salir de la aplicación
        Stage stage = (Stage) event.getSource();
        stage.close();
    }

    // Metodo para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
