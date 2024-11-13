package controllers.modificar;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import controllers.BaseController;
import controllers.gestionar.GestionarUsuariosController;
import exceptions.UsuarioDuplicadoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.Usuarios.Administrador;
import models.Usuarios.Cliente;
import models.Usuarios.Conserje;
import models.Usuarios.Usuario;
import services.GestionUsuario;
import services.Sesion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModificarMiUsuarioController extends BaseController {

    @FXML
    private TextField nombreUsuarioField;
    @FXML
    private TextField apellidoField; // Campo para el apellido del usuario
    @FXML
    private TextField dniField; // Campo para el DNI del usuario
    @FXML
    private PasswordField contraseniaField; // Campo para la contraseña
    @FXML
    private TextField passwordTextField; // Campo para la contraseña
    @FXML
    private TextField emailUsuarioField;  // Campo para el email del usuario
    @FXML
    private Button verContraseñaButton; // Botón para alternar la visibilidad de la contraseña

    // Campos específicos de Cliente
    @FXML
    private TextField direccionField;
    @FXML
    private Label direccionLabel;
    @FXML
    private TextField telefonoField;
    @FXML
    private Label telefonoLabel;
    @FXML
    private DatePicker fechaNacimientoField;
    @FXML
    private Label fechaNacimientoLabel;

    // Campos específicos de Conserje
    @FXML
    private DatePicker fechaIngresoField;
    @FXML
    private Label fechaIngresoLabel;
    @FXML
    private TextField telefonoConserjeField;
    @FXML
    private Label telefonoConserjeLabel;
    @FXML
    private TextField estadoTrabajoField;
    @FXML
    private Label estadoTrabajoLabel;

    private GestionarUsuariosController gestionarUsuariosController;
    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {

        direccionField.setVisible(false);
        telefonoField.setVisible(false);
        fechaNacimientoField.setVisible(false);
        fechaIngresoField.setVisible(false);
        telefonoConserjeField.setVisible(false);
        estadoTrabajoField.setVisible(false);

        // Limitar la longitud de los campos de texto
        setTextFieldLimit(nombreUsuarioField,30);
        setTextFieldLimit(apellidoField,30);
        setTextFieldLimit(dniField,10);
        setTextFieldLimit(contraseniaField,30);
        setTextFieldLimit(passwordTextField,30);
        setTextFieldLimit(emailUsuarioField,50);
        setTextFieldLimit(telefonoField,15);
        setTextFieldLimit(direccionField,30);
        setTextFieldLimit(telefonoConserjeField,20);
        setTextFieldLimit(telefonoField,30);

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

    // Metodo para alternar la visibilidad de la contraseña
    @FXML
    public void togglePasswordVisibility() {
        // Alternar la visibilidad de los campos
        if (contraseniaField.isVisible()) {
            contraseniaField.setVisible(false);
            passwordTextField.setVisible(true);
            passwordTextField.setText(contraseniaField.getText()); // Sincronizar el texto
            verContraseñaButton.setText("Ocultar");
        } else {
            passwordTextField.setVisible(false);
            contraseniaField.setVisible(true);
            contraseniaField.setText(passwordTextField.getText()); // Sincronizar el texto
            verContraseñaButton.setText("Ver");
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
            LocalDate fechaNacimiento = cliente.getFechaNacimiento(); // Obtener fecha de nacimiento del cliente
            if (fechaNacimiento != null) {
                // Establecer la fecha en el DatePicker sin necesidad de conversión adicional
                fechaNacimientoField.setValue(fechaNacimiento);
            }

            direccionField.setVisible(true);
            direccionLabel.setVisible(true);
            telefonoField.setVisible(true);
            telefonoLabel.setVisible(true);
            fechaNacimientoField.setVisible(true); // Hacer visible el campo de fechaNacimiento
            fechaNacimientoLabel.setVisible(true); // Hacer visible el campo de fechaNacimiento
        } else if (usuario instanceof Conserje) {
            Conserje conserje = (Conserje) usuario;

            // Obtener fecha de ingreso del conserje
            LocalDate fechaIngreso = conserje.getFechaIngreso();
            if (fechaIngreso != null) {
                // Establecer la fecha en el DatePicker (ya es LocalDate)
                fechaIngresoField.setValue(fechaIngreso); // Establecer directamente el LocalDate
            }


            telefonoConserjeField.setText(conserje.getTelefono());
            estadoTrabajoField.setText(conserje.getEstadoTrabajo());

            fechaIngresoField.setVisible(true); // Hacer visible el campo de fechaIngreso
            fechaIngresoLabel.setVisible(true); // Hacer visible el campo de fechaIngreso
            telefonoConserjeField.setVisible(true);
            telefonoConserjeLabel.setVisible(true);
            estadoTrabajoField.setVisible(true);
            estadoTrabajoLabel.setVisible(true);
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
    private void abrirVentanaModificarContrasenia() throws IOException {
        // Cargar el FXML de la ventana de cambiar contraseña
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/modificarContrasenia.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la nueva ventana
        ModificarContraseniaController controller = loader.getController();

        // Pasar el usuario actual al nuevo controlador
        controller.setUsuario(usuarioSeleccionado);

        // Pasar el controlador actual (ModificarMiUsuarioController) al nuevo controlador
        controller.setControllerAnterior(this);

        // Crear una nueva ventana (Stage) para mostrar la interfaz de modificar contraseña
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Modificar Contraseña");
        stage.show();
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

        // Iniciar lista de errores para capturar las excepciones
        List<String> errores = new ArrayList<>();

        try {
            // Actualizar los campos comunes del usuario
            usuarioSeleccionado.setNombre(nuevoNombre);
            usuarioSeleccionado.setApellido(nuevoApellido);
            usuarioSeleccionado.setCorreoElectronico(nuevoEmail);

            // Si es Cliente, actualizamos los datos específicos
            if (usuarioSeleccionado instanceof Cliente) {
                Cliente cliente = (Cliente) usuarioSeleccionado;
                cliente.setDireccion(direccionField.getText());
                cliente.setTelefono(telefonoField.getText());

                // Asignación de LocalDate a cliente
                if (fechaNacimientoField.getValue() != null) {
                    LocalDate fechaNacimiento = fechaNacimientoField.getValue();
                    cliente.setFechaNacimiento(fechaNacimiento); // No es necesario convertir, ya que setFechaNacimiento recibe LocalDate
                }
            }
            // Si es Conserje, actualizamos los datos específicos de Conserje
            else if (usuarioSeleccionado instanceof Conserje) {
                Conserje conserje = (Conserje) usuarioSeleccionado;
                conserje.setTelefono(telefonoConserjeField.getText());
                conserje.setEstadoTrabajo(estadoTrabajoField.getText());

                // Establecer LocalDate directamente en lugar de convertir a Date
                if (fechaIngresoField.getValue() != null) {
                    LocalDate localDate = fechaIngresoField.getValue();
                    conserje.setFechaIngreso(localDate); // Asignamos el LocalDate directamente
                }
            }


            // Llamar a `actualizarUsuario` para actualizar y guardar el usuario en JSON
            boolean exito = GestionUsuario.getInstancia("HotelManagement_I/usuarios.json").actualizarUsuario(usuarioSeleccionado);

            if (!exito) {
                throw new Exception("No se pudo actualizar el usuario.");
            }

            // Mostrar éxito si se guarda correctamente
            mostrarAlerta("Éxito", "Cambios guardados correctamente.");

        } catch (UsuarioDuplicadoException e) {
            // Manejar duplicados específicamente
            errores.add(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Si hay un problema con los datos de entrada (por ejemplo, contraseñas cortas)
            errores.add("Error de datos: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            errores.add("Error: " + e.getMessage());
        }

        // Si hay errores, mostramos una alerta
        if (!errores.isEmpty()) {
            mostrarAlerta("Errores", String.join("\n", errores));
        } else {
            // Si ttodo ha ido bien, actualizamos la tabla de usuarios
            if (gestionarUsuariosController != null) {
                gestionarUsuariosController.actualizarListaUsuarios();
            }

            // Cerrar la ventana después de guardar los cambios
            cerrarVentana(event);
        }
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

    public void actualizarContrasenia(String nuevaContrasenia) {
        // Actualizar ambos campos de contraseña
        contraseniaField.setText(nuevaContrasenia);
        passwordTextField.setText(nuevaContrasenia);  // También se actualiza el campo visible
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
