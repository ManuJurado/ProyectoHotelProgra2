package models;

public class Pasajero {

    private String nombre;
    private String apellido;
    private String dni;

    public Pasajero() {}

    public Pasajero(String nombre, String apellido, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String toString() {
        return "Pasajero [nombre=" + nombre + ", dni=" + dni + "]";
    }
}