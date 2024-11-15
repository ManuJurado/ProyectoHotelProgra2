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
import java.util.stream.Collectors;

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
            throws UsuarioNoEncontradoException, HabitacionInexistenteException, HabitacionDuplicadaException,
            FechaInvalidaException, ExcesoPasajerosException {

        // Obtener el nuevo ID de reserva antes de incrementar el contador
        int nuevoIdReserva = contadorIdReserva++;

        // Validar cantidad de pasajeros
        if (cantidadPersonas > 4) {
            throw new ExcesoPasajerosException("No se pueden agregar más de 4 pasajeros a una reserva.");
        }

        // Buscar usuario
        Usuario usuarioEncontrado = buscarUsuario(nombreUsuario);
        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en el sistema.");
        }

        // Buscar habitación
        Habitacion habitacionEncontrada = buscarHabitacion(habitacionId);
        if (habitacionEncontrada == null) {
            throw new HabitacionInexistenteException("La habitación con ID " + habitacionId + " no se encuentra en la lista.");
        }

        // Validar la capacidad de la habitación
        if (cantidadPersonas > habitacionEncontrada.getCapacidad()) {
            throw new ExcesoPasajerosException("La cantidad de personas excede la capacidad de la habitación seleccionada.");
        }

        // Verificar superposición de fechas
        verificarDisponibilidadFecha(habitacionEncontrada, fechaEntrada, fechaSalida);

        // Crear y guardar la reserva
        Reserva nuevaReserva = new Reserva(nuevoIdReserva, fechaEntrada, fechaSalida, "Reservada", comentario,
                cantidadPersonas, pasajeros, serviciosAdicionales, usuarioEncontrado, habitacionEncontrada);

        // Actualizar estado de la habitación y agregar la reserva
        habitacionEncontrada.setDisponible(false);
        nuevaReserva.setNumeroHabitacion(habitacionEncontrada.getNumero());

        listaReservas.add(nuevaReserva);

        // Guardar datos en archivos JSON
        guardarReservaYHabitacion(nuevaReserva, habitacionEncontrada);

        return nuevaReserva;
    }

    // Metodo auxiliar para verificar disponibilidad de fechas
    private void verificarDisponibilidadFecha(Habitacion habitacion, LocalDate fechaEntrada, LocalDate fechaSalida) throws FechaInvalidaException {
        for (Reserva reservaExistente : listaReservas) {
            if (!reservaExistente.getEstadoReserva().equalsIgnoreCase("Cancelada")) {
                if (reservaExistente.getHabitacion().getNumero() == habitacion.getNumero()) {
                    LocalDate fechaEntradaExistente = reservaExistente.getFechaEntrada();
                    LocalDate fechaSalidaExistente = reservaExistente.getFechaSalida();

                    // Verificar si las fechas se superponen
                    if ((fechaEntrada.isBefore(fechaSalidaExistente) && fechaSalida.isAfter(fechaEntradaExistente)) ||
                            fechaEntrada.equals(fechaEntradaExistente) || fechaSalida.equals(fechaSalidaExistente) ||
                            (fechaEntrada.isBefore(fechaEntradaExistente) && fechaSalida.isAfter(fechaSalidaExistente))) {

                        throw new FechaInvalidaException("La habitación con ID " + habitacion.getNumero() + " ya está reservada para las fechas solicitadas.");
                    }
                }
            }
        }
    }

    // Métodos auxiliares para separar la lógica
    private Usuario buscarUsuario(String nombreUsuario) {
        return GestionUsuario.getInstancia("HotelManagement_I/usuarios.json").getUsuarios().stream()
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombreUsuario))
                .findFirst().orElse(null);
    }

    private Habitacion buscarHabitacion(String habitacionId) {
        return GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").getHabitaciones().stream()
                .filter(habitacion -> String.valueOf(habitacion.getNumero()).equals(habitacionId))
                .findFirst().orElse(null);
    }

    private void guardarReservaYHabitacion(Reserva reserva, Habitacion habitacion) {
        try {
            System.out.println("TESTEO 1" + habitacion);
            actualizarReservasJson();
            System.out.println("TESTEO 2" + habitacion);
            GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").actualizarHabitacion(habitacion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar los datos en el archivo JSON.");
        }
    }

    public Reserva modificarReserva(Reserva reservaExistente, LocalDate nuevaFechaEntrada, LocalDate nuevaFechaSalida,
                                    String nuevoComentario, int nuevaCantidadPersonas, List<String> nuevosServiciosAdicionales, List<Pasajero> nuevosPasajeros)
            throws FechaInvalidaException, ExcesoPasajerosException {

        // Validar la cantidad de pasajeros
        if (nuevaCantidadPersonas > 4) {
            throw new ExcesoPasajerosException("No se pueden agregar más de 4 pasajeros a una reserva.");
        }

        // Obtener la habitación de la reserva existente
        Habitacion habitacion = reservaExistente.getHabitacion();

        // Validar la capacidad de la habitación
        if (nuevaCantidadPersonas > habitacion.getCapacidad()) {
            throw new ExcesoPasajerosException("La cantidad de personas excede la capacidad de la habitación seleccionada.");
        }

        // Verificar disponibilidad de fechas, excluyendo la reserva actual
        verificarDisponibilidadFechaParaModificacion(habitacion, nuevaFechaEntrada, nuevaFechaSalida, reservaExistente);

        // Actualizar los datos de la reserva
        reservaExistente.setFechaEntrada(nuevaFechaEntrada);
        reservaExistente.setFechaSalida(nuevaFechaSalida);
        reservaExistente.setComentario(nuevoComentario);
        reservaExistente.setCantidadPersonas(nuevaCantidadPersonas);
        reservaExistente.setServiciosAdicionales(nuevosServiciosAdicionales);
        reservaExistente.setPasajeros(nuevosPasajeros);

        // Guardar los cambios en los archivos JSON
        guardarReservaYHabitacion(reservaExistente, habitacion);

        return reservaExistente;
    }

    // Metodo auxiliar para verificar disponibilidad de fechas en la modificación
    private void verificarDisponibilidadFechaParaModificacion(Habitacion habitacion, LocalDate fechaEntrada, LocalDate fechaSalida, Reserva reservaActual) throws FechaInvalidaException {
        for (Reserva reservaExistente : listaReservas) {
            // Saltar la reserva actual para evitar conflicto con sí misma
            if (reservaExistente.getId() == reservaActual.getId()) {
                continue;
            }

            if (reservaExistente.getHabitacion().getNumero() == habitacion.getNumero()) {
                LocalDate fechaEntradaExistente = reservaExistente.getFechaEntrada();
                LocalDate fechaSalidaExistente = reservaExistente.getFechaSalida();

                // Verificar si las fechas se superponen
                if ((fechaEntrada.isBefore(fechaSalidaExistente) && fechaSalida.isAfter(fechaEntradaExistente)) ||
                        fechaEntrada.equals(fechaEntradaExistente) || fechaSalida.equals(fechaSalidaExistente) ||
                        (fechaEntrada.isBefore(fechaEntradaExistente) && fechaSalida.isAfter(fechaSalidaExistente))) {

                    throw new FechaInvalidaException("La habitación con ID " + habitacion.getNumero() + " ya está reservada para las fechas solicitadas.");
                }
            }
        }
    }


    // Metodo para realizar el check-in con verificación de lista de pasajeros
    public void realizarCheckIn(int idReserva, List<Pasajero> pasajerosCheckIn)
            throws ReservaNoEncontradaException, HabitacionInexistenteException, PasajerosNoCoincidenException, ReservaCanceladaException {

        // Buscar la reserva por ID
        Reserva reserva = buscarReservaPorId(idReserva);

        if (reserva == null) {
            throw new ReservaNoEncontradaException("La reserva con ID " + idReserva + " no se encontró.");
        }

        // Verificar si la reserva está cancelada
        if ("Cancelada".equals(reserva.getEstadoReserva())) {
            throw new ReservaCanceladaException("La reserva con ID " + idReserva + " está cancelada y no se puede realizar el check-in.");
        }

        // Verificar coincidencia de todos los DNI de los pasajeros
        try {
            List<Pasajero> pasajerosReserva = reserva.getPasajeros();
            if (pasajerosCheckIn.size() != pasajerosReserva.size()) {
                throw new PasajerosNoCoincidenException("El número de pasajeros no coincide con la reserva.");
            }

            for (int i = 0; i < pasajerosCheckIn.size(); i++) {
                String dniCheckIn = pasajerosCheckIn.get(i).getDni();
                String dniReserva = pasajerosReserva.get(i).getDni();

                if (!dniCheckIn.equals(dniReserva)) {
                    throw new PasajerosNoCoincidenException("Los DNI de los pasajeros no coinciden con la reserva.");
                }
            }
        } catch (PasajerosNoCoincidenException e) {
            System.out.println("Error en la verificación de pasajeros: " + e.getMessage());
            throw e;
        }

        // Verificar que la fecha de entrada no sea posterior a la fecha actual
        LocalDate fechaIngreso = reserva.getFechaEntrada();
        LocalDate fechaActual = LocalDate.now();

        if (fechaIngreso.isAfter(fechaActual)) {
            throw new IllegalArgumentException("La fecha de entrada aún no ha llegado. No se puede realizar el check-in.");
        }

        // Obtener la habitación de la reserva
        Habitacion habitacion = reserva.getHabitacion();

        // Cambiar el estado de la habitación a OCUPADA
        habitacion.setEstado(EstadoHabitacion.OCUPADA);

        reserva.setEstadoReserva("Finalizada");

        // Guardar el cambio de estado en el JSON de habitaciones
        GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").actualizarHabitacion(habitacion);

        // Guardar el cambio de estado de la reserva en el JSON de reservas
        actualizarReservasJson();
    }

    // Metodo para realizar el check-out
    public void realizarCheckOut(int idReserva) throws ReservaNoEncontradaException, HabitacionInexistenteException, ReservaCanceladaException {
        // Buscar la reserva por ID
        Reserva reserva = buscarReservaPorId(idReserva);

        if (reserva == null) {
            throw new ReservaNoEncontradaException("La reserva con ID " + idReserva + " no se encontró.");
        }

        // Verificar si la reserva está cancelada
        if ("Cancelada".equals(reserva.getEstadoReserva())) {
            throw new ReservaCanceladaException("La reserva con ID " + idReserva + " está cancelada y no se puede realizar el check-out.");
        }

        // Obtener la habitación de la reserva
        Habitacion habitacion = reserva.getHabitacion();

        // Cambiar el estado de la habitación a DISPONIBLE
        habitacion.setEstado(EstadoHabitacion.DISPONIBLE);

        // Cambiar el estado de la reserva a "Finalizada"
        reserva.setEstadoReserva("Finalizada");

        // Guardar los cambios en los archivos JSON
        GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json").actualizarHabitacion(habitacion);
        actualizarReservasJson();
    }


    // Metodo auxiliar para buscar una reserva por ID
    private Reserva buscarReservaPorId(int idReserva) {
        for (Reserva reserva : listaReservas) {
            if (reserva.getId() == idReserva) {
                return reserva;
            }
        }
        return null;
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
                    reserva.getHabitacion() != null && // Verificamos que la habitación no sea null
                    reserva.getHabitacion().getNumero() == Integer.parseInt(habitacionId) &&
                    reserva.getFechaEntrada().equals(fechaEntrada)) {
                reservaAEliminar = reserva;
                break;
            }
        }

        if (reservaAEliminar == null) {
            throw new ReservaNoEncontradaException("No se encontró ninguna reserva para el usuario " + nombreUsuario);
        }

        // Verificar si la habitación fue eliminada
        if (reservaAEliminar.getHabitacionEliminada()) {
            // Si la habitación fue eliminada, simplemente eliminamos la reserva sin modificar el estado de la habitación
            System.out.println("La habitación asociada a la reserva ya fue eliminada. Eliminando solo la reserva.");
        } else {
            // Si la habitación no fue eliminada, liberamos la habitación y actualizamos su estado
            Habitacion habitacion = reservaAEliminar.getHabitacion();
            habitacion.setEstado(EstadoHabitacion.DISPONIBLE); // Actualizar estado a disponible
        }

        // Eliminar la reserva de la lista
        listaReservas.remove(reservaAEliminar);

        // Guardar cambios en el archivo JSON
        actualizarReservasJson();

        System.out.println("Reserva eliminada con éxito.");
        return true;
    }

    public void cancelarReserva(Reserva reserva) throws Exception {
        // Verificar si la reserva está finalizada o ya está cancelada
        if (reserva.getEstadoReserva().equals("Finalizada")) {
            throw new Exception("No se puede cancelar una reserva finalizada.");
        }

        if (reserva.getEstadoReserva().equals("Cancelada")) {
            throw new Exception("La reserva ya está cancelada.");
        }

        // Si pasa las validaciones, cambiar el estado a "Cancelada"
        reserva.setEstadoReserva("Cancelada");

        // Aquí puedes agregar cualquier lógica adicional para guardar los cambios si es necesario.
        actualizarReservasJson();
    }

    public void actualizarReservasJson() {
        try {
            List<Reserva> reservasFiltradas = listaReservas.stream()
                    .filter(reserva -> reserva.getHabitacion() != null)
                    .collect(Collectors.toList());
            GestionJSON.guardarReservasJson(reservasFiltradas, "HotelManagement_I/reservas.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Reserva> obtenerReservasNoCanceladas() {
        List<Reserva> reservasNoCanceladas = new ArrayList<>();

        // Filtrar las reservas no canceladas
        for (Reserva reserva : listaReservas) {
            if (!reserva.getEstadoReserva().equalsIgnoreCase("Cancelada")) {
                reservasNoCanceladas.add(reserva);
            }
        }

        return reservasNoCanceladas;
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
