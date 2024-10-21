package controllers.gestionar;

import controllers.BaseController;
import controllers.crear.CrearReservaController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Pasajero;
import services.GestionReservas;
import models.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestionarReservasController extends BaseController {

    private List<Reserva> reservas = new ArrayList<>();

    @FXML
    private TableView<Reserva> tablaReservas; // Tabla para mostrar reservas
    @FXML
    private TableColumn<Reserva, Integer> idColumn; // Columna para el ID de la reserva
    @FXML
    private TableColumn<Reserva, String> habitacionColumn; // Columna para la habitación
    @FXML
    private TableColumn<Reserva, String> fechaInicioColumn; // Columna para la fecha de inicio
    @FXML
    private TableColumn<Reserva, String> fechaFinColumn; // Columna para la fecha de fin
    @FXML
    private TableColumn<Reserva, String> clienteColumn; // Columna para el nombre del cliente
    @FXML
    private TableColumn<Reserva, String> estadoColumn; // Columna para el estado de la reserva

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        cargarReservasEnTabla();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        habitacionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHabitacion().getId().toString()));
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        fechaFinColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNombre()));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Llama al método que carga las reservas en la tabla
        cargarReservasEnTabla();
    }

    private void cargarReservasEnTabla() {
        // Convierte la lista de reservas en un ObservableList para poder usarlo en la tabla
        ObservableList<Reserva> observableReservas = FXCollections.observableArrayList(reservas);

        // Asigna el ObservableList a la tabla
        tablaReservas.setItems(observableReservas);
    }

    // Métodos para crear, modificar, cancelar y ver detalles de reservas
    @FXML
    private void onCrearReserva() {
        // Abre un diálogo para crear una nueva reserva (puedes usar un FXML diferente)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearReserva.fxml"));
            Parent root = loader.load();

            CrearReservaController crearController = loader.getController();
            crearController.setGestionReservas(new GestionReservas()); // Pasar la instancia de la clase gestora

            Stage stage = new Stage();
            stage.setTitle("Crear Reserva");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Después de cerrar el diálogo, recargar las reservas en la tabla
            cargarReservasEnTabla();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de crear reserva.");
        }
    }

    @FXML
    private void onModificarReserva() {
        // Implementar lógica para modificar una reserva
    }

    @FXML
    private void onCancelarReserva() {
        // Implementar lógica para cancelar una reserva
    }

    @FXML
    private void onVerDetalleReserva() {
        // Obtener la reserva seleccionada en la tabla
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada != null) {
            try {
                // Cargar el FXML de detalles de la reserva
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detalles/detallesReserva.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y pasarle la reserva seleccionada
                controllers.details.DetallesReservaController detallesController = loader.getController();
                detallesController.mostrarDetalles(reservaSeleccionada);

                // Crear y mostrar la nueva ventana
                Stage stage = new Stage();
                stage.setTitle("Detalles de la Reserva");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar la excepción adecuadamente (por ejemplo, mostrar un mensaje al usuario)
            }
        } else {
            // Mostrar un mensaje de advertencia si no hay reserva seleccionada
            mostrarAlerta("Advertencia", "Por favor, selecciona una reserva para ver los detalles.");
        }
    }


    private String obtenerNombresPasajeros(List<Pasajero> pasajeros) {
        return pasajeros.stream()
                .map(pasajero -> pasajero.getNombre() + " " + pasajero.getApellido())
                .collect(Collectors.joining(", "));
    }

    @FXML
    private void onCerrarVentana() {
        // Implementar lógica para cerrar la ventana de gestión de reservas
    }

    @FXML
    private void volverAlMenuAdmin(ActionEvent event) {
        cambiarEscena("/views/menu/menuAdministrador.fxml", "Menú Administrador", (Node) event.getSource());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
