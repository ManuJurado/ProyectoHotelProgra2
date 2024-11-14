package services;

import enums.EstadoHabitacion;
import interfaces.Gestionable_I;
import models.Reserva;
import models.Habitacion.*;
import models.Usuarios.*;
import exceptions.*;
import manejoJson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import models.Pasajero;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestionReservas implements Gestionable_I<Reserva> {

    // Atributos
    private List<Reserva> listaReservas;
    private static GestionReservas instancia; // Instancia única de la clase

    // Contador estático para la generación de IDs autoincrementales
    private static int contadorIdReserva = 1;

    // Constructor
    private GestionReservas() {
        this.listaReservas = new ArrayList<>();
    }

    // Constructor que carga las reservas desde el JSON al crear la instancia de GestionReservas
    private GestionReservas(String fileName) {
        this.listaReservas = new ArrayList<>();
        mostrarMensajeTemporal("Cargando lista de reservas...");
        this.listaReservas = GestionJSON.mapeoReservasJson(fileName);
        this.contadorIdReserva = obtenerUltimoIdReservas();
    }

    // Metodo para obtener el último ID de las reservas (sin necesidad de pasar el nombre del archivo)
    private int obtenerUltimoIdReservas() {
        int ultimoId = 0; // Valor inicial por si no se encuentra ninguna reserva

        // Buscar el ID más alto en las reservas ya cargadas
        for (Reserva reserva : listaReservas) {
            if (reserva.getId() > ultimoId) {
                ultimoId = reserva.getId();
            }
        }

        // El contador debe ser el siguiente ID disponible (último ID + 1)
        return ultimoId + 1;
    }

    // Metodo para obtener la instancia única de la clase (Singleton)
    public static GestionReservas getInstancia(String fileName) {
        if (instancia == null) {
            instancia = new GestionReservas(fileName);
        }
        return instancia;
    }

    // Getter
    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    // Setter
    public void setListaReservas(List<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    /**-----------------------  INICIO METODOS ABM  -----------------------*/

    public Reserva crearReserva(String nombreUsuario, String habitacionId, LocalDate fechaEntrada, LocalDate fechaSalida,
                                String comentario, int cantidadPersonas, List<String> serviciosAdicionales, List<Pasajero> pasajeros)
            throws UsuarioNoEncontradoException, HabitacionInexistenteException, HabitacionDuplicadaException, FechaInvalidaException {

        // Obtener el nuevo ID de reserva antes de incrementar el contador
        int nuevoIdReserva = contadorIdReserva;

        // Incrementar el contador para el siguiente ID
        contadorIdReserva++;

        // Validar cantidad de pasajeros
        if (cantidadPersonas > 4) {
            throw new ExcesoPasajerosException("No se pueden agregar más de 4 pasajeros a una reserva.");
        }

        // Buscar usuario
        Usuario usuarioEncontrado = null;
        for (Usuario usuario : GestionUsuario.getInstancia("HotelManagement_I/usuarios.json").getUsuarios()) {
            if (usuario.getNombre().equalsIgnoreCase(nombreUsuario)) {
                usuarioEncontrado = usuario;
                break;
            }
        }
        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en el sistema.");
        }

        // Buscar habitación
        Habitacion habitacionEncontrada = null;
        for (Habitacion habitacion : GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").getHabitaciones()) {
            if (String.valueOf(habitacion.getNumero()).equals(habitacionId)) {
                habitacionEncontrada = habitacion;
                break;
            }
        }
        if (habitacionEncontrada == null) {
            throw new HabitacionInexistenteException("La habitación con ID " + habitacionId + " no se encuentra en la lista.");
        }

        // Validar que la cantidad de personas no exceda la capacidad de la habitación
        if (cantidadPersonas > habitacionEncontrada.getCapacidad()) {
            throw new ExcesoPasajerosException("La cantidad de personas excede la capacidad de la habitación seleccionada.");
        }

        // Verificar superposición de fechas
        try{
            for (Reserva reservaExistente : listaReservas) {
                if (reservaExistente.getHabitacion().getNumero() == habitacionEncontrada.getNumero()) {
                    LocalDate fechaEntradaExistente = reservaExistente.getFechaEntrada();
                    LocalDate fechaSalidaExistente = reservaExistente.getFechaSalida();

                    // Verificar si las fechas se superponen
                    if ((fechaEntrada.isBefore(fechaSalidaExistente) && fechaEntrada.isAfter(fechaEntradaExistente)) ||
                            (fechaSalida.isAfter(fechaEntradaExistente) && fechaSalida.isBefore(fechaSalidaExistente)) ||
                            (fechaEntrada.isBefore(fechaEntradaExistente) && fechaSalida.isAfter(fechaSalidaExistente))) {
                    }
                }
            }
        }catch (RuntimeException e){
            throw new FechaInvalidaException("La habitación con ID " + habitacionId + " ya está reservada para las fechas solicitadas.");
        }


        // Crear la reserva con el nuevo ID
        Reserva nuevaReserva = new Reserva(nuevoIdReserva, fechaEntrada, fechaSalida, "Reservada", comentario, cantidadPersonas, pasajeros, serviciosAdicionales, usuarioEncontrado, habitacionEncontrada);

        // Actualizar estado de la habitación
        habitacionEncontrada.setDisponible(false);

        // Agregar la nueva reserva a la lista
        listaReservas.add(nuevaReserva);

        // Guardar la nueva reserva en el archivo JSON
        try {
            actualizarReservasJson();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la reserva en el archivo JSON.");
        }

        // Guardar la habitación actualizada en el archivo JSON
        try {
            GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").actualizarHabitacion(habitacionEncontrada);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar los cambios en las habitaciones en el archivo JSON.");
        }

        return nuevaReserva; // Devolver la reserva creada
    }




    // Metodo para modificar una reserva existente
    public void modificarReserva(Reserva reservaModificada) {
        // Buscar la reserva en la lista usando su ID (o cualquier otro identificador único)
        for (int i = 0; i < this.listaReservas.size(); i++) {
            Reserva reserva = this.listaReservas.get(i);
            if (reserva.getId()==(reservaModificada.getId())) {
                // Reemplazar la reserva vieja con la nueva reserva modificada
                this.listaReservas.set(i, reservaModificada);
                break;
            }
        }

        // Luego guardamos las reservas modificadas (puedes implementarlo como desees)
        actualizarReservasJson();
    }


    // Metodo para eliminar una reserva
    public boolean eliminarReserva(String nombreUsuario, String habitacionId, LocalDate fechaEntrada)
            throws ReservaNoEncontradaException {

        Reserva reservaAEliminar = null;
        for (Reserva reserva : listaReservas) {
            if (reserva.getUsuario().getNombre().equalsIgnoreCase(nombreUsuario) &&
                    reserva.getHabitacion().getNumero() == Integer.parseInt(habitacionId) &&
                    reserva.getFechaEntrada().equals(fechaEntrada)) {
                reservaAEliminar = reserva;
                break;
            }
        }

        if (reservaAEliminar == null) {
            throw new ReservaNoEncontradaException("No se encontró ninguna reserva para el usuario " + nombreUsuario);
        }

        // Eliminar la reserva y liberar la habitación
        listaReservas.remove(reservaAEliminar);
        Habitacion habitacion = reservaAEliminar.getHabitacion();
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE); // Actualizar estado a disponible

        // Guardar cambios en el archivo JSON
        actualizarReservasJson();

        System.out.println("Reserva eliminada con éxito.");
        System.out.println("Usuario: " + reservaAEliminar.getUsuario().getNombre() + " - Habitacion: " + habitacion.getNumero());
        return true;
    }

    // Metodo para actualizar el archivo JSON con las reservas
    public void actualizarReservasJson() {
        try {
            GestionJSON.guardarReservasJson(listaReservas, "HotelManagement_I/reservas.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**-----------------------  FIN METODOS ABM  -----------------------*/

    // Mostrar mensaje temporal de carga (opcional)
    private void mostrarMensajeTemporal(String mensaje) {
        // Implementación similar a la de GestionHabitaciones
    }

    @Override
    public Reserva buscarPorId(String id) {
        for (Reserva reserva : listaReservas) {
            if (String.valueOf(reserva.getId()).equals(id)) {
                return reserva;
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(String id) {
        Reserva reservaAEliminar = buscarPorId(id);
        if (reservaAEliminar != null) {
            listaReservas.remove(reservaAEliminar);
            actualizarReservasJson();
            return true;
        }
        return false;
    }

    @Override
    public void guardar(Reserva objeto) {
        System.out.println(objeto.getFechaReserva());
        listaReservas.add(objeto);
        actualizarReservasJson();
    }
}
