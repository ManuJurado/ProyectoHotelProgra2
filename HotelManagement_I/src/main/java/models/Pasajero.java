package models;

public class Pasajero {

    private String nombre;
    private String apellido;
    private String dni;

    public Pasajero() {}

    public Pasajero(String nombre, String apellido, String dni) {
        setNombre(nombre);
        setApellido(apellido);
        setDni(dni);
    }

    // Getter y Setter para nombre con validación
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter para apellido con validación
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Getter y Setter para DNI con validación
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
        }
        if (!dni.matches("\\d+")) {  // Solo números
            throw new IllegalArgumentException("El DNI solo puede contener números.");
        }
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "Pasajero [nombre=" + nombre + ", dni=" + dni + "]";
    }
}
