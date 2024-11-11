import Clases.*;
import Clases.Habitaciones.Habitacion;
import Clases.Usuario.Usuario;
import JSON.gestorJSONHabitaciones;
import JSON.gestorJSONUsuarios;
import Exepcion.HabitacionOcupadaException;
import Exepcion.HabitacionNoEncontradaException;
import Exepcion.UsuarioNoEncontradoException;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        listaHabitaciones<Habitacion> listaHabitaciones = gestorJSONHabitaciones.cargarHabitaciones();

        listaUsuarios<Usuario> listaUsuarios = gestorJSONUsuarios.cargaUsuarios();

        listaReservas listaReservas = new listaReservas(new ArrayList<>());
        listaReservas.cargarReservasDesdeJSON();

        System.out.println("HABITACIONES:");
        System.out.println("\n");
        for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {

            System.out.println(habitacion);
            System.out.println("\n");
        }

        System.out.println("USUARIOS:");
        System.out.println("\n");
        for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
            System.out.println(usuario);
            System.out.println("\n");
        }

        System.out.print("Ingresa el nombre del usuario: ");
        String nombreUsuario = scanner.nextLine();

        System.out.print("Ingresa el ID de la habitación: ");
        String habitacionId = scanner.nextLine();

        try {
            Reserva reserva = new Reserva(listaUsuarios, listaHabitaciones);
            reserva.añadirReserva(nombreUsuario, habitacionId);
            listaReservas.agregarReserva(reserva);

            // Si la reserva se agrega con éxito, se muestra un mensaje
            System.out.println("Reserva realizada con éxito.");
        } catch (UsuarioNoEncontradoException e) {
            // Si el usuario no es encontrado, se muestra un error
            System.out.println(e.getMessage());
        } catch (HabitacionNoEncontradaException e) {
            // Si la habitación no es encontrada, se muestra un error
            System.out.println(e.getMessage());
        } catch (HabitacionOcupadaException e) {
            // Si la habitación está ocupada, se muestra un error
            System.out.println(e.getMessage());
        }
        listaReservas.guardarReservasComoJSON();
    }
}