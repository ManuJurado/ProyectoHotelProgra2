package models.Usuarios;

import enums.TipoUsuario;

import java.util.Date;

public class Conserje extends Usuario {

    private Date fechaIngreso;
    private String telefono;
    private String estadoTrabajo;

    public Conserje(String nombre, String apellido, String dni, String contrasenia, String correoElectronico,
                    Date fechaIngreso, String telefono, String estadoTrabajo) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.CONSERJE);
        this.fechaIngreso = fechaIngreso;
        this.telefono = telefono;
        this.estadoTrabajo = estadoTrabajo;
    }

    public Conserje(){
        super();
        super.setTipoUsuario(TipoUsuario.CONSERJE);
    }

    public void setFechaIngreso(Date fechaIngreso) {
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

    @Override
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        super.setNombre(nombre);
    }

    @Override
    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        super.setApellido(apellido);
    }

    @Override
    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{8,10}")) {  // Validación para 8-10 dígitos numéricos
            throw new IllegalArgumentException("El DNI debe contener entre 8 y 10 dígitos numéricos.");
        }
        super.setDni(dni);
    }

    @Override
    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6) {  // Longitud mínima de 6 caracteres
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        super.setContrasenia(contrasenia);
    }

    @Override
    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || !correoElectronico.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("Correo electrónico no válido.");
        }
        super.setCorreoElectronico(correoElectronico);
    }

    public Date getFechaIngreso() { return fechaIngreso; }
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
