package models.Usuarios;

import enums.TipoUsuario;
import exceptions.FechaInvalidaException;
import models.Reserva;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;
import java.util.List;

public class Cliente extends Usuario {
    private String direccion;
    private String telefono;
    private List<Reserva> historialReservas;
    private int puntosFidelidad;
    private Date fechaNacimiento;

    public Cliente(){
        super.setTipoUsuario(TipoUsuario.CLIENTE);
    }

    public Cliente(String nombre, String apellido, String dni, String contrasenia, String correoElectronico,
                   String direccion, String telefono, List<Reserva> historialReservas, int puntosFidelidad, Date fechaNacimiento) {
        super(nombre, apellido, dni, contrasenia, correoElectronico, TipoUsuario.CLIENTE);
        this.direccion = direccion;
        this.telefono = telefono;
        this.historialReservas = historialReservas;
        this.puntosFidelidad = puntosFidelidad;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Métodos getters y setters para los atributos
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public int getPuntosFidelidad() { return puntosFidelidad; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public List<Reserva> getHistorialReservas() { return historialReservas; }

    //setters
    public void setHistorialReservas(List<Reserva> historialReservas) { this.historialReservas = historialReservas; }
    public void setPuntosFidelidad(int puntosFidelidad) { this.puntosFidelidad = puntosFidelidad; }

    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        this.direccion = direccion;
    }
    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.matches("^\\+?\\d{10,15}$")) {
            throw new IllegalArgumentException("Número de teléfono no válido.");
        }
        this.telefono = telefono;
    }

    public void setFechaNacimiento(Date fechaNacimiento) throws FechaInvalidaException {
        // Verificar si la fecha de nacimiento es nula o si es una fecha futura
        if (fechaNacimiento == null || fechaNacimiento.after(new Date())) {
            throw new FechaInvalidaException("La fecha de nacimiento no puede ser futura o nula.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }



    // Métodos específicos de Cliente
    public void hacerReserva() {
        // Lógica para realizar una reserva
    }

    public void cancelarReserva() {
        // Lógica para cancelar una reserva
    }

    public Cliente guardarNuevoCliente(String nombre, String apellido, String contrasenia, String correoElectronico) {
        // Validación de nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        // Validación de apellido
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }

        // Validación de correo electrónico
        if (correoElectronico == null || !correoElectronico.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Correo electrónico no válido.");
        }

        // Validación de contraseña
        if (contrasenia == null || contrasenia.length() < 6) { // Ejemplo: longitud mínima de 6 caracteres
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }

        // Asignación de valores
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setContrasenia(contrasenia);
        this.setCorreoElectronico(correoElectronico);

        // Retornar el cliente actualizado
        return this;
    }

    public void agregarReserva(Reserva reserva) {
        historialReservas.add(reserva);
    }

    @Override
    public boolean iniciarSesion(String contrasenia) {
        return this.getContrasenia().equals(contrasenia);
    }

    @Override
    public void cerrarSesion() {
    }

    @Override
    public String toString() {
        return "Cliente{" +
                super.toString() +
                "direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", historialReservas=" + historialReservas +
                ", puntosFidelidad=" + puntosFidelidad +
                ", fechaNacimiento=" + fechaNacimiento +
                '}'+"\n";
    }
}
