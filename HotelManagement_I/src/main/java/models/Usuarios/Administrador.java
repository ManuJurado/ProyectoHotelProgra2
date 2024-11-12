package models.Usuarios;

import enums.TipoUsuario;

public class Administrador extends Usuario {

    public Administrador(){}

    public Administrador(String nombre, String apellido, String dni, String contrasenia, String correoElectronico) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.ADMINISTRADOR);
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public String getApellido() {
        return super.getApellido();
    }

    @Override
    public String getDni() {
        return super.getDni();
    }

    @Override
    public String getContrasenia() {
        return super.getContrasenia();
    }

    @Override
    public String getCorreoElectronico() {
        return super.getCorreoElectronico();
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return super.getTipoUsuario();
    }

    @Override
    public String getHabilitacion() {
        return super.getHabilitacion();
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

    @Override
    public String toString() {
        return "Administrador{" +
                "nombre='" + super.getNombre() + '\'' +
                ", apellido='" + super.getApellido() + '\'' +
                ", dni='" + super.getDni() + '\'' +
                ", contrasenia='" + super.getContrasenia() + '\'' +
                ", correoElectronico='" + super.getCorreoElectronico() + '\'' +
                ", tipoUsuario=" + super.getTipoUsuario() +
                '}'+"\n";
    }
}
