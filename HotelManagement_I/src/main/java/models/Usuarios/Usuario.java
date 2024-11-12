package models.Usuarios;

import enums.TipoUsuario;

public abstract class Usuario {
    private String nombre;
    private String apellido;
    private String dni;
    private String correoElectronico;
    private String contrasenia;
    private TipoUsuario tipoUsuario;
    private String habilitacion;

    public Usuario(){}

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

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        this.apellido = apellido;
    }

    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{8,10}")) { // Ejemplo: DNI debe ser de 8 a 10 dígitos
            throw new IllegalArgumentException("El DNI debe contener entre 8 y 10 dígitos numéricos.");
        }
        this.dni = dni;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6) { // Ejemplo: longitud mínima de 6 caracteres
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        this.contrasenia = contrasenia;
    }
    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || !correoElectronico.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("Correo electrónico no válido.");
        }
        this.correoElectronico = correoElectronico;
    }

    // Metodo setter para tipoUsuario
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    // Métodos abstractos
    public abstract boolean iniciarSesion(String contrasenia);
    public abstract void cerrarSesion();


    @Override
    public String toString() {
        return  "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", tipoUsuario=" + tipoUsuario + '\'';
    }
}
