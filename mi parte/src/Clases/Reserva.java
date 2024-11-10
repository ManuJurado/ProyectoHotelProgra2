package Clases;

import Clases.Habitaciones.Habitacion;
import Clases.Usuario.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.List;

import Exepcion.HabitacionOcupadaException;
import Exepcion.HabitacionNoEncontradaException;
import Exepcion.UsuarioNoEncontradoException;
import JSON.JSONUtiles;


public class Reserva {

    private listaUsuarios<Usuario> listaUsuarios;
    private listaHabitaciones<Habitacion> listaHabitaciones;
    private String fechaReserva;
    private String estadoReserva;
    private boolean estado;
    private List<Usuario> pasajeros;


    public Reserva(listaUsuarios<Usuario> listaUsuarios, listaHabitaciones<Habitacion> listaHabitaciones) {
        this.listaUsuarios = listaUsuarios;
        this.listaHabitaciones = listaHabitaciones;
    }

    public Reserva(Usuario usuarioEncontrado, Habitacion habitacionEncontrada) {
    }

    public void añadirReserva(String nombreUsuario, String habitacionId) throws UsuarioNoEncontradoException, HabitacionNoEncontradaException, HabitacionOcupadaException {
        Usuario usuarioEncontrado = null;
        Habitacion habitacionEncontrada = null;

        // Buscar el usuario en la lista por nombre
        for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
            if (usuario.getNombre().equalsIgnoreCase(nombreUsuario)) {
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en la lista.");
        }

        // Buscar la habitación en la lista
        for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {
            if (String.valueOf(habitacion.getNumero()).equals(habitacionId)) { // Convertir int a String para comparar
                habitacionEncontrada = habitacion;
                break;
            }
        }

        // Si no se encuentra la habitación
        if (habitacionEncontrada == null) {
            throw new HabitacionNoEncontradaException("La habitación con ID " + habitacionId + " no se encuentra en la lista.");
        }

        // Verificar si la habitación está disponible
        if (!habitacionEncontrada.isDisponible()) {
            throw new HabitacionOcupadaException("La habitación con ID " + habitacionId + " ya está ocupada.");
        }

        habitacionEncontrada.setEstado("ocupado");

        Reserva nuevaReserva = new Reserva(usuarioEncontrado, habitacionEncontrada);
        System.out.println("Reserva agregada con éxito:");
        System.out.println("Usuario: " + usuarioEncontrado.getNombre() + " - Habitacion: " + habitacionEncontrada.getNumero());
        System.out.println("Fecha de reserva: " + this.fechaReserva);

        grabarReservaJSON(usuarioEncontrado, habitacionEncontrada, this.fechaReserva, this.estadoReserva);
    }

    public void grabarReservaJSON(Usuario usuario, Habitacion habitacion, String fechaReserva, String estadoReserva) {
        // Crear un JSONObject para la reserva
        JSONObject reservaJSON = new JSONObject();
        try {
            reservaJSON.put("usuario", usuario.getNombre());
            reservaJSON.put("habitacionId", habitacion.getNumero());
            reservaJSON.put("fechaReserva", fechaReserva);
            reservaJSON.put("estadoReserva", estadoReserva);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // Crear un JSONArray y agregar la nueva reserva
        JSONArray reservasArray = new JSONArray();
        reservasArray.put(reservaJSON);

        // Grabar el JSON en el archivo
        JSONUtiles.grabar(reservasArray, "reservas.json");
    }

    public listaUsuarios<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(listaUsuarios<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public listaHabitaciones<Habitacion> getListaHabitaciones() {
        return listaHabitaciones;
    }

    public void setListaHabitaciones(listaHabitaciones<Habitacion> listaHabitaciones) {
        this.listaHabitaciones = listaHabitaciones;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fechaReserva='" + fechaReserva + '\'' +
                ", estadoReserva='" + estadoReserva + '\'' +
                '}';
    }
}

