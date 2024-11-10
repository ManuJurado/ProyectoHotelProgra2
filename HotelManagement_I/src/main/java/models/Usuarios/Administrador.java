package models.Usuarios;

import enums.TipoUsuario;

public class Administrador extends Usuario {

    public Administrador(String nombre, String apellido, String dni, String contrasenia, String correoElectronico) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.ADMINISTRADOR);
    }

    // Métodos específicos de Administrador
    public void crearUsuario() {
    }

    public void gestionarBackup() {
        System.out.println("Backup gestionado.");
    }

    public void borrarUsuario() {
        System.out.println("Usuario borrado.");
    }

    public void modificarUsuario() {
        System.out.println("Usuario modificado.");
    }

    @Override
    public boolean iniciarSesion(String contrasenia) {
        return this.getContrasenia().equals(contrasenia);
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Administrador " + getNombre() + " ha cerrado sesión.");
    }

}
