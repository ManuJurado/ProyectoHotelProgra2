package services;

import exceptions.FechaInvalidaException;
import exceptions.UsuarioDuplicadoException;
import interfaces.Gestionable_I;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
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
        try {
            guardar(cliente);
        } catch (UsuarioDuplicadoException e) {
            throw new RuntimeException("El usuario se encuentra duplicado...");
        }
        mostrarMensajeTemporal("Guardando nuevo cliente en archivo json");

        return cliente;
    }

    public boolean actualizarUsuario(Usuario usuario) throws UsuarioDuplicadoException {
        // Verificar si el nuevo correo electrónico ya está en uso por otro usuario
        for (Usuario u : usuarios) {
            if (!u.getDni().equals(usuario.getDni()) && u.getCorreoElectronico().equalsIgnoreCase(usuario.getCorreoElectronico())) {
                throw new UsuarioDuplicadoException("El correo electrónico '" + usuario.getCorreoElectronico() + "' ya está en uso.");
            }
        }

        // Buscar al usuario en la lista
        Usuario usuarioAActualizar = buscarPorId(usuario.getDni());

        if (usuarioAActualizar != null) {
            // Actualizar los campos comunes
            usuarioAActualizar.setNombre(usuario.getNombre());
            usuarioAActualizar.setApellido(usuario.getApellido());
            usuarioAActualizar.setCorreoElectronico(usuario.getCorreoElectronico());

            // Si la contraseña fue modificada, actualizarla
            if (usuario.getContrasenia() != null && !usuario.getContrasenia().isEmpty()) {
                usuarioAActualizar.setContrasenia(usuario.getContrasenia());
            }

            // Actualizar campos específicos dependiendo del tipo de usuario
            if (usuarioAActualizar instanceof Cliente) {
                Cliente clienteAActualizar = (Cliente) usuarioAActualizar;
                Cliente clienteNuevo = (Cliente) usuario;

                // Actualizar atributos específicos de Cliente
                clienteAActualizar.setDireccion(clienteNuevo.getDireccion());
                clienteAActualizar.setTelefono(clienteNuevo.getTelefono());
                try {
                    clienteAActualizar.setFechaNacimiento(clienteNuevo.getFechaNacimiento());
                } catch (FechaInvalidaException e) {
                    throw new RuntimeException(e);
                }
            } else if (usuarioAActualizar instanceof Conserje) {
                Conserje conserjeAActualizar = (Conserje) usuarioAActualizar;
                Conserje conserjeNuevo = (Conserje) usuario;

                // Actualizar atributos específicos de Conserje
                conserjeAActualizar.setFechaIngreso(conserjeNuevo.getFechaIngreso());
                conserjeAActualizar.setTelefono(conserjeNuevo.getTelefono());
                conserjeAActualizar.setEstadoTrabajo(conserjeNuevo.getEstadoTrabajo());
            }

            // Si es un Administrador, no es necesario hacer cambios adicionales porque los atributos comunes ya se actualizan.

            // Guardar los cambios en el archivo JSON
            guardarUsuariosEnJson();
            return true; // Se actualizó correctamente
        }
        return false; // No se encontró el usuario
    }



    // Metodo para crear un nuevo conserje
    public Conserje crearConserje(String nombre, String apellido, String dni, String password, String correoElectronico,
                                  Date fechaIngreso, String telefono, String estadoTrabajo) {
        Conserje conserje = new Conserje(nombre, apellido, dni, password, correoElectronico, fechaIngreso, telefono, estadoTrabajo);
        try {
            guardar(conserje);
        } catch (UsuarioDuplicadoException e) {
            throw new RuntimeException("El usuario se encuentra duplicado...");
        }
        return conserje;
    }

    // Metodo para crear un nuevo administrador
    public Administrador crearAdministrador(String nombre, String apellido, String dni, String password, String correoElectronico) {
        Administrador administrador = new Administrador(nombre, apellido, dni, password, correoElectronico);
        try {
            guardar(administrador);
        }catch (UsuarioDuplicadoException e){
            throw new RuntimeException("El usuario se encuentra duplicado...");
        }
        mostrarMensajeTemporal("Guardando nuevo administrador en archivo json");
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
    public void guardar(Usuario usuario) throws UsuarioDuplicadoException {
        // Verificar si el DNI ya existe
        if (existeUsuarioConDni(usuario.getDni())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con el DNI: " + usuario.getDni());
        }
        // Verificar si el correo electrónico ya existe
        if (existeUsuarioConCorreo(usuario.getCorreoElectronico())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con el correo electrónico: " + usuario.getCorreoElectronico());
        }
        // Si no existen duplicados, añadimos el usuario y guardamos en el JSON
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
            GestionJSON.guardarUsuariosJson(usuarios, "HotelManagement_I/usuarios.json");
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
