package controllers;

import controllers.gestionar.GestionarHabitacionesController;
import controllers.gestionar.GestionarUsuariosController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Habitacion;
import services.GestionarHabitaciones;
import services.GestionarUsuarios;
import javafx.scene.control.Alert;

import java.util.List;

public class AdministradorController extends BaseController {

    private GestionarUsuarios gestionarUsuarios = new GestionarUsuarios();
    private GestionarHabitaciones gestionarHabitaciones = new GestionarHabitaciones();

    @FXML
    private void gestionarUsuarios(ActionEvent event) {
        // Cambiar a la escena de gestionar usuarios utilizando el método cambiarEscena
        GestionarUsuariosController gestionarUsuariosController = cambiarEscena("/views/gestion/gestionarUsuarios.fxml", "Gestión de Usuarios", (Node) event.getSource());

        if (gestionarUsuariosController != null) {
            // Configurar el controlador si se cargó correctamente
            gestionarUsuariosController.setGestionarUsuarios(gestionarUsuarios);
        } else {
            // Mostrar una alerta si algo falló
            mostrarAlerta("Error", "No se pudo cargar la vista de gestión de usuarios.");
        }
    }

    @FXML
    private void gestionarHabitaciones(ActionEvent event) {
        // Primero, carga las habitaciones
        List<Habitacion> habitaciones = gestionarHabitaciones.obtenerHabitaciones();
        System.out.println("Habitaciones cargadas: " + habitaciones.size()); // Depuración

        // Cambiar a la escena de gestionar habitaciones utilizando el método cambiarEscena
        GestionarHabitacionesController gestionarHabitacionesController = cambiarEscena("/views/gestion/gestionarHabitaciones.fxml", "Gestión de Habitaciones", (Node) event.getSource());

        if (gestionarHabitacionesController != null) {
            // Configurar el controlador
            gestionarHabitacionesController.setGestionarHabitaciones(gestionarHabitaciones);
            gestionarHabitacionesController.cargarHabitaciones(); // Cargar habitaciones aquí
        } else {
            // Mostrar una alerta si algo falló
            mostrarAlerta("Error", "No se pudo cargar la vista de gestión de habitaciones.");
        }
    }


    @FXML
    private void gestionarReservas(ActionEvent event) {
        cambiarEscena("/views/gestion/gestionarReservas.fxml", "Gestión de Reservas", (Node) event.getSource());
    }

    @FXML
    private void gestionarServicios(ActionEvent event) {
        cambiarEscena("/views/gestion/gestionarServicios.fxml", "Gestión de Servicios", (Node) event.getSource());
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
