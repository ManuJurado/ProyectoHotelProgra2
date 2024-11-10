package Clases.Usuario;

import java.util.Date;

public class Conserje extends Usuario {

    private String turno;
    private String numeroEmpleado;
    private Date fechaIngreso;
    private String areaResponsable;
    private String telefono;
    private String estadoTrabajo;

    public Conserje(String nombre, String apellido, String dni,String email, String password, String turno, String numeroEmpleado, Date fechaIngreso, String areaResponsable, String telefono, String estadoTrabajo) {
        super(nombre, apellido, dni,email, password);
        this.turno = turno;
        this.numeroEmpleado = numeroEmpleado;
        this.fechaIngreso = fechaIngreso;
        this.areaResponsable = areaResponsable;
        this.telefono = telefono;
        this.estadoTrabajo = estadoTrabajo;
    }

    public Conserje() {
        super();
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstadoTrabajo() {
        return estadoTrabajo;
    }

    public void setEstadoTrabajo(String estadoTrabajo) {
        this.estadoTrabajo = estadoTrabajo;
    }

    @Override
    public String toString() {
        return "Conserje{" +
                "nombre=" + getNombre() + '\'' +
                "apellido=" + getApellido() + '\'' +
                "dni=" +getDni()+ '\'' +
                "email=" + getEmail() +  '\'' +
                "turno='" + turno + '\'' +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", areaResponsable='" + areaResponsable + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estadoTrabajo='" + estadoTrabajo + '\'' +
                '}';
    }
}
