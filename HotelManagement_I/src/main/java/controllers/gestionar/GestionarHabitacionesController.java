package controllers.gestionar;

import controllers.BaseController;
import controllers.crear.CrearHabitacionController;
import controllers.modificar.ModificarHabitacionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GestionarHabitacionesController extends BaseController {


    @FXML
    private TableView<Habitacion> tablaHabitaciones;
    @FXML
    private TableColumn<Habitacion, String> columnaTipo;
    @FXML
    private TableColumn<Habitacion, String> columnaEstado;
    @FXML
    private TableColumn<Habitacion, Integer> columnaMetrosCuadrados;
    @FXML
    private TableColumn<Habitacion, Integer> columnaCantidadCamas;
    @FXML
    private TableColumn<Habitacion, Integer> nroHabitacion;

    private services.GestionarHabitaciones gestionarHabitaciones;
    private ObservableList<Habitacion> habitacionesOriginales;

    @FXML
    public void initialize() {
        gestionarHabitaciones = new services.GestionarHabitaciones();
        // Configura las columnas de la tabla
        nroHabitacion.setCellValueFactory(new PropertyValueFactory<>("nroHabitacion"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoHabitacion"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaMetrosCuadrados.setCellValueFactory(new PropertyValueFactory<>("metrosCuadrados"));
        columnaCantidadCamas.setCellValueFactory(new PropertyValueFactory<>("cantidadCamas"));
    }

    // Método para establecer el servicio de gestión de habitaciones
    public void setGestionarHabitaciones(services.GestionarHabitaciones gestionarHabitaciones) {
        this.gestionarHabitaciones = gestionarHabitaciones;
        cargarHabitaciones(); // Carga habitaciones después de configurar
    }

    public void cargarHabitaciones() {
        if (gestionarHabitaciones != null) {
            List<Habitacion> habitaciones = gestionarHabitaciones.obtenerHabitaciones();
            System.out.println("Habitaciones cargadas: " + habitaciones.size()); // Depuración

            // Limpiar la tabla antes de agregar nuevos elementos
            tablaHabitaciones.getItems().clear();

            // Actualizar la tabla con las habitaciones obtenidas
            ObservableList<Habitacion> habitacionesObservable = FXCollections.observableArrayList(habitaciones);
            tablaHabitaciones.setItems(habitacionesObservable); // Actualiza la tabla en la UI
        } else {
            System.out.println("GestionarHabitaciones es nulo."); // Depuración adicional
        }
    }


    @FXML
    private void onCrearHabitacion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearHabitacion.fxml"));
            Parent root = loader.load();

            CrearHabitacionController crearHabitacionController = loader.getController();
            crearHabitacionController.setGestionarHabitaciones(gestionarHabitaciones);

            // Aquí pasamos la referencia al controlador de gestión de habitaciones
            crearHabitacionController.setGestionarHabitacionesController(this);

            // Crear un nuevo escenario
            Stage stage = new Stage();
            stage.setTitle("Crear Habitación");
            stage.setScene(new Scene(root));

            // Mostrar el nuevo escenario
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de crear habitación.");
            e.printStackTrace(); // Muestra la traza de error en la consola para depuración
        }
    }

    @FXML
    private void onModificarHabitacion(ActionEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null) {
            try {
                // Cargar el FXML del formulario de modificación
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/ModificarHabitacion.fxml"));
                Parent root = loader.load();

                // Obtener el controlador del nuevo FXML
                ModificarHabitacionController modificarHabitacionController = loader.getController();
                modificarHabitacionController.setHabitacion(habitacionSeleccionada, this); // Pasar la habitación seleccionada y el controlador actual

                // Crear una nueva ventana para mostrar el formulario
                Stage stage = new Stage();
                stage.setTitle("Modificar Habitación");
                stage.setScene(new Scene(root));
                stage.show();

                // Cerrar la ventana actual si es necesario (opcional)
                // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo abrir el formulario de modificación.");
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona una habitación para modificar.");
        }
    }

    @FXML
    private void onHabilitarInhabilitarHabitacion(ActionEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null) {
            // Lógica para habilitar o inhabilitar la habitación
            String nuevoEstado = habitacionSeleccionada.getEstado().equals("disponible") ? "no disponible" : "disponible";
            habitacionSeleccionada.setEstado(nuevoEstado);
            cargarHabitaciones();
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona una habitación para habilitar/inhabilitar.");
        }
    }

    @FXML
    private void onBorrarHabitacion(ActionEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null) {
            gestionarHabitaciones.eliminarHabitacion(habitacionSeleccionada);
            cargarHabitaciones();
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona una habitación para borrar.");
        }
    }

    public void actualizarListaHabitaciones() {
        if (gestionarHabitaciones != null) {
            List<Habitacion> habitaciones = gestionarHabitaciones.obtenerHabitaciones();
            System.out.println("Usuarios cargados: " + habitaciones.size()); // Depuración

            // Limpiar la tabla antes de agregar nuevos elementos
            tablaHabitaciones.getItems().clear();

            // Guardar la lista original de usuarios
            habitacionesOriginales = FXCollections.observableArrayList(habitaciones);

            // Actualizar la tabla con los usuarios obtenidos
            tablaHabitaciones.setItems(habitacionesOriginales); // Actualiza la tabla en la UI
        } else {
            System.out.println("GestionarUsuarios es nulo."); // Depuración adicional
        }
    }

    @FXML
    private void onVerDetalleHabitacion(ActionEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null) {
            // Lógica para mostrar detalles de la habitación (puedes usar un cuadro de diálogo o nueva ventana)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles de la Habitación");
            alert.setHeaderText(habitacionSeleccionada.getTipoHabitacion());
            alert.setContentText("Estado: " + habitacionSeleccionada.getEstado() +
                    "\nMetros Cuadrados: " + habitacionSeleccionada.getMetrosCuadrados() +
                    "\nCantidad de Camas: " + habitacionSeleccionada.getCantidadCamas() +
                    "\nCapacidad: " + habitacionSeleccionada.getCapacidad());
            alert.showAndWait();
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona una habitación para ver detalles.");
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
