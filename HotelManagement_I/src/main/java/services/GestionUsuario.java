package services;

import models.Reserva;
import models.Usuarios.*;
import org.json.JSONException;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import static manejoJson.GestionJSON.mapeoUsuariosJson;

public class GestionUsuario {

    private List<Usuario> usuarios;

    // Constructor que carga los usuarios desde el JSON para aplicar la logica ni bien se crea la instancia de GestionUsuario
    public GestionUsuario(String fileName) {
        this.usuarios = new ArrayList<>();
        try {
            this.usuarios = mapeoUsuariosJson(fileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Metodo para crear un nuevo cliente
    public Cliente crearCliente(String nombre, String apellido, String dni, String password, String correoElectronico,
                                String direccion, String telefono, List<Reserva> historialReservas, int puntosFidelidad) {
        Cliente cliente = new Cliente(nombre, apellido, dni, password, correoElectronico, direccion, telefono, historialReservas, puntosFidelidad, new Date());
        usuarios.add(cliente);
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
}
