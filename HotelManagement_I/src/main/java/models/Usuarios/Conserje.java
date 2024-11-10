package models.Usuarios;

import enums.TipoUsuario;

import java.util.Date;

public class Conserje extends Usuario {
    private String turno;
    private String numeroEmpleado;
    private Date fechaIngreso;
    private String areaResponsable;
    private String telefono;
    private String estadoTrabajo;

    public Conserje(String nombre, String apellido, String dni, String contrasenia, String correoElectronico,
                    String turno, String numeroEmpleado, Date fechaIngreso, String areaResponsable, String telefono, String estadoTrabajo) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.CONSERJE);
        this.turno = turno;
        this.numeroEmpleado = numeroEmpleado;
        this.fechaIngreso = fechaIngreso;
        this.areaResponsable = areaResponsable;
        this.telefono = telefono;
        this.estadoTrabajo = estadoTrabajo;
    }

    // Métodos getters y setters

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }
    public Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getAreaResponsable() { return areaResponsable; }
    public void setAreaResponsable(String areaResponsable) { this.areaResponsable = areaResponsable; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEstadoTrabajo() { return estadoTrabajo; }
    public void setEstadoTrabajo(String estadoTrabajo) { this.estadoTrabajo = estadoTrabajo; }

    // Métodos específicos de Conserje
    public void gestionarCheckIn() {
        System.out.println("Check-in gestionado.");
    }

    public void gestionarCheckOut() {
        System.out.println("Check-out gestionado.");
    }

    public void realizarReserva() {
        System.out.println("Reserva realizada.");
    }

    @Override
    public boolean iniciarSesion(String contrasenia) {
        return this.getContrasenia().equals(contrasenia);
    }

    @Override
    public void cerrarSesion() {
        System.out.println("Conserje " + getNombre() + " ha cerrado sesión.");
    }

    @Override
    public String toString() {
        return "Conserje{" +
                "turno='" + turno + '\'' +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", areaResponsable='" + areaResponsable + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estadoTrabajo='" + estadoTrabajo + '\'' +
                '}';
    }
}
