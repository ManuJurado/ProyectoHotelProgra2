package models;


import models.Habitacion.Habitacion;
import models.Usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private static List<Reserva> reservas = new ArrayList<>();
    private int id;
    private List<Pasajero> pasajeros;
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Usuario cliente;
    private String estado;  // Estado de la reserva

    public Reserva(int id, List<Pasajero> pasajeros, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, Usuario cliente, String estado) {
        this.id = id;
        this.pasajeros = pasajeros;
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cliente = cliente;
        this.estado = estado;  // Asignar estado
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<Pasajero> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaReserva() {
        return "Desde: " + fechaInicio.toString() + " hasta: " + fechaFin.toString();
    }

    public List<Pasajero> getListaPasajeros() {
        return pasajeros;
    }

    // Métodos para obtener datos específicos
    public String getNombreCliente() {
        return cliente != null ? cliente.getNombre() : "Cliente no asignado";
    }

    public LocalDate getFechaInicioReserva() {
        return fechaInicio;
    }

    public LocalDate getFechaFinReserva() {
        return fechaFin;
    }

    // Metodo para crear una nueva reserva
    public static void crearReserva(int id, List<Pasajero> pasajeros, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, Usuario cliente, String estado) {
        Reserva nuevaReserva = new Reserva(id, pasajeros, habitacion, fechaInicio, fechaFin, cliente, estado);
        reservas.add(nuevaReserva);
        System.out.println("Reserva creada para el cliente " + cliente.getNombre() + " desde " + fechaInicio + " hasta " + fechaFin);
    }

    // Metodo para obtener todas las reservas
    public static List<Reserva> obtenerReservas() {
        return reservas;
    }

}
