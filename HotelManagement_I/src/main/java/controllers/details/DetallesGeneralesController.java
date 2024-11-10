package controllers.details;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Pasajero;
import models.Usuarios.Usuario;

import java.util.List;

public class DetallesGeneralesController {

    @FXML
    private VBox vboxDetalles; // Usamos un VBox para agregar dinámicamente los detalles

    // Métodos para mostrar detalles
    public void mostrarDetallesUsuario(Usuario cliente) {
        vboxDetalles.getChildren().clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del usuario");
        agregarDetalles("Nombre: " + cliente.getNombre());
        agregarDetalles("Email: " + cliente.getCorreoElectronico());
    }

    public void mostrarDetallesHabitacion(String idHabitacion, String tipo, String estado, int metrosCuadrados, int cantidadCamas) {
        vboxDetalles.getChildren().clear();
        agregarDetalles("ID de Habitación: " + idHabitacion);
        agregarDetalles("Tipo de Habitación: " + tipo);
        agregarDetalles("Estado: " + estado);
        agregarDetalles("Metros cuadrados: " + metrosCuadrados);
        agregarDetalles("Cantidad De Camas: " + cantidadCamas);
    }

    public void mostrarDetallesPasajeros(List<Pasajero> pasajeros) {
        vboxDetalles.getChildren().clear();
        for (Pasajero pasajero : pasajeros) {
            agregarDetalles("Pasajero: " + pasajero.getNombre() + " " + pasajero.getApellido() + " DNI: " + pasajero.getDni());
        }
    }

    private void agregarDetalles(String detalle) {
        Label label = new Label(detalle);
        vboxDetalles.getChildren().add(label);
    }
}
