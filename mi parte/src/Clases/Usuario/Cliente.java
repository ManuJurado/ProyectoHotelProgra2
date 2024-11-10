package Clases.Usuario;

import java.util.Date;


public class Cliente extends Usuario {

    private String direccion;
    private String telefono;
    private int puntosFidelidad;
    private Date fechaNacimiento;

    public Cliente(String nombre, String apellido, String dni, String email, String password, String direccion, String telefono, int puntosFidelidad, Date fechaNacimiento) {
        super(nombre, apellido, dni, email, password);
        this.direccion = direccion;
        this.telefono = telefono;
        this.puntosFidelidad = puntosFidelidad;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Cliente() {
        super();
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre=" + getNombre() + '\'' +
                "apellido=" + getApellido() + '\'' +
                "dni=" +getDni()+ '\'' +
                "email=" + getEmail() +  '\'' +
                "direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", puntosFidelidad=" + puntosFidelidad +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}
