package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import exceptions.AtributoFaltanteException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Usuarios.Cliente;
import services.GestionUsuario;

import java.time.LocalDate;
import java.util.ArrayList;

public class CrearClienteController extends BaseController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField dniField;
    @FXML
    private PasswordField contraseniaField;
    @FXML
    private TextField correoElectronicoField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private DatePicker fechaNacimientoPicker;


    private Stage stageAnterior;

    // Metodo para establecer la ventana anterior (Stage)
    public void setStageAnterior(Stage stage) {
        this.stageAnterior = stage;
    }

    @FXML
    public void initialize() {
        // Rellenar los campos con los datos guardados en DatosUsuario
        nombreField.setText(DatosUsuario.getNombre());
        correoElectronicoField.setText(DatosUsuario.getEmail());
    }

    // Metodo que se llama al hacer clic en el botón "Guardar Cliente"
    @FXML
    public void guardarCliente() {
        // Obtiene los valores de los campos
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String dni = dniField.getText();
        String contrasenia = contraseniaField.getText();
        String correoElectronico = correoElectronicoField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        LocalDate fechaNacimiento = fechaNacimientoPicker.getValue();

        // Valida los datos antes de intentar guardar
        if (validarDatos(nombre, apellido, dni, correoElectronico, contrasenia, direccion, telefono, fechaNacimiento)) {
            try {
                // Verificamos si el dni o el correo electrónico ya existen usando la instancia de GestionUsuario
                GestionUsuario gestionUsuario = GestionUsuario.getInstancia("usuarios.json");
                if (gestionUsuario.existeUsuarioConDni(dni) || gestionUsuario.existeUsuarioConCorreo(correoElectronico)) {
                    throw new AtributoFaltanteException("El DNI o correo electrónico ya están registrados.");
                }

                // Si no existe ningún usuario con esos datos, creamos el nuevo cliente
                Cliente cliente = gestionUsuario.crearCliente(nombre, apellido, dni, contrasenia, correoElectronico,
                        direccion, telefono, new ArrayList<>(), 0);

                // Si la creación es exitosa, mostramos un mensaje de éxito
                System.out.println("Cliente guardado con éxito");

                // Volver a la ventana anterior
                regresarAVentanaAnterior();

            } catch (AtributoFaltanteException e) {
                showAlert(e.getMessage());
            }
        } else {
            showAlert("Por favor, complete todos los campos obligatorios.");
        }
    }

    // Metodo para regresar a la ventana anterior
    public void regresarAVentanaAnterior() {
        if (stageAnterior != null) {
            // Lógica para regresar a la ventana anterior
            stageAnterior.show();
            stageAnterior.toFront();  // Asegura que la ventana anterior esté al frente
        }
        cerrarVentana();  // Cierra la ventana actual si es necesario
    }

    private boolean validarDatos(String nombre, String apellido, String dni, String correoElectronico,
                                 String contrasenia, String direccion, String telefono, LocalDate fechaNacimiento) {
        // Validación del correo electrónico con expresión regular
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        boolean esCorreoValido = correoElectronico != null && !correoElectronico.isEmpty() && correoElectronico.matches(emailRegex);

        // Validación de otros campos
        return nombre != null && !nombre.isEmpty()
                && apellido != null && !apellido.isEmpty()
                && dni != null && !dni.isEmpty()
                && esCorreoValido // Validamos que el correo sea válido
                && contrasenia != null && !contrasenia.isEmpty()
                && direccion != null && !direccion.isEmpty()
                && telefono != null && !telefono.isEmpty()
                && fechaNacimiento != null; // Asegurándonos de que la fecha no sea nula
    }

    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
