package controllers.gestionar;

import controllers.BaseController;
import controllers.crear.CrearUsuarioController;
import controllers.modificar.ModificarMiUsuarioController;
import enums.TipoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import models.Usuarios.Administrador;
import models.Usuarios.Usuario;
import services.GestionUsuario;
import services.Sesion;

import java.io.IOException;
import java.util.List;

public class GestionarUsuariosController extends BaseController {

    @FXML
    public TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> columnaNombre;
    @FXML
    private TableColumn<Usuario, String> columnaEmail;
    @FXML
    private TableColumn<Usuario, String> columnaRol;
    @FXML
    private TableColumn<Usuario, String> columnaDni; // Columna DNI

    @FXML
    private TextField nombreUsuarioField;
    @FXML
    private TextField dniUsuarioField; // Campo para el DNI

    private GestionUsuario gestionarUsuarios;

    // Lista que contendrá todos los usuarios
    private ObservableList<Usuario> usuariosOriginales;

    @FXML
    private void initialize() {
        // Configura las columnas de la tabla
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        columnaDni.setCellValueFactory(new PropertyValueFactory<>("dni"));  // Aquí agregamos la columna DNI


        // Obtener la instancia de GestionUsuario para cargar los usuarios
        this.gestionarUsuarios = GestionUsuario.getInstancia("C:/Users/Manu/OneDrive/Escritorio/NuevaRamaManu/ProyectoHotelProgra2/HotelManagement_I/usuarios.json");

        cargarUsuarios(); // Carga los usuarios al inicializar

        // Agregar un listener al campo de texto
        nombreUsuarioField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuariosPorNombre(); // Filtrar usuarios cada vez que cambia el texto
        });
    }

    private Scene previousScene;  // Cambiar a Scene en vez de Stage

    // Metodo para establecer la escena anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @FXML
    public void onCrearNuevoUsuarioButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearUsuario.fxml"));
            Parent root = loader.load();

            CrearUsuarioController crearUsuarioController = loader.getController();

            // Obtener el Stage actual (ventana principal)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Pasamos el Stage actual al controlador de la ventana emergente
            crearUsuarioController.setPreviousScene(previousScene); // Establecer la escena anterior

            // Crear una ventana nueva para seleccionar el tipo de usuario
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Crear Nuevo Usuario");
            newStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta que se cierre esta
            newStage.showAndWait();

            // Después de cerrar la ventana emergente, obtenemos el tipo de usuario seleccionado
            TipoUsuario tipoUsuario = crearUsuarioController.getTipoUsuarioSeleccionado();
            if (tipoUsuario != null) {
                // Cargar el formulario correspondiente en la ventana principal
                switch (tipoUsuario) {
                    case CLIENTE:
                        cambiarEscenaConSceneAnterior("/views/crear/crearCliente.fxml", "Creación de Cliente", (Node) event.getSource());
                        break;
                    case ADMINISTRADOR:
                        cambiarEscenaConSceneAnterior("/views/crear/crearAdministrador.fxml", "Creación de Administrador", (Node) event.getSource());
                        break;
                    case CONSERJE:
                        cambiarEscenaConSceneAnterior("/views/crear/crearConserje.fxml", "Creación de Conserje", (Node) event.getSource());
                        break;
                    default:
                        System.out.println("Rol no válido");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onModificarUsuarioButtonClick(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem(); // Obtener el usuario seleccionado
        if (usuarioSeleccionado != null) {
            try {
                // Cargar el FXML de ModificarUsuario
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/modificarUsuario.fxml"));
                Parent root = loader.load();

                // Obtener el controlador
                ModificarMiUsuarioController modificarMiUsuarioController = loader.getController();

                // Establecer el usuario seleccionado en el controlador
                modificarMiUsuarioController.setUsuarioSeleccionado(usuarioSeleccionado); // Pasar el usuario seleccionado
                modificarMiUsuarioController.setGestionarUsuariosController(this); // Pasar el controlador principal

                System.out.println(usuarioSeleccionado);

                // Crear y mostrar la nueva ventana
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modificar Usuario");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona un usuario para modificar.");
        }
    }



    @FXML
    private void onHabilitarInhabilitarUsuario(ActionEvent event) {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            String nuevoRol = usuarioSeleccionado.getHabilitacion().equals("habilitado") ? "inhabilitado" : "habilitado";
            usuarioSeleccionado.setHabilitacion(nuevoRol);
            actualizarListaUsuarios(); // Actualiza la lista para reflejar los cambios
        } else {
            mostrarAlerta("Advertencia", "Por favor, selecciona un usuario para habilitar/inhabilitar.");
        }
    }

    @FXML
    private void onEliminarUsuarioButtonClick() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem(); // Obtener el usuario seleccionado

        if (usuarioSeleccionado != null) {
            // Mostramos una alerta de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Está seguro de eliminar al usuario?");
            alert.setContentText("Esta acción no se puede deshacer.");

            // Si el administrador acepta
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Abrir ventana para ingresar la contraseña del administrador
                    mostrarVentanaConfirmarContraseña(usuarioSeleccionado);
                }
            });
        } else {
            // Si no se seleccionó ningún usuario
            mostrarAlerta("Error","Por favor, seleccione un usuario para eliminar.");
        }
    }

    private void mostrarVentanaConfirmarContraseña(Usuario usuarioSeleccionado) {
        // Crear una nueva ventana (nuevo Stage) para ingresar la contraseña
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmación de Contraseña");
        dialog.setHeaderText("Ingrese la contraseña del administrador:");

        // Campo para ingresar la contraseña
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        // Botón de confirmar
        ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        // Agregar el campo de contraseña al dialog
        dialog.getDialogPane().setContent(passwordField);

        // Esperar la respuesta del usuario
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarButtonType) {
                return passwordField.getText(); // Obtener la contraseña ingresada
            }
            return null;
        });

        // Mostrar el diálogo
        dialog.showAndWait().ifPresent(password -> {
            // Comparar la contraseña ingresada con la del usuario logueado
            Usuario usuarioLogueado = Sesion.getUsuarioLogueado(); // Obtener el usuario logueado

            if (usuarioLogueado != null && usuarioLogueado.getContrasenia().equals(password)) {
                // Si la contraseña es correcta, procedemos con la eliminación
                if (usuarioLogueado.getDni().equals(usuarioSeleccionado.getDni())) {
                    // Si el usuario a eliminar es el mismo que el logueado, cerramos la aplicación
                    mostrarAlerta("Error","No puede eliminarse a sí mismo. Cerrando sesión...");
                    cerrarAplicacion();
                } else if (usuarioSeleccionado instanceof Administrador) {
                    // Validar si el usuario seleccionado es un Administrador
                    mostrarAlerta("Error","No puede eliminar a otro Administrador.");
                } else {
                    // Si no es el usuario logueado ni un administrador, procedemos a eliminarlo
                    GestionUsuario gestionUsuario = GestionUsuario.getInstancia("usuarios.json");
                    boolean eliminado = gestionUsuario.eliminarUsuario(usuarioSeleccionado.getDni());

                    if (eliminado) {
                        // Si se eliminó correctamente, actualizar la tabla
                        tablaUsuarios.getItems().remove(usuarioSeleccionado);
                        mostrarAlerta("Éxito","Usuario eliminado con éxito.");
                    } else {
                        mostrarAlerta("Error","Error: No se pudo eliminar el usuario.");
                    }
                }
            } else {
                // Si la contraseña es incorrecta, mostramos un mensaje de error
                mostrarAlerta("Error","Contraseña incorrecta. No se puede eliminar el usuario.");
            }
        });
    }


    @FXML
    private void filtrarUsuariosPorNombre() {
        String filtro = nombreUsuarioField.getText().toLowerCase(); // Obtener el texto y pasarlo a minúsculas

        // Si el filtro está vacío, cargar todos los usuarios
        if (filtro.isEmpty()) {
            tablaUsuarios.setItems(usuariosOriginales); // Mostrar todos los usuarios
            return; // Salir del metodo para evitar la siguiente lógica
        }

        // Filtrar la lista original de usuarios
        ObservableList<Usuario> usuariosFiltrados = FXCollections.observableArrayList();

        // Recorrer la lista original de usuarios y agregar a la lista filtrada si coincide
        for (Usuario usuario : usuariosOriginales) {
            if (usuario.getNombre().toLowerCase().contains(filtro)) {
                usuariosFiltrados.add(usuario);
            }
        }
        // Actualizar la tabla con los usuarios filtrados
        tablaUsuarios.setItems(usuariosFiltrados);
    }

    @FXML
    private void filtrarUsuariosPorDni() {
        String filtro = dniUsuarioField.getText().toLowerCase(); // Obtener el texto del DNI en minúsculas

        // Si el filtro está vacío, cargar todos los usuarios
        if (filtro.isEmpty()) {
            tablaUsuarios.setItems(usuariosOriginales); // Mostrar todos los usuarios
            return; // Salir del metodo si el filtro está vacío
        }

        // Filtrar la lista original de usuarios
        ObservableList<Usuario> usuariosFiltrados = FXCollections.observableArrayList();

        // Recorrer la lista original de usuarios y agregar a la lista filtrada si el DNI coincide
        for (Usuario usuario : usuariosOriginales) {
            if (usuario.getDni().toLowerCase().contains(filtro)) { // Filtrar por DNI
                usuariosFiltrados.add(usuario);
            }
        }

        // Actualizar la tabla con los usuarios filtrados
        tablaUsuarios.setItems(usuariosFiltrados);
    }

    private void cargarUsuarios() {
        // Obtener los usuarios desde el singleton de GestionUsuario
        List<Usuario> usuarios = gestionarUsuarios.getUsuarios();

        // Limpiar la tabla antes de agregar nuevos elementos
        tablaUsuarios.getItems().clear();

        // Guardar la lista original de usuarios
        usuariosOriginales = FXCollections.observableArrayList(usuarios);

        // Actualizar la tabla con los usuarios obtenidos
        tablaUsuarios.setItems(usuariosOriginales); // Actualiza la tabla en la UI
    }

    public void actualizarListaUsuarios() {
        // Recargar los usuarios desde el archivo JSON
        cargarUsuarios(); // Recarga la lista de usuarios desde el archivo JSON

        // Actualiza la tabla con los usuarios obtenidos
        tablaUsuarios.setItems(usuariosOriginales); // Actualiza la tabla en la UI
    }

    public void actualizarUsuario(Usuario usuarioOriginal, String nuevoNombre, String nuevoEmail, TipoUsuario nuevoRol) {
        if (usuarioOriginal != null) {
            usuarioOriginal.setNombre(nuevoNombre);
            usuarioOriginal.setCorreoElectronico(nuevoEmail);
            usuarioOriginal.setTipoUsuario(nuevoRol); // También actualizar el rol
            actualizarListaUsuarios(); // Recargar la lista de usuarios para reflejar los cambios
        }
    }

    // Metodo que se llamará al hacer clic en el botón "Volver"
    @FXML
    private void volverAlMenuAdmin(ActionEvent event) {
        volverAEscenaAnterior(event,previousScene);
    }

    private void cerrarAplicacion() {
        try {
            // Obtener el stage actual (de la ventana de gestión de usuarios)
            Stage stage = (Stage) tablaUsuarios.getScene().getWindow();

            // Cerrar la ventana actual
            stage.close();

            // Cargar la escena de login (deberías tener un archivo FXML de login, por ejemplo "login.fxml")
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menu/login.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena y establecerla en un nuevo Stage
            Stage loginStage = new Stage();
            loginStage.setTitle("Inicio de Sesión");
            loginStage.setScene(new Scene(root));

            // Mostrar la ventana de login
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("error","Hubo un error al intentar reiniciar la aplicación.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
