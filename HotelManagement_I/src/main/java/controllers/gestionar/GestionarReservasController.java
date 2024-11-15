package controllers.gestionar;

import controllers.BaseController;
import controllers.GlobalData;
import enums.TipoUsuario;
import controllers.crear.CrearModificarReservaController;
import exceptions.HabitacionInexistenteException;
import exceptions.PasajerosNoCoincidenException;
import exceptions.ReservaCanceladaException;
import exceptions.ReservaNoEncontradaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import services.Sesion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Button buttonModificarReserva,buttonCheckIn,buttonCheckOut, buttonCancelar, buttonEliminar;

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    public void initialize() {
        if (Sesion.getUsuarioLogueado().getTipoUsuario() == TipoUsuario.CLIENTE){
            buttonModificarReserva.setVisible(false);
            buttonCheckIn.setVisible(false);
            buttonCheckOut.setVisible(false);
            buttonCancelar.setVisible(false);
            buttonEliminar.setVisible(false);
        }else if (Sesion.getUsuarioLogueado().getTipoUsuario() == TipoUsuario.CONSERJE){
            buttonModificarReserva.setVisible(false);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        habitacionColumn.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();  // Obtenemos la reserva de la celda
            // Comprobamos si la habitación es eliminada, si no, obtenemos el número de habitación
            if (reserva.getHabitacionEliminada()) {
                // Si la habitación fue eliminada, usamos el numeroAuxiliarHabitacion (asegurándonos de que no sea nulo)
                return new SimpleStringProperty(String.valueOf(reserva.getNumeroHabitacion()));
            } else {
                // Si la habitación no fue eliminada, usamos el número de la habitación
                return new SimpleStringProperty(String.valueOf(reserva.getHabitacion().getNumero()));
            }
        });

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
        // Cargar la lista de reservas desde el servicio
        reservas = gestionReservas.getListaReservas();

        // Crear una lista filtrada solo si el usuario es de tipo CLIENTE
        ObservableList<Reserva> reservasFiltradas;

        if (Sesion.getUsuarioLogueado().getTipoUsuario() == TipoUsuario.CLIENTE) {
            List<Reserva> listaFiltrada = new ArrayList<>();

            // Filtrar las reservas para mostrar solo las del cliente logueado
            for (Reserva r : reservas) {
                if (r.getUsuario().getDni().equals(Sesion.getUsuarioLogueado().getDni())) {
                    listaFiltrada.add(r);
                }
            }
            reservasFiltradas = FXCollections.observableArrayList(listaFiltrada);
        } else {
            // Si el usuario no es CLIENTE, mostrar todas las reservas
            reservasFiltradas = FXCollections.observableArrayList(reservas);
        }

        // Asignar la lista filtrada a la tabla
        tablaReservas.setItems(reservasFiltradas);

        // Refrescar la tabla si es necesario
        tablaReservas.refresh();
    }


    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        cargarReservasEnTabla();
    }

    @FXML
    private void onCrearReserva() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearReserva.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de crear/modificar reserva
            CrearModificarReservaController crearController = loader.getController();

            // Crear y mostrar la nueva ventana de creación de reserva
            Stage stage = new Stage();
            stage.setTitle("Crear Reserva");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);  // Hacer que la ventana sea modal
            stage.showAndWait();  // Esperar hasta que la ventana de creación se cierre

            // Recargar la tabla de reservas
            cargarReservasEnTabla();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de crear reserva.\n" + e.getMessage());
        }
    }


    @FXML
    private void onModificarReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para modificar.");
            return;
        }

        if (!reservaSeleccionada.getEstadoReserva().equalsIgnoreCase("Cancelada")) {
            try {
                GlobalData.setReservaSeleccionada(reservaSeleccionada);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearReserva.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva ventana
                CrearModificarReservaController crearController = loader.getController();

                // Pasar la reserva seleccionada para que la ventana de modificación la pueda cargar

                // Mostrar la ventana para modificar la reserva
                Stage stage = new Stage();
                stage.setTitle("Modificar Reserva");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Una vez cerrada la ventana, actualizar la tabla con las reservas modificadas
                cargarReservasEnTabla();
                GlobalData.limpiarReservaSeleccionada();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo abrir la ventana de modificar reserva.");
            }
        }else {
            mostrarAlerta("No se puede modificar una reserva cancelada");
        }
    }

    @FXML
    private void onCancelarReserva() {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para cancelar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas cancelar esta reserva?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (reservaSeleccionada == null) {
                    mostrarAlerta("Advertencia", "Selecciona una reserva para cancelar.");
                    return;
                }

                // Verificar el estado de la reserva (no debe estar finalizada ni cancelada)
                if (reservaSeleccionada.getEstadoReserva().equals("Finalizada")) {
                    mostrarAlerta("Error", "No se puede cancelar una reserva finalizada.");
                    return;
                }

                if (reservaSeleccionada.getEstadoReserva().equals("Cancelada")) {
                    mostrarAlerta("Error", "La reserva ya está cancelada.");
                    return;
                }

                // Si pasa las validaciones, cambiar el estado a "Cancelada"
                reservaSeleccionada.setEstadoReserva("Cancelada");

                // Actualizar la reserva en el sistema
                gestionReservas.modificarReserva(reservaSeleccionada);

                // Mostrar un mensaje de éxito
                mostrarAlerta("Éxito", "La reserva ha sido cancelada exitosamente.");

                // Actualizar la tabla con las reservas modificadas
                cargarReservasEnTabla();

            } catch (Exception e) {
                // Mostrar un mensaje de error si ocurre una excepción
                mostrarAlerta("Error", "Ocurrió un error al cancelar la reserva: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onEliminarReserva(){
        // Obtener la reserva seleccionada de la tabla
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        // Verificar si hay una reserva seleccionada
        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para cancelar.");
            return;
        }

        // Confirmación de cancelación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar esta reserva?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Asegúrate de tener una instancia de GestionReservas
                GestionReservas gestionReservas = GestionReservas.getInstancia("reservas.json");

                    gestionReservas.eliminarReserva(
                            reservaSeleccionada.getUsuario().getNombre(),
                            String.valueOf(reservaSeleccionada.getHabitacion().getNumero()),
                            reservaSeleccionada.getFechaEntrada()
                    );
                // Actualizar la tabla de reservas
                cargarReservasEnTabla();

                // Mostrar alerta de éxito
                mostrarAlerta("Éxito", "La reserva ha sido eliminada exitosamente.");
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

            // Detalles de los pasajeros
            if (reservaSeleccionada.getPasajeros() != null && !reservaSeleccionada.getPasajeros().isEmpty()) {
                Label pasajerosLabel = new Label("Pasajeros:");
                vbox.getChildren().add(pasajerosLabel);

                for (Pasajero pasajero : reservaSeleccionada.getPasajeros()) {
                    Label pasajeroInfo = new Label("- " + pasajero.getNombre() + " " + pasajero.getApellido() + " (DNI: " + pasajero.getDni() + ")");
                    vbox.getChildren().add(pasajeroInfo);
                }
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
    private void onCheckIn() {
        // Obtener la reserva seleccionada
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para hacer el check-in.");
            return;
        }

        // Verificar si la reserva está cancelada
        if (reservaSeleccionada.getEstadoReserva().equals("Cancelada")) {
            mostrarAlerta("Error", "La reserva está cancelada y no se puede realizar el check-in.");
            return; // Salir del metodo si la reserva está cancelada
        }

        // Abrir la ventana de DNI de pasajeros
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detalles/checkInPasajeros.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana y pasarle la cantidad de pasajeros
            CheckInController controller = loader.getController();
            controller.inicializarCampos(reservaSeleccionada.getCantidadPersonas());
            int cantidadPasajeros = reservaSeleccionada.getCantidadPersonas();
            controller.setCantidadPasajeros(cantidadPasajeros);

            // Crear y mostrar el nuevo Stage
            Stage stage = new Stage();
            stage.setTitle("Ingresar DNIs de los Pasajeros");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recuperar los pasajeros ingresados

            // Intentar realizar el check-in con la lista obtenida
            try {
                List<Pasajero> pasajerosCheckIn = controller.obtenerPasajerosCheckIn();
                gestionReservas.realizarCheckIn(reservaSeleccionada.getId(), pasajerosCheckIn);
                mostrarAlerta("Éxito", "Check-in realizado correctamente.");
                cargarReservasEnTabla(); // Refrescar la tabla de reservas
            } catch (PasajerosNoCoincidenException e) {
                mostrarAlerta("Error", "Los DNIs ingresados no coinciden con la reserva.");
            } catch (RuntimeException e) {
                mostrarAlerta("Error", "DNI invalido");
            } catch (ReservaCanceladaException e) {
                mostrarAlerta("Error", "La reserva está cancelada y no se puede realizar el check-in.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana para ingresar los DNIs de los pasajeros.");
        }
    }


    @FXML
    private void onCheckOut() {
        // Obtener la reserva seleccionada
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        // Verificar si hay una reserva seleccionada
        if (reservaSeleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una reserva para hacer el check-out.");
            return;
        }

        // Verificar que el día de finalización coincida con el día actual
        LocalDate fechaActual = LocalDate.now();
        if (!reservaSeleccionada.getFechaSalida().equals(fechaActual)) {
            mostrarAlerta("Advertencia", "La fecha de finalización de la reserva no coincide con el día actual. No se puede realizar el check-out.");
            return;
        }

        try {
            // Realizar el check-out utilizando el metodo `realizarCheckOut`
            gestionReservas.realizarCheckOut(reservaSeleccionada.getId());

            // Actualizar la tabla de reservas
            cargarReservasEnTabla();

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "Check-out realizado correctamente.");
        } catch (ReservaNoEncontradaException e) {
            mostrarAlerta("Error", "No se encontró la reserva seleccionada.");
        } catch (HabitacionInexistenteException e) {
            mostrarAlerta("Error", "No se encontró la habitación asociada a la reserva.");
        } catch (ReservaCanceladaException e) {
            mostrarAlerta("Error", "La reserva está cancelada y no se puede realizar el check-out.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo realizar el check-out.");
        }
    }


    @FXML
    private void volverAlMenuAdmin(ActionEvent event) {
        volverAEscenaAnterior(event, previousScene);
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
