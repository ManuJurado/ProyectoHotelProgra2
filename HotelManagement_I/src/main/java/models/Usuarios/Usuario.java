package models.Usuarios;

import enums.TipoUsuario;

public abstract class Usuario {
    private String nombre;
    private String apellido;
    private String dni;
    private String contrasenia;
    private String correoElectronico;
    private TipoUsuario tipoUsuario;
    private String habilitacion;



    public Usuario(String nombre, String apellido, String dni, String contrasenia, String correoElectronico, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.contrasenia = contrasenia;
        this.correoElectronico = correoElectronico;
        this.tipoUsuario = tipoUsuario;
        this.habilitacion = "habilitado";
    }

    // Métodos getters y setters

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getContrasenia() { return contrasenia; }
    public String getCorreoElectronico() { return correoElectronico; }
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public String getHabilitacion() {
        return habilitacion;
    }

    public void setHabilitacion(String habilitacion) {
        this.habilitacion = habilitacion;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    // Metodo setter para tipoUsuario
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    // Métodos abstractos
    public abstract boolean iniciarSesion(String contrasenia);
    public abstract void cerrarSesion();


    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
