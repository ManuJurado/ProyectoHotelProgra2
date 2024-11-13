package Clases.Usuario;

import java.time.LocalDate;

public class Pasajero {
    private int idPasajero;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String telefono;
    private String email;


    public Pasajero(int idPasajero, String nombre, String apellido, String numeroDocumento, LocalDate fechaNacimiento, String nacionalidad, String telefono, String email) {
        this.idPasajero = idPasajero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDocumento = numeroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.email = email;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPaajero(int idPaajero) {
        this.idPasajero = idPaajero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
