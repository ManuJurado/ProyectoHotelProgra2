import Clases.*;
import Clases.Habitaciones.Habitacion;
import Clases.Usuario.Cliente;
import Clases.Usuario.Pasajero;
import Clases.Usuario.Usuario;
import Exepcion.ReservaNoEncontradaExceptiio;
import JSON.gestorJSONHabitaciones;
import JSON.gestorJSONUsuarios;
import Exepcion.HabitacionOcupadaException;
import Exepcion.HabitacionNoEncontradaException;
import Exepcion.UsuarioNoEncontradoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        listaHabitaciones<Habitacion> listaHabitaciones = gestorJSONHabitaciones.cargarHabitaciones();
        listaUsuarios<Usuario> listaUsuarios = gestorJSONUsuarios.cargaUsuarios();
        List<Reserva> reservas = new ArrayList<>();
        listaReservas listaReservas = new listaReservas(reservas, listaUsuarios, listaHabitaciones);


        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ DE OPCIONES ---");
            System.out.println("1. Agregar una reserva - Permite agregar una nueva reserva con los datos del usuario, la habitación y las fechas.");
            System.out.println("2. Ver habitaciones disponibles - Muestra las habitaciones disponibles para reservar.");
            System.out.println("3. Ver usuarios - Muestra la lista de usuarios registrados en el sistema.");
            System.out.println("4. Eliminar una reserva");
            System.out.println("5. Salir - Finaliza el programa.");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingresa el nombre del usuario: ");
                    String nombreUsuario = scanner.nextLine();

                    System.out.print("Ingresa el ID de la habitación: ");
                    String habitacionId = scanner.nextLine();

                    System.out.print("Ingresa la fecha de entrada (yyyy-MM-dd): ");
                    String fechaEntradaStr = scanner.nextLine();
                    LocalDate fechaEntrada = LocalDate.parse(fechaEntradaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                    System.out.print("Ingresa la fecha de salida (yyyy-MM-dd): ");
                    String fechaSalidaStr = scanner.nextLine();
                    LocalDate fechaSalida = LocalDate.parse(fechaSalidaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.print("Ingresa el estado de la reserva: ");
                    String estadoReserva = scanner.nextLine();

                    System.out.print("Ingresa un comentario (opcional): ");
                    String comentario = scanner.nextLine();

                    System.out.print("Ingresa la cantidad de personas: ");
                    int cantidadPersonas = scanner.nextInt();
                    scanner.nextLine();

                    List<String> serviciosAdicionales = new ArrayList<>();
                    System.out.print("¿Desea agregar servicios adicionales? (s/n): ");
                    String agregarServicios = scanner.nextLine();
                    while (agregarServicios.equalsIgnoreCase("s")) {
                        System.out.print("Ingresa el servicio adicional: ");
                        serviciosAdicionales.add(scanner.nextLine());
                        System.out.print("¿Desea agregar otro servicio adicional? (s/n): ");
                        agregarServicios = scanner.nextLine();
                    }

                    List<Pasajero> pasajeros = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    System.out.print("¿Desea agregar pasajeros a la reserva? (s/n): ");
                    String agregarPasajero = scanner.nextLine();
                    int idPasajeroCounter = 1;

                    while (agregarPasajero.equalsIgnoreCase("s")) {
                        System.out.print("Ingresa el nombre del pasajero: ");
                        String nombrePasajero = scanner.nextLine();

                        System.out.print("Ingresa el apellido del pasajero: ");
                        String apellidoPasajero = scanner.nextLine();

                        System.out.print("Ingresa el número de documento del pasajero: ");
                        String numeroDocumento = scanner.nextLine();

                        System.out.print("Ingresa la fecha de nacimiento del pasajero (yyyy-MM-dd): ");
                        LocalDate fechaNacimiento = LocalDate.parse(scanner.nextLine(), formatter);

                        System.out.print("Ingresa la nacionalidad del pasajero: ");
                        String nacionalidad = scanner.nextLine();

                        System.out.print("Ingresa el teléfono del pasajero: ");
                        String telefono = scanner.nextLine();

                        System.out.print("Ingresa el email del pasajero: ");
                        String email = scanner.nextLine();


                        Pasajero pasajero = new Pasajero(idPasajeroCounter++, nombrePasajero, apellidoPasajero, numeroDocumento, fechaNacimiento, nacionalidad, telefono, email);
                        pasajeros.add(pasajero);

                        System.out.print("¿Desea agregar otro pasajero? (s/n): ");
                        agregarPasajero = scanner.nextLine();
                    }

                    try {
                        listaReservas.añadirReserva(nombreUsuario, habitacionId, fechaEntrada, fechaSalida,  comentario, cantidadPersonas, serviciosAdicionales, pasajeros);
                        System.out.println("Reserva realizada con éxito.");
                    } catch (UsuarioNoEncontradoException | HabitacionNoEncontradaException | HabitacionOcupadaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:

                    System.out.println("\nHABITACIONES DISPONIBLES:");
                    for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {
                        if (habitacion.isDisponible()) {
                            System.out.println(habitacion);
                        }
                    }
                    break;

                case 3:

                    System.out.println("\nUSUARIOS:");
                    for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
                        if (usuario instanceof Cliente) {
                            System.out.println(usuario);
                        }
                    }
                    break;

                case 4:

                    System.out.print("Ingresa el nombre del usuario: ");
                    String nombreUsuarioEliminar = scanner.nextLine();

                    System.out.print("Ingresa la contraseña del usuario: ");
                    String contraseniaEliminar = scanner.nextLine();

                    System.out.print("Ingresa el DNI del usuario: ");
                    String dniEliminar = scanner.nextLine();

                    try {
                        listaReservas.eliminarReserva(nombreUsuarioEliminar, contraseniaEliminar, dniEliminar);
                    } catch (UsuarioNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    } catch (ReservaNoEncontradaExceptiio e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("¿Seguro que deseas salir? (s/n): ");
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {

                        listaReservas.guardarReservasComoJSON();
                        System.out.println("Todos los cambios han sido guardados en reservas.json.");
                        salir = true;
                    }
                    break;
                default:

                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }
}
