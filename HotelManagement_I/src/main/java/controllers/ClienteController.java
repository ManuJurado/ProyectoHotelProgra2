package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import services.GestionUsuario;
import services.GestionReservas;
import models.Usuarios.Usuario;
import services.Sesion;

import java.io.IOException;
import java.util.Optional;

public class ClienteController extends BaseController {

    @FXML
    private void modificarMiUsuario(ActionEvent event) {
        // Cambiar a la escena de modificar usuario
        cambiarEscenaConSceneAnterior("/views/modificar/modificarUsuario.fxml", "Modificar Usuario", (Node) event.getSource());
    }

    @FXML
    private void borrarMiUsuario(ActionEvent event) {
        Usuario usuarioLogueado = Sesion.getUsuarioLogueado(); // Obtener el usuario logueado desde la sesión
        if (usuarioLogueado == null) {
            mostrarAlerta("Error", "No hay usuario logueado.");
            return;
        }

        // Pedir la contraseña para confirmar la eliminación
        Boolean contraseniaCorrecta = pedirContrasenia(usuarioLogueado);
        if (contraseniaCorrecta == null) {
            // El usuario canceló la operación, no mostramos la alerta de contraseña incorrecta
            return; // Salimos sin hacer nada
        }

        if (!contraseniaCorrecta) {
            mostrarAlerta("Error", "La contraseña ingresada es incorrecta.");
            return;
        }

        // Lógica para borrar el usuario después de la confirmación de la contraseña
        boolean confirmado = confirmarAccion("¿Estás seguro que deseas borrar tu cuenta?");
        if (confirmado) {
            GestionUsuario gestionUsuario = GestionUsuario.getInstancia("usuarios.json");
            boolean eliminado = gestionUsuario.eliminar(usuarioLogueado.getDni()); // Elimina el usuario por DNI
            if (eliminado) {
                mostrarAlerta("Éxito", "Tu cuenta ha sido eliminada.");
                cerrarSesion(event); // Al eliminar la cuenta, cerrar sesión
            } else {
                mostrarAlerta("Error", "No se pudo eliminar la cuenta.");
            }
        }
    }

    private Boolean pedirContrasenia(Usuario usuarioLogueado) {
        // Crear un diálogo para que el usuario ingrese su contraseña
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirmación de Contraseña");
        dialog.setHeaderText("Por favor, ingresa tu contraseña para confirmar la eliminación de la cuenta.");
        dialog.setContentText("Contraseña:");

        // Mostrar el diálogo y obtener el resultado
        Optional<String> resultado = dialog.showAndWait();

        // Si el usuario cancela el diálogo (resultado es Optional.empty()), retornamos null
        if (!resultado.isPresent()) {
            return null;  // El usuario canceló el diálogo
        }

        // Comprobar si la contraseña ingresada coincide con la del usuario logueado
        String contraseniaIngresada = resultado.get();
        if (!contraseniaIngresada.equals(usuarioLogueado.getContrasenia())) {
            return false; // La contraseña es incorrecta
        }

        return true; // La contraseña es correcta
    }

    @FXML
    private void gestionarMisReservas(ActionEvent event) {
        // Cambiar a la escena de gestionar reservas
        cambiarEscenaConSceneAnterior("/views/gestion/gestionarMisReservas.fxml", "Gestionar Reservas", (Node) event.getSource());
    }

    @FXML
    private void verHabitacionesDisponibles(ActionEvent event) {
        // Cambiar a la escena de ver habitaciones disponibles
        cambiarEscenaConSceneAnterior("/views/gestion/verHabitacionesDisponibles.fxml", "Habitaciones Disponibles", (Node) event.getSource());
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        // Cerrar sesión y volver a la pantalla de login
        Sesion.setUsuarioLogueado(null); // Limpiar el usuario logueado
        cambiarEscena("/views/menu/login.fxml", "Login", (Node) event.getSource());
    }

    @FXML
    private void salir() {
        // Salir de la aplicación
        System.exit(0);
    }

    private boolean confirmarAccion(String mensaje) {
        // Metodo para mostrar una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().filter(button -> button == ButtonType.OK).isPresent();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        // Metodo para mostrar alertas
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
