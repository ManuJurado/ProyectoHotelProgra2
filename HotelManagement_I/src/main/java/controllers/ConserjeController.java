package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.Sesion;

public class ConserjeController extends BaseController{

    // Métodos para cada acción del menú

    private Scene previousScene;

    @Override
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    private void checkIn(ActionEvent event) {
        // Lógica para realizar el check-in
        mostrarAlerta("Check In", "Realizando check-in de cliente.");
        // Lógica del check-in aquí
    }

    @FXML
    private void checkOut(ActionEvent event) {
        // Lógica para realizar el check-out
        mostrarAlerta("Check Out", "Realizando check-out de cliente.");
        // Lógica del check-out aquí
    }

    @FXML
    private void asignarReservaACliente(ActionEvent event) {
        // Lógica para asignar una reserva a un cliente
        mostrarAlerta("Asignar Reserva", "Asignando reserva a cliente.");
        // Lógica de asignación aquí
    }

    @FXML
    private void verListaReservas(ActionEvent event) {
        // Lógica para ver la lista de reservas
        mostrarAlerta("Lista de Reservas", "Mostrando lista de reservas.");
        // Lógica para mostrar las reservas aquí
    }

    @FXML
    private void verListaHabitaciones(ActionEvent event) {
        // Lógica para ver la lista de habitaciones
        mostrarAlerta("Lista de Habitaciones", "Mostrando lista de habitaciones.");
        // Lógica para mostrar las habitaciones aquí
    }

    @FXML
    private void verListaClientes(ActionEvent event) {
        // Lógica para ver la lista de clientes
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarUsuarios.fxml", "Gestionar Usuarios", (Node) event.getSource());
    }

    @FXML
    private void realizarReservaSinCliente(ActionEvent event) {
        // Lógica para realizar una reserva sin cliente
        mostrarAlerta("Reserva Sin Cliente", "Realizando reserva sin cliente.");
        // Lógica de reserva sin cliente aquí
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
