package Clases;

public class Pasajero {

    private String nombre;
    private String apellido;
    private String dni;
    private String domicilio;

    public Pasajero(String nombre, String apellido, String dni, String domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
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

    public String getDomicilio() {
        return domicilio;
    }

    public String toString() {
        return "Pasajero [nombre=" + nombre + ", dni=" + dni + "]";
    }
}
