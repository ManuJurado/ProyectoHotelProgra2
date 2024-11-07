package controllers.details;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Habitacion.Habitacion;
import models.Pasajero;
import models.Reserva;
import models.Usuario;

import java.util.List;

public class DetallesReservaController {

    @FXML
    private Label labelCliente;
    @FXML
    private Label labelHabitacion;
    @FXML
    private Label labelFechaInicio;
    @FXML
    private Label labelFechaFin;
    @FXML
    private Label labelEstado;
    @FXML
    private Label labelPasajeros;

    private Reserva reserva; // Añadir un atributo para la reserva

    public void mostrarDetalles(Reserva reserva) {
        this.reserva = reserva; // Guardar la reserva
        labelCliente.setText("Cliente: " + reserva.getCliente().getNombre());
        labelHabitacion.setText("Habitación: " + reserva.getHabitacion().getNumero());
        labelFechaInicio.setText("Fecha de Inicio: " + reserva.getFechaInicio().toString());
        labelFechaFin.setText("Fecha de Fin: " + reserva.getFechaFin().toString());
        labelEstado.setText("Estado: " + reserva.getEstado());

        // Mostrar todos los pasajeros
        StringBuilder pasajeros = new StringBuilder("Pasajeros: ");
        for (Pasajero pasajero : reserva.getPasajeros()) {
            pasajeros.append(pasajero.getNombre()).append(" ").append(pasajero.getApellido()).append(", ");
        }
        labelPasajeros.setText(pasajeros.toString());
    }

    @FXML
    public void onVerDetallesCliente() {
        abrirNuevoStage("/views/detalles/detallesGenerales.fxml", "Detalles del Cliente", reserva.getCliente());
    }

    @FXML
    public void onVerDetallesHabitacion() {
        abrirNuevoStage("/views/detalles/detallesGenerales.fxml", "Detalles de la Habitación", reserva.getHabitacion());
    }

    @FXML
    public void onVerDetallesPasajeros() {
        abrirNuevoStage("/views/detalles/detallesGenerales.fxml", "Detalles de los Pasajeros", reserva.getPasajeros());
    }

    private void abrirNuevoStage(String fxmlPath, String titulo, Object datos) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            DetallesGeneralesController controller = loader.getController();

//            // Llamar al método para mostrar detalles
//            if (datos instanceof Usuario) {
//                controller.mostrarDetallesUsuario((Usuario) datos);
//            } else if (datos instanceof Habitacion) {
//                controller.mostrarDetallesHabitacion(String.valueOf(((Habitacion) datos).getNumero()), ((Habitacion) datos).getTipo(), ((Habitacion) datos).getEstado(),((Habitacion) datos).getCapacidad(),((Habitacion) datos).getCamas());
//            } else if (datos instanceof List<?>) {
//                controller.mostrarDetallesPasajeros((List<Pasajero>) datos);
//            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
