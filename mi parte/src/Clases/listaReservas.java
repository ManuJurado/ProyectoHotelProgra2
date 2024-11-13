package Clases;

import Clases.Habitaciones.Habitacion;
import Clases.Usuario.Usuario;
import Exepcion.*;
import JSON.JSONUtiles;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import Clases.Usuario.Pasajero;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class listaReservas {

    List<Reserva> listaReservas;
    private listaUsuarios<Usuario> listaUsuarios;
    private listaHabitaciones<Habitacion> listaHabitaciones;

    public listaReservas(List<Reserva> listaReservas, listaUsuarios<Usuario> listaUsuarios, listaHabitaciones<Habitacion> listaHabitaciones) {
        this.listaReservas = listaReservas;
        this.listaUsuarios = listaUsuarios;
        this.listaHabitaciones = listaHabitaciones;
    }


    public void añadirReserva(String nombreUsuario, String habitacionId, LocalDate fechaEntrada, LocalDate fechaSalida, String comentario, int cantidadPersonas, List<String> serviciosAdicionales, List<Pasajero> pasajeros)
            throws UsuarioNoEncontradoException, HabitacionNoEncontradaException, HabitacionOcupadaException {

        if (pasajeros.size() > 4) {
            throw new ExcesoPasajerosException("No se pueden agregar más de 4 pasajeros a una reserva.");
        }

        for (Pasajero pasajero : pasajeros) {
            if (!pasajero.getEmail().contains("@")) {
                throw new CorreoInvalidoException("El correo electrónico del pasajero " + pasajero.getNombre() + " no es válido.");
            }
        }

        Usuario usuarioEncontrado = null;
        Habitacion habitacionEncontrada = null;

        for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
            if (usuario.getNombre().equalsIgnoreCase(nombreUsuario)) {
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en la lista.");
        }

        for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {
            if (String.valueOf(habitacion.getNumero()).equals(habitacionId)) {
                habitacionEncontrada = habitacion;
                break;
            }
        }

        if (habitacionEncontrada == null) {
            throw new HabitacionNoEncontradaException("La habitación con ID " + habitacionId + " no se encuentra en la lista.");
        }

        if (!habitacionEncontrada.isDisponible()) {
            throw new HabitacionOcupadaException("La habitación con ID " + habitacionId + " ya está ocupada.");
        }

        for (Reserva reservaExistente : listaReservas) {
            if (reservaExistente.getHabitacion().getNumero() == habitacionEncontrada.getNumero()) {
                LocalDate fechaEntradaExistente = reservaExistente.getFechaEntrada();
                LocalDate fechaSalidaExistente = reservaExistente.getFechaSalida();

                // Verificar si las fechas se sobre ponen
                if ((fechaEntrada.isBefore(fechaSalidaExistente) && fechaEntrada.isAfter(fechaEntradaExistente)) ||
                        (fechaSalida.isAfter(fechaEntradaExistente) && fechaSalida.isBefore(fechaSalidaExistente)) ||
                        (fechaEntrada.isBefore(fechaEntradaExistente) && fechaSalida.isAfter(fechaSalidaExistente))) {
                    throw new FechaInvalidaException("La habitación con ID " + habitacionId + " ya está reservada para las fechas solicitadas.");
                }
            }
        }

        Reserva nuevaReserva = new Reserva(LocalDate.now(), fechaEntrada, fechaSalida, "reservada", comentario, cantidadPersonas, pasajeros, serviciosAdicionales, usuarioEncontrado, habitacionEncontrada);
        habitacionEncontrada.setEstado("ocupado");
        listaReservas.add(nuevaReserva);

        System.out.println("Reserva agregada con éxito:");
        System.out.println("Usuario: " + usuarioEncontrado.getNombre() + " - Habitacion: " + habitacionEncontrada.getNumero());
        System.out.println("Fecha de reserva: " + nuevaReserva.getFechaReserva());
    }

    public void eliminarReserva(String nombreUsuario, String contrasenia, String dni)
            throws UsuarioNoEncontradoException, ReservaNoEncontradaExceptiio {

        Usuario usuarioEncontrado = null;
        Reserva reservaAEliminar = null;

        for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
            if (usuario.getNombre().equalsIgnoreCase(nombreUsuario) &&
                    usuario.getContrasenia().equals(contrasenia) &&
                    usuario.getDni().equals(dni)) {
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en el sistema o los datos son incorrectos.");
        }

        for (Reserva reserva : listaReservas) {
            if (reserva.getUsuario().equals(usuarioEncontrado)) {
                reservaAEliminar = reserva;
                break;
            }
        }

        if (reservaAEliminar == null) {
            throw new ReservaNoEncontradaExceptiio("No se encontró ninguna reserva para el usuario " + nombreUsuario);
        }

        // Eliminar la reserva y actualizar el estado de la habitación
        listaReservas.remove(reservaAEliminar);
        Habitacion habitacion = reservaAEliminar.getHabitacion();
        habitacion.setEstado("disponible"); // Actualiza el estado de la habitación a disponible

        System.out.println("Reserva eliminada con éxito.");
        System.out.println("Usuario: " + usuarioEncontrado.getNombre() + " - Habitacion: " + habitacion.getNumero());
        System.out.println("La habitación ha sido liberada.");
        guardarReservasComoJSON();
    }

    public void guardarReservasComoJSON() {
        try {
            JSONArray jsonArrayReservas = new JSONArray();
            for (Reserva reserva : listaReservas) {
                JSONObject jsonReserva = new JSONObject();
                jsonReserva.put("id", reserva.getId());
                jsonReserva.put("usuario", reserva.getUsuario().getNombre());
                jsonReserva.put("habitacion", reserva.getHabitacion().getNumero());
                jsonReserva.put("fechaEntrada", reserva.getFechaEntrada().toString());
                jsonReserva.put("fechaSalida", reserva.getFechaSalida().toString());
                jsonReserva.put("fechaReserva", reserva.getFechaReserva().toString());
                jsonReserva.put("estadoReserva", reserva.getEstadoReserva());
                jsonReserva.put("comentario", reserva.getComentario());
                jsonReserva.put("cantidadPersonas", reserva.getCantidadPersonas());
                jsonReserva.put("serviciosAdicionales", new JSONArray(reserva.getServiciosAdicionales()));

                JSONArray jsonArrayPasajeros = new JSONArray();
                for (Pasajero pasajero : reserva.getPasajeros()) {
                    JSONObject jsonPasajero = new JSONObject();
                    jsonPasajero.put("idPasajero", pasajero.getIdPasajero());
                    jsonPasajero.put("nombre", pasajero.getNombre());
                    jsonPasajero.put("apellido", pasajero.getApellido());
                    jsonPasajero.put("numeroDocumento", pasajero.getNumeroDocumento());
                    jsonPasajero.put("fechaNacimiento", pasajero.getFechaNacimiento().toString());
                    jsonPasajero.put("nacionalidad", pasajero.getNacionalidad());
                    jsonPasajero.put("telefono", pasajero.getTelefono());
                    jsonPasajero.put("email", pasajero.getEmail());

                    jsonArrayPasajeros.put(jsonPasajero);
                }
                jsonReserva.put("pasajeros", jsonArrayPasajeros);

                jsonArrayReservas.put(jsonReserva);
            }

            JSONUtiles.grabar(jsonArrayReservas, "reservas.json");

        } catch (JSONException e) {
            System.err.println("Error al crear el JSON: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "listaReservas{" +
                "listaReservas=" + listaReservas +
                ", listaUsuarios=" + listaUsuarios +
                ", listaHabitaciones=" + listaHabitaciones +
                '}';
    }
}