package controllers.gestionar;

import controllers.BaseController;
import controllers.crear.CrearReservaController;
import exceptions.AtributoFaltanteException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Pasajero;
import services.GestionReservas;
import models.Reserva;
import javafx.fxml.FXML;
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

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        habitacionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getHabitacion().getNumero())));
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaEntrada"));
        fechaFinColumn.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoReserva"));

        cargarReservasEnTabla();
    }

    // Declaración del servicio de reservas
    private GestionReservas gestionReservas = GestionReservas.getInstancia("HotelManagement_I/reservas.json");

    public void setGestionReservas(GestionReservas gestionReservas) {
        this.gestionReservas = gestionReservas;
    }

    private void cargarReservasEnTabla() {
        reservas = gestionReservas.getListaReservas();  // Cargar lista desde el servicio
        ObservableList<Reserva> observableReservas = FXCollections.observableArrayList(reservas);
        tablaReservas.setItems(observableReservas);
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        cargarReservasEnTabla();
    }

    // Métodos para crear, modificar, cancelar y ver detalles de reservas
    @FXML
    private void onCrearReserva() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearReserva.fxml"));
            Parent root = loader.load();

            // El controlador de la ventana de crear reserva será cargado automáticamente con el FXMLLoader
            CrearReservaController crearController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Crear Reserva");
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Mostrar la ventana y esperar hasta que se cierre

            cargarReservasEnTabla();  // Actualizar la tabla después de que la ventana de creación de reserva se haya cerrado
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de crear reserva.");
        }
    }


    @FXML
    private void onModificarReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearReserva.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            CrearReservaController crearController = loader.getController();

            // Pasar la reserva seleccionada para que la ventana de modificación la pueda cargar
//            crearController.setReservaParaModificar(reservaSeleccionada);

            // Mostrar la ventana para modificar la reserva
            Stage stage = new Stage();
            stage.setTitle("Modificar Reserva");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Una vez cerrada la ventana, actualizar la tabla con las reservas modificadas
            cargarReservasEnTabla();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de modificar reserva.");
        }
    }

    @FXML
    private void onCancelarReserva() {
        // Obtener la reserva seleccionada de la tabla
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        // Verificar si hay una reserva seleccionada
        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para cancelar.");
            return;
        }

        // Confirmación de cancelación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas cancelar esta reserva?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Asegúrate de tener una instancia de GestionReservas
                GestionReservas gestionReservas = GestionReservas.getInstancia("reservas.json");

                // Llamar al metodo eliminarReserva para cancelar la reserva
                gestionReservas.eliminarReserva(reservaSeleccionada.getUsuario().getNombre(),
                        String.valueOf(reservaSeleccionada.getHabitacion().getNumero()),
                        reservaSeleccionada.getFechaEntrada());

                // Actualizar la tabla de reservas
                cargarReservasEnTabla();

                // Mostrar alerta de éxito
                mostrarAlerta("Éxito", "La reserva ha sido cancelada exitosamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cancelar la reserva.");
            }
        }
    }

    @FXML
    private void onVerDetalleReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada != null) {
            // Crear la ventana para los detalles
            Stage detallesStage = new Stage();
            detallesStage.setHeight(400);
            detallesStage.setWidth(600);
            detallesStage.setTitle("Detalles de la Reserva");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(20));

            // Detalles comunes de la reserva
            Label nombreLabel = new Label("Nombre del Usuario: " + reservaSeleccionada.getUsuario().getNombre());
            Label numeroHabitacionLabel = new Label("Número de Habitación: " + reservaSeleccionada.getHabitacion().getNumero());
            Label fechaEntradaLabel = new Label("Fecha de Entrada: " + reservaSeleccionada.getFechaEntrada());
            Label fechaSalidaLabel = new Label("Fecha de Salida: " + reservaSeleccionada.getFechaSalida());
            Label cantidadPersonasLabel = new Label("Cantidad de Personas: " + reservaSeleccionada.getCantidadPersonas());

            vbox.getChildren().addAll(nombreLabel, numeroHabitacionLabel, fechaEntradaLabel, fechaSalidaLabel, cantidadPersonasLabel);

            // Si existen servicios adicionales, los mostramos
            if (reservaSeleccionada.getServiciosAdicionales() != null && !reservaSeleccionada.getServiciosAdicionales().isEmpty()) {
                Label serviciosLabel = new Label("Servicios Adicionales: " + String.join(", ", reservaSeleccionada.getServiciosAdicionales()));
                vbox.getChildren().add(serviciosLabel);
            }

            // Botón para cerrar
            Button cerrarButton = new Button("Cerrar");
            cerrarButton.setOnAction(e -> detallesStage.close());
            vbox.getChildren().add(cerrarButton);

            // Crear y mostrar la ventana
            Scene scene = new Scene(vbox, 300, 350);
            detallesStage.setScene(scene);
            detallesStage.initModality(Modality.APPLICATION_MODAL);
            detallesStage.showAndWait();
        } else {
            mostrarAlerta("Advertencia", "Selecciona una reserva para ver los detalles.");
        }
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
