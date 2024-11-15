package controllers.crear;

import controllers.BaseController;
import controllers.details.DatosUsuario;
import controllers.gestionar.GestionarUsuariosController;
import exceptions.AtributoFaltanteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import models.Usuarios.Conserje;
import services.GestionUsuario;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class CrearConserjeController extends BaseController {

    @FXML
    private TextField nombreField,apellidoField,dniField,correoElectronicoField,telefonoField,estadoTrabajoField;

    @FXML
    private PasswordField contraseniaField;

    @FXML
    private DatePicker fechaIngresoPicker;

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
        apellidoField.setText(DatosUsuario.getApellido());
        dniField.setText(DatosUsuario.getDni());
        // Limitar el número de caracteres en los campos de texto
        setTextFieldLimit(nombreField, 30);
        setTextFieldLimit(apellidoField, 30);
        setTextFieldLimit(telefonoField, 15);
        setTextFieldLimit(dniField, 10);
        setTextFieldLimit(contraseniaField, 20);
        setTextFieldLimit(correoElectronicoField, 30);
        setTextFieldLimit(estadoTrabajoField, 30);

    }

    @FXML
    public void guardarConserje(ActionEvent event) {
        List<String> errores = new ArrayList<>();  // Lista para acumular mensajes de error

        try {
            // Crear instancia de Conserje y asignar valores
            Conserje conserje = new Conserje();

            // Validaciones
            validarCampo(conserje::setNombre, nombreField.getText(), "Nombre", errores);
            validarCampo(conserje::setApellido, apellidoField.getText(), "Apellido", errores);
            validarCampo(conserje::setDni, dniField.getText(), "DNI", errores);
            validarCampo(conserje::setContrasenia, contraseniaField.getText(), "Contraseña", errores);
            validarCampo(conserje::setCorreoElectronico, correoElectronicoField.getText(), "Correo Electrónico", errores);
            validarCampo(conserje::setTelefono, telefonoField.getText(), "Teléfono", errores);
            validarCampo(conserje::setEstadoTrabajo, estadoTrabajoField.getText(), "Estado de Trabajo", errores);

            if (fechaIngresoPicker.getValue() != null) {
                LocalDate localDate = fechaIngresoPicker.getValue();
                conserje.setFechaIngreso(localDate); // Asignar directamente el LocalDate
            } else {
                errores.add("La fecha de ingreso es obligatoria.");
            }

            // Verificar si hay errores acumulados y mostrarlos en una sola alerta
            if (!errores.isEmpty()) {
                showAlert(String.join("\n", errores));
            } else {
                // Obtener la instancia de GestionUsuario y verificar duplicados
                GestionUsuario gestionUsuario = GestionUsuario.getInstancia("HotelManagement_I/usuarios.json");
                if (gestionUsuario.existeUsuarioConDni(conserje.getDni())) {
                    throw new AtributoFaltanteException("El DNI ya está registrado.");
                }
                if (gestionUsuario.existeUsuarioConCorreo(conserje.getCorreoElectronico())) {
                    throw new AtributoFaltanteException("El correo electrónico ya está registrado.");
                }


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaIngresoString = null;
                if (conserje.getFechaIngreso() != null) {
                    fechaIngresoString = conserje.getFechaIngreso().format(formatter);
                }

                // Guardar el conserje en el JSON, manteniendo la fecha como Date en la clase Conserje
                gestionUsuario.crearConserje(
                        conserje.getNombre(),
                        conserje.getApellido(),
                        conserje.getDni(),
                        conserje.getContrasenia(),
                        conserje.getCorreoElectronico(),
                        conserje.getFechaIngreso(),
                        conserje.getTelefono(),
                        conserje.getEstadoTrabajo()
                );

                // Llamar a actualizarListaUsuarios() en el controlador de la escena anterior, si aplica
                if (previousScene != null && previousScene.getUserData() instanceof GestionarUsuariosController gestionarUsuariosController) {
                    gestionarUsuariosController.actualizarListaUsuarios();
                }

                System.out.println("Conserje guardado con éxito");
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
