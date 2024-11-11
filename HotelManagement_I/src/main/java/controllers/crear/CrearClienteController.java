package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import controllers.gestionar.GestionarUsuariosController;
import exceptions.AtributoFaltanteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Usuarios.Cliente;
import services.GestionUsuario;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

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

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    public void initialize() {
        // Rellenar los campos con los datos guardados en DatosUsuario
        nombreField.setText(DatosUsuario.getNombre());
        correoElectronicoField.setText(DatosUsuario.getEmail());

        // Limitar el número de caracteres en los campos de texto
        nombreField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 30 ? change : null));

        apellidoField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 30 ? change : null));

        direccionField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 30 ? change : null));

        telefonoField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 15 ? change : null));

        dniField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 10 ? change : null));

        contraseniaField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 20 ? change : null));

        correoElectronicoField.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 30 ? change : null));
    }

    @FXML
    public void guardarCliente(ActionEvent event) {
        List<String> errores = new ArrayList<>();  // Lista para acumular mensajes de error

        try {
            // Crear instancia de Cliente y asignar valores
            Cliente cliente = new Cliente();

            // Validaciones
            validarCampo(cliente::setNombre, nombreField.getText(), "Nombre", errores);
            validarCampo(cliente::setApellido, apellidoField.getText(), "Apellido", errores);
            validarCampo(cliente::setDni, dniField.getText(), "DNI", errores);
            validarCampo(cliente::setContrasenia, contraseniaField.getText(), "Contraseña", errores);
            validarCampo(cliente::setCorreoElectronico, correoElectronicoField.getText(), "Correo Electrónico", errores);
            validarCampo(cliente::setDireccion, direccionField.getText(), "Dirección", errores);
            validarCampo(cliente::setTelefono, telefonoField.getText(), "Teléfono", errores);

            if (fechaNacimientoPicker.getValue() != null) {
                LocalDate localDate = fechaNacimientoPicker.getValue();
                // Convertir LocalDate a Date con la zona horaria correcta
                ZoneId zoneId = ZoneId.systemDefault();
                Date fechaNacimiento = Date.from(localDate.atStartOfDay(zoneId).toInstant());
                cliente.setFechaNacimiento(fechaNacimiento);
            } else {
                errores.add("La fecha de nacimiento es obligatoria.");
            }

            // Verificar si hay errores acumulados y mostrarlos en una sola alerta
            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores));
            } else {
                // Obtener la instancia de GestionUsuario y verificar duplicados
                GestionUsuario gestionUsuario = GestionUsuario.getInstancia("usuarios.json");
                if (gestionUsuario.existeUsuarioConDni(cliente.getDni())) {
                    throw new AtributoFaltanteException("El DNI ya está registrado.");
                }
                if (gestionUsuario.existeUsuarioConCorreo(cliente.getCorreoElectronico())) {
                    throw new AtributoFaltanteException("El correo electrónico ya está registrado.");
                }

                // Ahora, cuando vayas a guardar, formateas la fecha a "yyyy-MM-dd" solo para el almacenamiento
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimientoString = null;
                if (cliente.getFechaNacimiento() != null) {
                    fechaNacimientoString = formato.format(cliente.getFechaNacimiento());
                }

                // Guardar el cliente en el JSON, manteniendo la fecha como Date en la clase Cliente
                gestionUsuario.crearCliente(
                        cliente.getNombre(),
                        cliente.getApellido(),
                        cliente.getDni(),
                        cliente.getContrasenia(),
                        cliente.getCorreoElectronico(),
                        cliente.getDireccion(),
                        cliente.getTelefono(),
                        new ArrayList<>(), // Historial de reservas vacío
                        0, // Puntos de fidelidad inicial
                        cliente.getFechaNacimiento() // Se pasa la fecha como Date
                );
                System.out.println("Cliente guardado con éxito");
                volverAEscenaAnterior(event, previousScene);
            }
        } catch (AtributoFaltanteException e) {
            showAlert(e.getMessage());
        }
    }




    // Metodo de validación general para los campos
    private void validarCampo(Consumer<String> setter, String value, String campo, List<String> errores) {
        try {
            setter.accept(value);
        } catch (IllegalArgumentException e) {
            errores.add(campo + ": " + e.getMessage());
        }
    }

    // Metodo para mostrar una alerta con los errores acumulados
    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void cerrarVentana(ActionEvent event) {
        DatosUsuario.limpiarDatos();
        volverAEscenaAnterior(event, previousScene);
    }
}
