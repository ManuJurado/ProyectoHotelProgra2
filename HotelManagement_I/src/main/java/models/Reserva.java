package models;

import exceptions.ExcesoPasajerosException;
import exceptions.FechaInvalidaException;
import exceptions.HabitacionInexistenteException;
import exceptions.UsuarioNoEncontradoException;
import models.Habitacion.*;
import models.Usuarios.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Reserva {

    private static int contadorReservas = 1;
    private int id;
    private LocalDate fechaReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estadoReserva;
    private String comentario;
    private int cantidadPersonas;
    private List<Pasajero> pasajeros;
    private List<String> serviciosAdicionales;
    private Usuario usuario;
    private Habitacion habitacion;

    public Reserva(){}

    public Reserva(LocalDate fechaEntrada, LocalDate fechaSalida, String estadoReserva, String comentario, int cantidadPersonas, List<Pasajero> pasajeros, List<String> serviciosAdicionales, Usuario usuario, Habitacion habitacion) {
        setFechaReserva(LocalDate.now());
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estadoReserva = estadoReserva;
        this.comentario = comentario;
        this.cantidadPersonas = cantidadPersonas;
        this.pasajeros = pasajeros;
        this.serviciosAdicionales = serviciosAdicionales;
        this.usuario = usuario;
        this.habitacion = habitacion;
    }

    private String obtenerFechaActual() {
        return java.time.LocalDate.now().toString();  // Devuelve la fecha actual en formato ISO
    }

    public static int getContadorReservas() {
        return contadorReservas;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public static void setContadorReservas(int contadorReservas) {
        Reserva.contadorReservas = contadorReservas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) throws UsuarioNoEncontradoException {
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("El usuario no puede ser nulo.");
        }
        this.usuario = usuario;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setHabitacion(Habitacion habitacion) throws HabitacionInexistenteException {
        if (habitacion == null) {
            throw new HabitacionInexistenteException("La habitación no puede ser nula.");
        }
        this.habitacion = habitacion;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) throws FechaInvalidaException {
        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha de entrada no puede ser anterior a la fecha actual.");
        }
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaSalida(LocalDate fechaSalida) throws FechaInvalidaException {
        if (fechaSalida.isBefore(fechaEntrada)) {
            throw new FechaInvalidaException("La fecha de salida no puede ser anterior a la fecha de entrada.");
        }
        this.fechaSalida = fechaSalida;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) throws IllegalArgumentException {
        if (estadoReserva == null || estadoReserva.isEmpty()) {
            throw new IllegalArgumentException("El estado de la reserva no puede ser vacío.");
        }
        this.estadoReserva = estadoReserva;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        if (comentario != null && comentario.length() > 100) {
            throw new IllegalArgumentException("El comentario no puede exceder los 100 caracteres.");
        }
        this.comentario = comentario;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) throws IllegalArgumentException {
        if (cantidadPersonas > 4) {
            throw new IllegalArgumentException("La cantidad de personas debe ser menor que 4.");
        }
        this.cantidadPersonas = cantidadPersonas;
    }

    public List<String> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setServiciosAdicionales(List<String> serviciosAdicionales) {
        if (serviciosAdicionales == null || serviciosAdicionales.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un servicio adicional.");
        }
        this.serviciosAdicionales = serviciosAdicionales;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<Pasajero> pasajeros) throws ExcesoPasajerosException {
        if (pasajeros.size() > 4) {
            throw new ExcesoPasajerosException("No se pueden agregar más de 4 pasajeros.");
        }
        this.pasajeros = pasajeros;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", fechaReserva=" + fechaReserva +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", estadoReserva='" + estadoReserva + '\'' +
                ", comentario='" + comentario + '\'' +
                ", cantidadPersonas=" + cantidadPersonas +
                ", pasajeros=" + pasajeros +
                ", serviciosAdicionales=" + serviciosAdicionales +
                ", usuario=" + usuario +
                ", habitacion=" + habitacion +
                '}';
    }
}