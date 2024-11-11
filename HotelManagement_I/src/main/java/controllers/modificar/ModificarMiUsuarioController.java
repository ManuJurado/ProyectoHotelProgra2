package controllers.modificar;

import java.time.LocalDate;
import java.time.ZoneId;
import controllers.BaseController;
import controllers.gestionar.GestionarUsuariosController;
import enums.TipoUsuario;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Usuarios.Administrador;
import models.Usuarios.Cliente;
import models.Usuarios.Conserje;
import models.Usuarios.Usuario;
import services.GestionUsuario;
import services.Sesion;

import java.util.Date;

public class ModificarMiUsuarioController extends BaseController {

    @FXML
    private TextField nombreUsuarioField; // Campo para el nombre del usuario
    @FXML
    private TextField apellidoField; // Campo para el apellido del usuario
    @FXML
    private TextField dniField; // Campo para el DNI del usuario
    @FXML
    private PasswordField contraseniaField; // Campo para la contraseña
    @FXML
    private TextField emailUsuarioField;  // Campo para el email del usuario

    // Campos específicos de Cliente
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private DatePicker fechaNacimientoField;

    // Campos específicos de Conserje
    @FXML
    private TextField turnoField;
    @FXML
    private TextField numeroEmpleadoField;
    @FXML
    private DatePicker fechaIngresoField;
    @FXML
    private TextField areaResponsableField;
    @FXML
    private TextField estadoTrabajoField;

