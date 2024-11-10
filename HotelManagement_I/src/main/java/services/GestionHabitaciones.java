package services;

import enums.EstadoHabitacion;
import exceptions.*;
import manejoJson.GestionJSON;
import models.Habitacion.*;
import models.Usuarios.Usuario;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;
import static enums.EstadoHabitacion.*;

public class GestionHabitaciones {
    //Atributos
    private List<Habitacion> habitaciones;
    //private static GestionHabitaciones instancia;

    //Constructor
    public GestionHabitaciones() {
        this.habitaciones = new ArrayList<>();
    }

    //Getter
    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    //Setter
    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    /**-----------------------  INICIO METODOS DE FILTRADO Y BÚSQUEDA  -----------------------*/
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

    //Metodo para buscar una habitacion ingresando el numero
    private Habitacion buscarHabitacionPorNumero(int numero) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null; // Usuario no encontrado
    }

    /*-----------------------  FIN METODOS DE FILTRADO Y BÚSQUEDA  -----------------------*/

    /**-----------------------  INICIO METODOS ABM  -----------------------*/

    //Metodo para crear una habitacion (Individual)
    public Individual crearHabitacionIndividual(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {

        Individual individual = new Individual(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        habitaciones.add(individual);
        actualizarHabitacionesJson();
        return individual;
    }

    //Metodo para crear una habitacion (Doble)
    public Doble crearHabitacionDoble(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {

        Doble doble = new Doble(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        habitaciones.add(doble);
        actualizarHabitacionesJson();
        return doble;
    }

    //Metodo para crear una habitacion (Apartamento)
    public Apartamento crearHabitacionApartamento(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, int ambientes, boolean cocina) {

        Apartamento apartamento = new Apartamento(tipo, numero, capacidad, camas, disponible, estado, detalleEstado, ambientes, cocina);
        habitaciones.add(apartamento);
        actualizarHabitacionesJson();
        return  apartamento;
    }

    //Metodo para crear una habitacion (Presidencial)
    public Presidencial crearHabitacionPresidencial(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, List<String> adicionales, double dimension) {

        Presidencial presidencial = new Presidencial(tipo, numero, capacidad, camas, disponible, estado, detalleEstado, adicionales, dimension);
        habitaciones.add(presidencial);
        actualizarHabitacionesJson();
        return  presidencial;
    }

    //Metodo para crear una habitacion (Suite)
    public Suite crearHabitacionSuite(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, boolean balcon, boolean comedor) {

        Suite suite = new Suite(tipo, numero, capacidad, camas, disponible, estado, detalleEstado, balcon, comedor);
        habitaciones.add(suite);
        actualizarHabitacionesJson();
        return  suite;
    }

    //Metodo para eliminar una habitacion por su número
    public boolean eliminarHabitacion(int numero) {

        Habitacion habitacionAEliminar = buscarHabitacionPorNumero(numero);

        if (habitacionAEliminar != null) {
            habitaciones.remove(habitacionAEliminar);
            actualizarHabitacionesJson();
            return true;
        }

        return false;
    }

    //Metodo para actualizar el archivo Json cuando se realice algún cambio
    private void actualizarHabitacionesJson() {
        try {
            GestionJSON.guardarHabitacionesJson(habitaciones, "habitaciones.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /*-----------------------  FIN METODOS ABM  -----------------------*/
}
