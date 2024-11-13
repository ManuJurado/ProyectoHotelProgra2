package models.Usuarios;

import enums.TipoUsuario;

import java.time.LocalDate;
import java.util.Date;

public class Conserje extends Usuario {

    private LocalDate fechaIngreso;
    private String telefono;
    private String estadoTrabajo;

    public Conserje(String nombre, String apellido, String dni, String contrasenia, String correoElectronico,
                    LocalDate fechaIngreso, String telefono, String estadoTrabajo) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.CONSERJE);
        this.fechaIngreso = fechaIngreso;
        this.telefono = telefono;
        this.estadoTrabajo = estadoTrabajo;
    }

    public Conserje(){
        super();
        super.setTipoUsuario(TipoUsuario.CONSERJE);
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("^\\+?\\d{10,15}$")) {
            throw new IllegalArgumentException("Número de teléfono no válido.");
        }
        this.telefono = telefono;
    }

    public void setEstadoTrabajo(String estadoTrabajo) {
        if (estadoTrabajo == null || estadoTrabajo.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de trabajo no puede estar vacío.");
        }
        this.estadoTrabajo = estadoTrabajo;
    }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public String getTelefono() { return telefono; }
    public String getEstadoTrabajo() { return estadoTrabajo; }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public String getApellido() {
        return super.getApellido();
    }

    @Override
    public String getDni() {
        return super.getDni();
    }

    @Override
    public String getContrasenia() {
        return super.getContrasenia();
    }

    @Override
    public String getCorreoElectronico() {
        return super.getCorreoElectronico();
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return super.getTipoUsuario();
    }

    @Override
    public String getHabilitacion() {
        return super.getHabilitacion();
    }

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
                super.toString() +
                ", fechaIngreso=" + fechaIngreso +
                ", telefono='" + telefono + '\'' +
                ", estadoTrabajo='" + estadoTrabajo +
                '}'+"\n";
    }
}
