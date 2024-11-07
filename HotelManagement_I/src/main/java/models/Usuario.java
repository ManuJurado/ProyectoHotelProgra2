package models;

public class Usuario {
    private String nombre;
    private String email;
    private String rol; // Podría ser algo como "administrador", "cliente", etc.
    private String habilitacion;

    // Constructor
    public Usuario(String nombre, String email, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.habilitacion = "habilitado";
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getHabilitacion() {
        return habilitacion;
    }

    public void setHabilitacion(String habilitacion) {
        this.habilitacion = habilitacion;
    }

    public void cambiarHabilitacion(String habilitacion) {
        if (this.habilitacion == "Habilitado"){
            this.habilitacion = "Inhabilitado";
        }else {
            this.habilitacion = "Habilitado";
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", habilitacion='" + habilitacion + '\'' +
                '}';
    }
}
