package services;

import com.almasb.fxgl.net.Client;
import models.Habitacion.*;
import models.Pasajero;
import models.Reserva;
import models.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionReservas {
    private List<Usuario> usuarios; // Lista de usuarios (clientes)
    private List<Habitacion> habitaciones; // Lista de habitaciones
    private List<Reserva> reservas; // Lista de reservas

//    private GestionarHabitaciones gestionarHabitaciones; // Lista de reservas
    private GestionarUsuarios gestionarUsuarios; // Lista de usuarios

    // Constructor
    public GestionReservas() {
        this.usuarios = new ArrayList<>();
        this.habitaciones = new ArrayList<>();

//        this.gestionarHabitaciones = new GestionarHabitaciones(); // Instanciar aquí
        this.gestionarUsuarios = new GestionarUsuarios(); // Instanciar aquí

//        this.habitaciones = gestionarHabitaciones.getHabitaciones();
        this.usuarios = gestionarUsuarios.getUsuarios();
        this.reservas = new ArrayList<>();

        // Hardcodear reservas
        List<Pasajero> pasajeros1 = new ArrayList<>();
        pasajeros1.add(new Pasajero("Don", "Quijote", "11223344")); // Ejemplo de pasajero
        pasajeros1.add(new Pasajero("Sancho", "Panza", "235423324")); // Ejemplo de pasajero
        reservas.add(new Reserva(1, pasajeros1, habitaciones.get(0), LocalDate.now(), LocalDate.now().plusDays(2), usuarios.get(0), "Activa"));

        List<Pasajero> pasajeros2 = new ArrayList<>();
        pasajeros2.add(new Pasajero("Emi", "Salias", "34757453745")); // Otro ejemplo de pasajero
        pasajeros2.add(new Pasajero("Mati", "Rodrig", "6456442")); // Otro ejemplo de pasajero
        pasajeros2.add(new Pasajero("Nico", "Martínez", "235626622")); // Otro ejemplo de pasajero
        pasajeros2.add(new Pasajero("Manu", "Sosa", "2145115122")); // Otro ejemplo de pasajero
        reservas.add(new Reserva(2, pasajeros2, habitaciones.get(1), LocalDate.now(), LocalDate.now().plusDays(3), usuarios.get(1), "Activa"));
    }

    // Metodo para crear una reserva
    public void crearReserva(List<Pasajero> pasajeros, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, Usuario cliente) {
        // Lógica de validación (por ejemplo, verificar si la habitación está disponible)
        if (habitacion.isDisponible()) {
            int nuevaId = reservas.size() + 1; // Generar un nuevo ID
            String estado = "Activa"; // Asumimos que la reserva es activa al crearla
            reservas.add(new Reserva(nuevaId, pasajeros, habitacion, fechaInicio, fechaFin, cliente, estado));
            habitacion.setDisponible(false); // Cambiar el estado de la habitación
        } else {
            throw new IllegalArgumentException("La habitación no está disponible.");
        }
    }

    // Método para modificar una reserva
    public void modificarReserva(int id, List<Pasajero> pasajeros, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin) {
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                reserva.setPasajeros(pasajeros);
                reserva.setHabitacion(habitacion);
                reserva.setFechaInicio(fechaInicio);
                reserva.setFechaFin(fechaFin);
                return;
            }
        }
        throw new IllegalArgumentException("Reserva no encontrada.");
    }

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservas); // Devuelve una copia de la lista
    }

    public List<Habitacion> getListaHabitaciones(){
        return new ArrayList<>(habitaciones); // Devuelve una copia de la lista
    }

    public List<Usuario> getListaClientes(){
        return new ArrayList<>(usuarios); // Devuelve una copia de la lista
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }
}
