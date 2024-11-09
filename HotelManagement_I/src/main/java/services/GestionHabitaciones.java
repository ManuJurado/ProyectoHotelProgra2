package services;

import exceptions.*;
import models.Habitacion.*;
import java.util.*;
import static enums.EstadoHabitacion.*;

public class GestionHabitaciones <H extends Habitacion> {
    //Atributos
    private List<H> habitaciones;

    //Constructor
    public GestionHabitaciones() {
        this.habitaciones = new ArrayList<>();
    }

    //Getter
    public List<H> getHabitaciones() {
        return habitaciones;
    }

    //Setter
    public void setHabitaciones(List<H> habitaciones) {
        this.habitaciones = habitaciones;
    }

    /**-----------------------  INICIO METODOS DE FILTRADO  -----------------------*/
    //Metodo que devuelve todas las habitaciones que se encuentran disponibles para alquilar
    public List<Habitacion> listadoHabitacionesDisponibles() {

        List<Habitacion> habitacionesDisponibles = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (h.isDisponible()) {
                habitacionesDisponibles.add(h);
            }
        }

        if (habitacionesDisponibles.isEmpty()){
            throw new HabitacionInexistenteException("No se han encontrado habitaciones disponibles para alquilar.");
        }

        return habitacionesDisponibles;
    }

    //Metodo que devuelve todas las habitaciones que están alquiladas
    public List<Habitacion> listadoHabitacionesAlquiladas() {

        List<Habitacion> habitacionesAlquiladas = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (h.getEstado().equals(ALQUILADA)) {
                habitacionesAlquiladas.add(h);
            }
        }

        if (habitacionesAlquiladas.isEmpty()){
            throw new HabitacionInexistenteException("No se han encontrado habitaciones alquiladas.");
        }

        return habitacionesAlquiladas;
    }

    //Metodo que devuelve todas las habitaciones en las que se está realizando algún trabajo
    public List<Habitacion> listadoHabitacionesEnAlgunProceso() {

        List<Habitacion> habitacionesEnAlgunProceso = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (!h.getEstado().equals(DISPONIBLE) || !h.getEstado().equals(ALQUILADA)) {
                habitacionesEnAlgunProceso.add(h);
            }
        }

        if (habitacionesEnAlgunProceso.isEmpty()){
            throw new HabitacionInexistenteException("No se han encontrado habitaciones donde se este realizando algun trabajo.");
        }

        return habitacionesEnAlgunProceso;
    }

    //Metodo que devuelve todas las habitaciones filtrando por cantidad de pasajeros ingresada
    public List<Habitacion> listadoHabitacionesPorCapacidad(int cantidadPasajeros) {

        List<Habitacion> habitacionesPorCapacidad = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (h.getCapacidad() >= cantidadPasajeros) {
                habitacionesPorCapacidad.add(h);
            }
        }

        return habitacionesPorCapacidad;
    }

    //Metodo que devuelve todas las habitaciones filtrando por tipo de habitacion
    public List<Habitacion> listadoHabitacionesPorTipo(String tipoHabitacion) {

        List<Habitacion> habitacionesPorTipo = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (h.getTipo().equalsIgnoreCase(tipoHabitacion)) {
                habitacionesPorTipo.add(h);
            }
        }

        return habitacionesPorTipo;
    }

    /**-----------------------  FIN METODOS DE FILTRADO  -----------------------*/

    /**-----------------------  INICIO METODOS CRUD  -----------------------*/

    //agregar
    //modificar
    //eliminar
    //guardar (carga json)

    /*public static JSONObject convertirUsuarioAJson(Usuario usuario) throws JSONException {
        JSONObject jsonUsuario = new JSONObject();
        jsonUsuario.put("nombre", usuario.getNombre());
        jsonUsuario.put("apellido", usuario.getApellido());
        jsonUsuario.put("dni", usuario.getDni());
        jsonUsuario.put("correoElectronico", usuario.getCorreoElectronico());


        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            jsonUsuario.put("direccion", cliente.getDireccion());
            jsonUsuario.put("telefono", cliente.getTelefono());
            jsonUsuario.put("puntosFidelidad", cliente.getPuntosFidelidad());
            // Verificar si la fecha de nacimiento es nula antes de convertirla a String
            if (cliente.getFechaNacimiento() != null) {
                jsonUsuario.put("fechaNacimiento", cliente.getFechaNacimiento().toString());
            } else {
                jsonUsuario.put("fechaNacimiento", "Fecha no disponible");
            }
        } else if (usuario instanceof Conserje) {
            Conserje conserje = (Conserje) usuario;
            jsonUsuario.put("turno", conserje.getTurno());
            jsonUsuario.put("numeroEmpleado", conserje.getNumeroEmpleado());
            jsonUsuario.put("areaResponsable", conserje.getAreaResponsable());
            jsonUsuario.put("estadoTrabajo", conserje.getEstadoTrabajo());
            // Verificar si la fecha de ingreso es nula antes de convertirla a String
            if (conserje.getFechaIngreso() != null) {
                jsonUsuario.put("fechaIngreso", conserje.getFechaIngreso().toString());
            } else {
                jsonUsuario.put("fechaIngreso", "Fecha no disponible");
            }
        } else if (usuario instanceof Administrador) {
            // Agregar campos específicos del administrador si es necesario
        }

        return jsonUsuario;
    }*/

    /**-----------------------  FIN METODOS CRUD  -----------------------*/
}
