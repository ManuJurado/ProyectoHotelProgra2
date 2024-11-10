package services;

import exceptions.AtributoFaltanteException;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;
import models.Reserva;
import models.Usuarios.*;
import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.Alert.AlertType;

import manejoJson.GestionJSON;

public class GestionUsuario{

    private static GestionUsuario instancia; // Instancia única de la clase
    private List<Usuario> usuarios;


    // Constructor que carga los usuarios desde el JSON para aplicar la logica ni bien se crea la instancia de GestionUsuario
    private GestionUsuario(String fileName){
        this.usuarios = new ArrayList<>();
        mostrarMensajeTemporal("Cargando lista de usuarios...");
        this.usuarios = GestionJSON.mapeoUsuariosJson(fileName);
    }

    // Metodo para obtener la instancia única de la clase (Singleton)
    public static GestionUsuario getInstancia(String fileName) {
        if (instancia == null) {
            instancia = new GestionUsuario(fileName);
        }
        return instancia;
    }

    // Metodo para crear un nuevo cliente
    public Cliente crearCliente(String nombre, String apellido, String dni, String password, String correoElectronico,
                                String direccion, String telefono, List<Reserva> historialReservas, int puntosFidelidad) {
        Cliente cliente = new Cliente(nombre, apellido, dni, password, correoElectronico, direccion, telefono, historialReservas, puntosFidelidad, new Date());
        usuarios.add(cliente);
        guardarUsuariosEnJson();
        mostrarMensajeTemporal("Guardando nuevo usuario en archivo json");
        return cliente;
    }

    // Metodo para crear un nuevo conserje
    public Conserje crearConserje(String nombre, String apellido, String dni, String password, String correoElectronico,
                                  String turno, String numeroEmpleado, String areaResponsable, String telefono, String estadoTrabajo) {
        Conserje conserje = new Conserje(nombre, apellido, dni, password, correoElectronico, turno, numeroEmpleado, new Date(), areaResponsable, telefono, estadoTrabajo);
        usuarios.add(conserje);
        return conserje;
    }

    // Metodo para crear un nuevo administrador
    public Administrador crearAdministrador(String nombre, String apellido, String dni, String password, String correoElectronico) {
        Administrador administrador = new Administrador(nombre, apellido, dni, password, correoElectronico);
        usuarios.add(administrador);
        return administrador;
    }

    // Metodo para obtener todos los usuarios
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    // Metodo para habilitar/inhabilitar usuario (cambiar el rol)
    public void habilitarInhabilitarUsuario(Usuario usuario) {
        if (usuario != null) {
            String nuevoRol = usuario.getHabilitacion().equals("habilitado") ? "inhabilitado" : "habilitado";
            usuario.setHabilitacion(nuevoRol);
        }
    }

    // Eliminar un usuario por su DNI
    public boolean eliminarUsuario(String dni) {
        Usuario usuarioAEliminar = buscarUsuarioPorDni(dni);
        if (usuarioAEliminar != null) {
            usuarios.remove(usuarioAEliminar);
            guardarUsuariosEnJson(); // Guardamos los cambios
            return true; // Usuario eliminado con éxito
        }
        return false; // Usuario no encontrado
    }

    // Buscar un usuario por su DNI
    private Usuario buscarUsuarioPorDni(String dni) {
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(dni)) {
                return usuario;
            }
        }
        return null; // Usuario no encontrado
    }

    // Metodo para verificar si ya existe un usuario con el mismo DNI
    public boolean existeUsuarioConDni(String dni) {
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(dni)) {
                return true; // Si se encuentra el DNI, retornamos true
            }
        }
        return false; // Si no se encuentra el DNI, retornamos false
    }

    // Metodo para verificar si ya existe un usuario con el mismo correo electrónico
    public boolean existeUsuarioConCorreo(String correoElectronico) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreoElectronico().equals(correoElectronico)) {
                return true; // Si se encuentra el correo electrónico, retornamos true
            }
        }
        return false; // Si no se encuentra el correo electrónico, retornamos false
    }


    // Guardar los cambios de los usuarios en el archivo JSON
    private void guardarUsuariosEnJson() {
        try {
            // Aquí guardamos la lista de usuarios en el archivo JSON
            GestionJSON.guardarUsuariosJson(usuarios, "C:/Users/emili/OneDrive/Documentos/Tecnicatura Progra/2024/Programacion 2 (Java)/TP_FINAL_RAMA_HABITACIONES/ProyectoHotelProgra2/HotelManagement_I/usuarios.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para mostrar el mensaje temporal
    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Mostrar el alert
        alert.show();

        // Usamos un Timeline para cerrar el alert después de 2 segundos (2000 milisegundos)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }

}
