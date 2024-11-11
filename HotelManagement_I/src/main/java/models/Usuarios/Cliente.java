package models.Usuarios;

import enums.TipoUsuario;
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

    public void setFechaNacimiento(Date fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.after(new Date())) {
            throw new IllegalArgumentException("Fecha de nacimiento no válida.");
        }
        this.fechaNacimiento = fechaNacimiento;
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
        if (dni == null || !dni.matches("\\d{8,10}")) { // Ejemplo: DNI debe ser de 8 a 10 dígitos
            throw new IllegalArgumentException("El DNI debe contener entre 8 y 10 dígitos numéricos.");
        }
        super.setDni(dni);
    }

    @Override
    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6) { // Ejemplo: longitud mínima de 6 caracteres
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
