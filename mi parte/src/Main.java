import Clases.*;
import Clases.Habitaciones.Habitacion;
import Clases.Usuario.Usuario;
import JSON.gestorJSONHabitaciones;
import JSON.gestorJSONUsuarios;
import Exepcion.HabitacionOcupadaException;
import Exepcion.HabitacionNoEncontradaException;
import Exepcion.UsuarioNoEncontradoException;

public class Main {

    public static void main(String[] args) {

        listaHabitaciones<Habitacion> listaHabitaciones = gestorJSONHabitaciones.cargarHabitaciones();

        System.out.println("HABITACIONES:");
        System.out.println("\n");
        for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {

            System.out.println(habitacion);
            System.out.println("\n");
        }

        listaUsuarios<Usuario> listaUsuarios = gestorJSONUsuarios.cargaUsuarios();

        System.out.println("USUARIOS:");
        System.out.println("\n");
        for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
            System.out.println(usuario);
            System.out.println("\n");
        }

        Reserva reserva = new Reserva(listaUsuarios,listaHabitaciones);

        try {
            reserva.añadirReserva("Juan", "102");
        } catch (UsuarioNoEncontradoException | HabitacionNoEncontradaException | HabitacionOcupadaException e) {
            System.out.println(e.getMessage());
        }













        /*
        // Carga el JSON con los pasajero de ejemplo

        List<Reserva> reservas = ReservasCargadas.cargarReservas();
        crearReservas CrearReservas = new crearReservas(reservas);
        crearReservas.guardarReservas(reservas,     "reservas.json");

        System.out.println("Reservas cargadas:");
        for (Reserva reserva : reservas) {
            System.out.println(reserva);
        }

        // Agrega una reserva de manera manual

        Pasajero nuevoPasajero = new Pasajero("Laura", "Martínez", "98765432", "Calle Nueva 456");
        Habitacion nuevaHabitacion = new Habitacion(404, "Doble", true, "Limpia");
        LocalDate fechaInicio = LocalDate.of(2024, 11, 5);
        LocalDate fechaFin = LocalDate.of(2024, 11, 10);

        Reserva nuevaReserva = new Reserva(nuevoPasajero, nuevaHabitacion, fechaInicio, fechaFin);
        reservas.add(nuevaReserva);

        // Agregala nueva reserva al archivo JSON
        crearReservas.guardarReservas(reservas, "reservas.json");

        // Muestra una nueva reserva
        System.out.println("Nueva reserva agregada:");
        System.out.println(nuevaReserva);

        // Muestra el historial de reservas de un pasajero
        Historial historial = new Historial(nuevoPasajero);
        historial.agregarReserva(nuevaReserva);
        System.out.println(historial);

        // Inicializa la gestión con los datos cargados
        Gestionamiento gestion = new Gestionamiento(reservas);

        // Muestra las habitaciones disponibles
        System.out.println("Habitaciones disponibles:");
        for (Habitacion habitacion : gestion.habitacionesDisponibles()) {
            System.out.println(habitacion);
        }

        // ejemplo cargado por mi, desp intentar de manera manual

        Pasajero pasajero = new Pasajero("Juan", "Perez", "DNI12345678","Calle Nueva 456");
        Habitacion habitacion = new Habitacion(101, "Doble",false, "disponible");
        LocalDate fechaInicio2 = LocalDate.of(2024, 11, 1);
        LocalDate fechaFin2 = LocalDate.of(2024, 11, 7);

        boolean reservaExitosa = gestion.reservarHabitacion(pasajero, habitacion, fechaInicio, fechaFin); // en este caso para probar que esta ocupada ya

        if (reservaExitosa) {
            System.out.println("Reserva realizada con éxito para el pasajero " + pasajero.getNombre());
        } else {
            System.out.println("La habitación ya está ocupada.");
        }

        gestion.checkOut(habitacion);  // recordatorio es gestion no Gestion esa es la clase la instacia creada es "gestion"

        System.out.println("Check-out realizado. La habitación " + habitacion.getNumero() + " está nuevamente disponible.");

        */

    }
}