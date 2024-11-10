package controllers.gestionar;

import controllers.BaseController;
import controllers.crear.CrearUsuarioController;
import controllers.modificar.ModificarUsuarioController;
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
import javafx.stage.Stage;
import javafx.scene.Scene;
import models.Usuarios.Usuario;
import services.GestionUsuario;

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
    private TableColumn<Usuario, String> columnaHabilitacion;

    @FXML
    private TextField nombreUsuarioField;
    @FXML
    private TextField emailUsuarioField;

    private GestionUsuario gestionarUsuarios;

    private Usuario usuarioOriginal;

    // Lista que contendrá todos los usuarios
    private ObservableList<Usuario> usuariosOriginales;

    @FXML
    private void initialize() {
        // Configura las columnas de la tabla
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        columnaHabilitacion.setCellValueFactory(new PropertyValueFactory<>("habilitacion"));

        cargarUsuarios(); // Carga usuarios al inicializar

        // Agregar un listener al campo de texto
        nombreUsuarioField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarUsuarios(); // Filtrar usuarios cada vez que cambia el texto
        });
    }

    @FXML
    private void onCrearNuevoUsuarioButtonClick(ActionEvent event) {
        // Lógica para crear un nuevo usuario
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/crear/crearUsuario.fxml"));
            Parent root = loader.load();

            // Asumir que tienes un controlador CrearUsuarioController
            CrearUsuarioController crearUsuarioController = loader.getController();
            crearUsuarioController.setGestionarUsuarios(gestionarUsuarios, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Crear Nuevo Usuario");
            stage.show();
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
                ModificarUsuarioController modificarUsuarioController = loader.getController();

                // Establecer el usuario y el controlador
                modificarUsuarioController.setUsuario(usuarioSeleccionado, this); // usuarioSeleccionado es el usuario que estás modificando

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
    private void filtrarUsuarios() {
        String filtro = nombreUsuarioField.getText().toLowerCase(); // Obtener el texto y pasarlo a minúsculas

        // Si el filtro está vacío, cargar todos los usuarios
        if (filtro.isEmpty()) {
            tablaUsuarios.setItems(usuariosOriginales); // Mostrar todos los usuarios
            return; // Salir del método para evitar la siguiente lógica
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

    public void setGestionarUsuarios(GestionUsuario gestionarUsuarios) {
        this.gestionarUsuarios = gestionarUsuarios;
        configurarColumnas(); // Configura las columnas
        cargarUsuarios(); // Cargar usuarios
    }

    private void configurarColumnas() {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }


    private void cargarUsuarios() {
        if (gestionarUsuarios != null) {
            List<Usuario> usuarios = gestionarUsuarios.getUsuarios();
            System.out.println("Usuarios cargados: " + usuarios.size()); // Depuración

            // Limpiar la tabla antes de agregar nuevos elementos
            tablaUsuarios.getItems().clear();

            // Guardar la lista original de usuarios
            usuariosOriginales = FXCollections.observableArrayList(usuarios);

            // Actualizar la tabla con los usuarios obtenidos
            tablaUsuarios.setItems(usuariosOriginales); // Actualiza la tabla en la UI
        } else {
            System.out.println("GestionarUsuarios es nulo."); // Depuración adicional
        }
    }

    // Método para actualizar la lista de usuarios
    public void actualizarListaUsuarios() {
        cargarUsuarios(); // Simplemente recarga la lista de usuarios
    }

    public void actualizarUsuario(Usuario usuarioOriginal, String nuevoNombre, String nuevoEmail, TipoUsuario nuevoRol) {
        if (usuarioOriginal != null) {
            usuarioOriginal.setNombre(nuevoNombre);
            usuarioOriginal.setCorreoElectronico(nuevoEmail);
            usuarioOriginal.setTipoUsuario(nuevoRol); // También actualizar el rol
            actualizarListaUsuarios(); // Recargar la lista de usuarios para reflejar los cambios
        }
    }

    private void abrirFormularioModificacion(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modificar/modificarUsuario.fxml"));
            Parent root = loader.load();

            ModificarUsuarioController modificarUsuarioController = loader.getController();
            modificarUsuarioController.setUsuario(usuario, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentana(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volverAlMenuAdmin(ActionEvent event) {
        cambiarEscena("/views/menu/menuAdministrador.fxml", "Menú Administrador", (Node) event.getSource());
    }
}
