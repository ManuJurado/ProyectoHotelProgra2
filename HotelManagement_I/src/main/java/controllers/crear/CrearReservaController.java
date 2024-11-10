package controllers.crear;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Habitacion.Habitacion;
import models.Pasajero;
import models.Usuarios.Usuario;
import services.GestionReservas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrearReservaController extends BaseController {


    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtHabitacion;
    @FXML
    private DatePicker dateFechaInicio;
    @FXML
    private DatePicker dateFechaFin;
    @FXML
    private VBox vboxPasajeros;

    @FXML
    private ComboBox<Integer> comboPasajeros;
    @FXML
    private ComboBox<Integer> comboPasajeroSeleccionado; // Cambiado a Integer
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDNI;

    private List<Pasajero> pasajeros = new ArrayList<>(); // Lista de pasajeros
    private GestionReservas gestionReservas;

    public void setGestionReservas(GestionReservas gestionReservas) {
        this.gestionReservas = gestionReservas;
        pasajeros = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        inicializarComboBox();
    }

    private void inicializarComboBox() {
        comboPasajeros.getItems().addAll(1, 2, 3, 4);
        comboPasajeros.setValue(1); // Establecer valor predeterminado
    }

    @FXML
    private void cargarDatosPasajero() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDNI.setText("");

        Integer index = comboPasajeroSeleccionado.getValue();
        if (index != null && index >= 0 && index < pasajeros.size()) {
            Pasajero pasajeroSeleccionado = pasajeros.get(index);
            txtNombre.setText(pasajeroSeleccionado.getNombre());
            txtApellido.setText(pasajeroSeleccionado.getApellido());
            txtDNI.setText(pasajeroSeleccionado.getDni());
        }
    }

    @FXML
    private void cargarPasajeros() {
        int cantidadPasajeros = comboPasajeros.getValue();
        comboPasajeroSeleccionado.getItems().clear();

        // Asegúrate de que la lista de pasajeros tenga la cantidad correcta de pasajeros.
        while (pasajeros.size() < cantidadPasajeros) {
            pasajeros.add(new Pasajero("", "", "")); // Añadir pasajeros vacíos.
        }

        for (int i = 0; i < cantidadPasajeros; i++) {
            comboPasajeroSeleccionado.getItems().add(i); // Agregar índices
        }
    }

    @FXML
    private void guardarPasajero() {
        // Crear un nuevo VBox para el pasajero
        VBox nuevoPasajeroVBox = new VBox();
        nuevoPasajeroVBox.setSpacing(10);

        // Crear los campos de texto
        TextField nombreField = new TextField(txtNombre.getText());
        nombreField.setId("txtNombre");

        TextField apellidoField = new TextField(txtApellido.getText());
        apellidoField.setId("txtApellido");

        TextField dniField = new TextField(txtDNI.getText());
        dniField.setId("txtDNI");

        // Añadir los campos al VBox
        nuevoPasajeroVBox.getChildren().addAll(new Label("Nombre:"), nombreField,
                new Label("Apellido:"), apellidoField,
                new Label("DNI:"), dniField);

        // Agregar el VBox al vboxPasajeros
        vboxPasajeros.getChildren().add(nuevoPasajeroVBox);

        // Limpiar los campos de texto después de agregar
        txtNombre.clear();
        txtApellido.clear();
        txtDNI.clear();
    }

    @FXML
    private void onCrearReserva() {
        try {
            String nombreCliente = txtCliente.getText();
            String numHabitacion = txtHabitacion.getText();
            LocalDate fechaInicio = dateFechaInicio.getValue();
            LocalDate fechaFin = dateFechaFin.getValue();

            // Comprobaciones de inicialización
            if (vboxPasajeros == null) {
                throw new IllegalStateException("El VBox de pasajeros no está inicializado.");
            }

            // Validaciones de campos
            if (nombreCliente.isEmpty() || numHabitacion.isEmpty() || fechaInicio == null || fechaFin == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            // Búsqueda del cliente
            Usuario cliente = buscarClientePorNombre(nombreCliente);
            if (cliente == null) {
                mostrarAlerta("Error", "Cliente no encontrado: " + nombreCliente);
                return;
            }

            // Búsqueda de la habitación
            Habitacion habitacion = buscarHabitacionPorNumero(numHabitacion);
            if (habitacion == null) {
                mostrarAlerta("Error", "No se encontró la habitación con el número: " + numHabitacion);
                return;
            }

            // Imprimir la cantidad de pasajeros en vboxPasajeros
            System.out.println("Cantidad de pasajeros en vbox: " + vboxPasajeros.getChildren().size());

            // Obtener la lista de pasajeros
            List<Pasajero> pasajeros = obtenerListaPasajerosDesdeVBox();
            if (pasajeros.isEmpty()) {
                mostrarAlerta("Error", "No hay pasajeros registrados.");
                return;
            }

            gestionReservas.crearReserva(pasajeros, habitacion, fechaInicio, fechaFin, cliente);
            ((Stage) txtCliente.getScene().getWindow()).close();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo crear la reserva: " + e.getMessage());
        }
    }


    private List<Pasajero> obtenerListaPasajerosDesdeVBox() {
        List<Pasajero> pasajeros = new ArrayList<>();

        // Recorrer los nodos del VBox
        for (Node node : vboxPasajeros.getChildren()) {
            if (node instanceof VBox) { // Suponiendo que cada pasajero está en su propio VBox
                VBox pasajeroVBox = (VBox) node;
                // Aquí se obtienen directamente los TextField de los nodos hijos
                TextField nombreField = (TextField) pasajeroVBox.getChildren().get(1); // el segundo hijo es el nombre
                TextField apellidoField = (TextField) pasajeroVBox.getChildren().get(3); // el cuarto hijo es el apellido
                TextField dniField = (TextField) pasajeroVBox.getChildren().get(5); // el sexto hijo es el DNI

                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String dni = dniField.getText();

                if (!nombre.isEmpty() && !apellido.isEmpty() && !dni.isEmpty()) {
                    pasajeros.add(new Pasajero(nombre, apellido, dni));
                }
            }
        }
        return pasajeros;
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos auxiliares para buscar cliente y habitación (implementa la lógica según tus requerimientos)
    private Usuario buscarClientePorNombre(String nombre) {
        // Implementar la lógica para buscar y retornar un Usuario por su nombre
        // Por ejemplo, buscar en una lista de clientes
        for (Usuario cliente : gestionReservas.getListaClientes()) {
            if (cliente.getNombre().equalsIgnoreCase(nombre)) {
                return cliente; // Retorna el cliente encontrado
            }
        }
        return null; // Retorna null si no se encuentra
    }

    private Habitacion buscarHabitacionPorNumero(String numero) {
        // Convertir el String a Integer
        Integer numeroHabitacion;
        try {
            numeroHabitacion = Integer.parseInt(numero);
        } catch (NumberFormatException e) {
            return null; // Retorna null si el formato no es válido
        }

        // Buscar la habitación en la lista de habitaciones
        for (Habitacion habitacion : gestionReservas.getListaHabitaciones()) {
            if (habitacion.getNumero()==numeroHabitacion) {
                return habitacion; // Retorna la habitación encontrada
            }
        }
        return null; // Retorna null si no se encuentra
    }

    @FXML
    private void cargarCamposPasajeros() {
        int cantidadPasajeros = comboPasajeros.getValue();

        // Limpiar solo si la cantidad de pasajeros ha cambiado
        if (comboPasajeroSeleccionado.getItems().size() != cantidadPasajeros) {
            comboPasajeroSeleccionado.getItems().clear(); // Limpia solo si hay un cambio en la cantidad

            for (int i = 0; i < cantidadPasajeros; i++) {
                // Si la lista de pasajeros tiene el índice, lo añade
                if (i < pasajeros.size()) {
                    comboPasajeroSeleccionado.getItems().add(i);
                } else {
                    // Solo crea nuevos pasajeros si es necesario
                    pasajeros.add(new Pasajero("Nombre" + (i + 1), "Apellido" + (i + 1), "DNI" + (i + 1)));
                    comboPasajeroSeleccionado.getItems().add(i);
                }
            }
        }
    }


    @FXML
    public void onCancelarReserva(){
        ((Stage) txtCliente.getScene().getWindow()).close();
    }
}