    private GestionarUsuariosController gestionarUsuariosController;
    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        // Si el controlador GestionarUsuariosController está disponible, usamos el usuario seleccionado.
        if (gestionarUsuariosController != null && usuarioSeleccionado != null) {
            cargarDatosUsuario(usuarioSeleccionado);
        } else {
            // Si no hay un usuario seleccionado y no tenemos un controlador GestionarUsuariosController, asignamos el usuario logueado.
            if (usuarioSeleccionado == null) {
                usuarioSeleccionado = Sesion.getUsuarioLogueado();
                if (usuarioSeleccionado == null) {
                    mostrarAlerta("Error", "No hay usuario logueado.");
                    return;
                }
            }
            cargarDatosUsuario(usuarioSeleccionado);
        }
    }

    // Metodo para cargar los datos de un usuario en los campos
    private void cargarDatosUsuario(Usuario usuario) {
        nombreUsuarioField.setText(usuario.getNombre());
        apellidoField.setText(usuario.getApellido());
        dniField.setText(usuario.getDni());
        contraseniaField.setText(usuario.getContrasenia());
        emailUsuarioField.setText(usuario.getCorreoElectronico());

        // Comprobar el tipo de usuario y mostrar/ocultar los campos correspondientes
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            direccionField.setText(cliente.getDireccion());
            telefonoField.setText(cliente.getTelefono());

            // Conversión de java.util.Date a LocalDate para el campo de fechaNacimiento
            Date fechaNacimiento = cliente.getFechaNacimiento(); // Obtener fecha de nacimiento del cliente
            if (fechaNacimiento != null) {
                // Convertir de java.util.Date a LocalDate
                LocalDate localDate = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaNacimientoField.setValue(localDate); // Establecer la fecha en el DatePicker
            }

            direccionField.setVisible(true);
            telefonoField.setVisible(true);
            fechaNacimientoField.setVisible(true); // Hacer visible el campo de fechaNacimiento
        } else if (usuario instanceof Conserje) {
            Conserje conserje = (Conserje) usuario;
            turnoField.setText(conserje.getTurno());
            numeroEmpleadoField.setText(conserje.getNumeroEmpleado());

            // Conversión de java.util.Date a LocalDate para el campo de fechaIngreso
            Date fechaIngreso = conserje.getFechaIngreso(); // Obtener fecha de ingreso del conserje
            if (fechaIngreso != null) {
                // Convertir de java.util.Date a LocalDate
                LocalDate fechaIngresoLocal = fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaIngresoField.setValue(fechaIngresoLocal); // Establecer la fecha en el DatePicker
            }

            areaResponsableField.setText(conserje.getAreaResponsable());
            estadoTrabajoField.setText(conserje.getEstadoTrabajo());

            turnoField.setVisible(true);
            numeroEmpleadoField.setVisible(true);
            fechaIngresoField.setVisible(true); // Hacer visible el campo de fechaIngreso
            areaResponsableField.setVisible(true);
            estadoTrabajoField.setVisible(true);
        } else if (usuario instanceof Administrador) {
            // Si es administrador, no hacemos nada más ya que no tiene campos adicionales
        }
    }

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setUsuarioSeleccionado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
        System.out.println("Usuario seleccionado en ModificarMiUsuarioController: " + usuario);  // Verificar que se pasa correctamente
        cargarDatosUsuario(usuario);  // Cargar los datos del usuario
    }

    // Metodo para establecer el controlador principal (Gestión de usuarios)
    public void setGestionarUsuariosController(GestionarUsuariosController gestionarUsuariosController) {
        this.gestionarUsuariosController = gestionarUsuariosController;
    }

    @FXML
    private void guardarCambios(ActionEvent event) {
        // Asegúrate de que haya un usuario seleccionado
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Error", "No se ha seleccionado un usuario para modificar.");
            return;
        }

        // Obtener los valores modificados del formulario
        String nuevoNombre = nombreUsuarioField.getText();
        String nuevoApellido = apellidoField.getText();
        String nuevoEmail = emailUsuarioField.getText();

        // Actualizar según el tipo de usuario
        if (usuarioSeleccionado instanceof Cliente) {
            Cliente cliente = (Cliente) usuarioSeleccionado;
            cliente.setNombre(nuevoNombre);
            cliente.setApellido(nuevoApellido);
            cliente.setCorreoElectronico(nuevoEmail);
            cliente.setDireccion(direccionField.getText());
            cliente.setTelefono(telefonoField.getText());

            // Conversión de LocalDate a java.util.Date para el campo de fechaNacimiento
            if (fechaNacimientoField.getValue() != null) {
                LocalDate localDate = fechaNacimientoField.getValue(); // Obtener la fecha seleccionada en el DatePicker
                Date fechaNacimiento = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                cliente.setFechaNacimiento(fechaNacimiento); // Guardamos la fecha convertida
            }

        } else if (usuarioSeleccionado instanceof Conserje) {
            Conserje conserje = (Conserje) usuarioSeleccionado;
            conserje.setNombre(nuevoNombre);
            conserje.setApellido(nuevoApellido);
            conserje.setCorreoElectronico(nuevoEmail);
            conserje.setTurno(turnoField.getText());
            conserje.setNumeroEmpleado(numeroEmpleadoField.getText());
            conserje.setAreaResponsable(areaResponsableField.getText());
            conserje.setEstadoTrabajo(estadoTrabajoField.getText());
            conserje.setTelefono(telefonoField.getText());

            // Conversión de LocalDate a java.util.Date para la fecha de ingreso
            if (fechaIngresoField.getValue() != null) {
                LocalDate localDate = fechaIngresoField.getValue();
                Date fechaIngreso = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                conserje.setFechaIngreso(fechaIngreso); // Guardamos la fecha convertida
            }

        } else if (usuarioSeleccionado instanceof Administrador) {
            Administrador admin = (Administrador) usuarioSeleccionado;
            admin.setNombre(nuevoNombre);
            admin.setApellido(nuevoApellido);
            admin.setCorreoElectronico(nuevoEmail);
        }

        // Llamada a la clase GestionUsuario para guardar los cambios en el archivo JSON
        if (GestionUsuario.getInstancia("C:/path/to/usuarios.json").actualizarUsuario(usuarioSeleccionado, nuevoNombre, nuevoApellido, nuevoEmail)) {
            mostrarAlerta("Éxito", "Cambios guardados correctamente.");
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el usuario.");
        }

        // Cerrar la ventana después de guardar los cambios
            cerrarVentana(event);
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        if (previousScene!=null){
            volverAEscenaAnterior(event, previousScene);
        }else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
