package services;

import interfaces.Gestionable_I;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import models.Reserva;
import models.Usuarios.*;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.Alert.AlertType;

import manejoJson.GestionJSON;

public class GestionUsuario implements Gestionable_I<Usuario> {

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
                                String direccion, String telefono, List<Reserva> historialReservas, int puntosFidelidad,
                                Date fechaNacimiento) {
        // Crear un nuevo cliente con la fecha de nacimiento (sin formatear aquí)
        Cliente cliente = new Cliente(nombre, apellido, dni, password, correoElectronico, direccion, telefono, historialReservas, puntosFidelidad, fechaNacimiento);

        // Guardar el cliente en el almacenamiento (por ejemplo, archivo JSON)
        guardar(cliente);
        mostrarMensajeTemporal("Guardando nuevo usuario en archivo json");

        return cliente;
    }

    public boolean actualizarUsuario(Usuario usuario, String nuevoNombre, String nuevoApellido, String nuevoEmail) {
        // Buscar al usuario en la lista
        Usuario usuarioAActualizar = buscarPorId(usuario.getDni());

        if (usuarioAActualizar != null) {
            // Actualizar los campos comunes del usuario
            usuarioAActualizar.setNombre(nuevoNombre);
            usuarioAActualizar.setApellido(nuevoApellido);
            usuarioAActualizar.setCorreoElectronico(nuevoEmail);

            // Si es cliente, actualizamos también los datos específicos
            if (usuarioAActualizar instanceof Cliente) {
                Cliente cliente = (Cliente) usuarioAActualizar;
                cliente.setDireccion(((Cliente) usuario).getDireccion());
                cliente.setTelefono(((Cliente) usuario).getTelefono());
                cliente.setFechaNacimiento(((Cliente) usuario).getFechaNacimiento());
            }

            // Si es Conserje, actualizamos los datos específicos de Conserje
            else if (usuarioAActualizar instanceof Conserje) {
                Conserje conserje = (Conserje) usuarioAActualizar;
                // Actualizamos los campos de Conserje
                conserje.setTurno(((Conserje) usuario).getTurno());
                conserje.setNumeroEmpleado(((Conserje) usuario).getNumeroEmpleado());
                conserje.setAreaResponsable(((Conserje) usuario).getAreaResponsable());
                conserje.setTelefono(((Conserje) usuario).getTelefono());
                conserje.setEstadoTrabajo(((Conserje) usuario).getEstadoTrabajo());

                // Convertir la fecha de ingreso de LocalDate a Date
                if (((Conserje) usuario).getFechaIngreso() != null) {
                    conserje.setFechaIngreso(((Conserje) usuario).getFechaIngreso());
                }
            }

            // Guardamos los cambios en el archivo JSON
            guardarUsuariosEnJson();
            return true; // Se actualizó correctamente
        }
        return false; // No se encontró el usuario
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

    public boolean eliminarUsuario(String dni) {
        // Buscar al usuario por su DNI
        Usuario usuarioAEliminar = buscarPorId(dni);
        if (usuarioAEliminar != null) {
            // Si el usuario existe, lo eliminamos de la lista
            usuarios.remove(usuarioAEliminar);
            // Guardamos los cambios en el archivo JSON
            guardarUsuariosEnJson();
            return true; // Retornamos true indicando que se eliminó con éxito
        }
        // Si no encontramos al usuario, retornamos false
        return false;
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

    @Override
    public void guardar(Usuario usuario) {
        usuarios.add(usuario);
        guardarUsuariosEnJson();
    }

    @Override
    public boolean eliminar(String dni) {
        Usuario usuarioAEliminar = buscarPorId(dni);
        if (usuarioAEliminar != null) {
            usuarios.remove(usuarioAEliminar);
            guardarUsuariosEnJson();
            return true;
        }
        return false;
    }

    // Guardar los cambios de los usuarios en el archivo JSON
    private void guardarUsuariosEnJson() {
        try {
            // Aquí guardamos la lista de usuarios en el archivo JSON
            GestionJSON.guardarUsuariosJson(usuarios, "C:/Users/Manu/OneDrive/Escritorio/NuevaRamaManu/ProyectoHotelProgra2/HotelManagement_I/usuarios.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Usuario buscarPorId(String dni) {//busca por DNI
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(dni)) {
                return usuario;
            }
        }
        return null;
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
