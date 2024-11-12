package services;

import enums.EstadoHabitacion;
import exceptions.*;
import interfaces.Gestionable_I;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import manejoJson.GestionJSON;
import models.Habitacion.*;
import org.json.JSONException;
import java.io.IOException;
import java.util.*;
import static enums.EstadoHabitacion.*;

public class GestionHabitaciones implements Gestionable_I<Habitacion> {
    //Atributos
    private List<Habitacion> habitaciones;
    private static GestionHabitaciones instancia; //Instancia única de la clase

    //Constructor
    public GestionHabitaciones() {
        this.habitaciones = new ArrayList<>();
    }

    // Constructor que carga las habitaciones desde el JSON para aplicar la logica ni bien se crea la instancia de GestionHabitacion
    private GestionHabitaciones(String fileName){
        this.habitaciones = new ArrayList<>();
        mostrarMensajeTemporal("Cargando lista de habitaciones...");
        this.habitaciones = GestionJSON.mapeoHabitaciones(fileName);
    }

    // Metodo para obtener la instancia única de la clase (Singleton)
    public static GestionHabitaciones getInstancia(String fileName) {
        if (instancia == null) {
            instancia = new GestionHabitaciones(fileName);
        }
        return instancia;
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

    //Metodo para buscar una habitación por su identificador (implementación con String)
    @Override
    public Habitacion buscarPorId(String id) {
        try {
            int numero = Integer.parseInt(id);  // Convertimos el id String a int
            return buscarPorId(String.valueOf(numero));  // Usamos el metodo que ya tenías para buscar por número
        } catch (NumberFormatException e) {
            // Si no se puede convertir el id a int, manejamos el error
            System.out.println("El ID no es válido: " + id);
        }
        return null;
    }
    /*-----------------------  FIN METODOS DE FILTRADO Y BÚSQUEDA  -----------------------*/

    /**-----------------------  INICIO METODOS ABM  -----------------------*/

    //Metodo para crear una habitacion (Individual)
    public Individual crearHabitacionIndividual(int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {

        Individual individual = new Individual(numero, camas, disponible, estado, detalleEstado);
        habitaciones.add(individual);
        actualizarHabitacionesJson();
        return individual;
    }

    //Metodo para crear una habitacion (Doble)
    public Doble crearHabitacionDoble(int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {

        Doble doble = new Doble(numero, camas, disponible, estado, detalleEstado);
        habitaciones.add(doble);
        actualizarHabitacionesJson();
        return doble;
    }

    //Metodo para crear una habitacion (Apartamento)
    public Apartamento crearHabitacionApartamento(int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, int ambientes, boolean cocina) {

        Apartamento apartamento = new Apartamento(numero, capacidad, camas, disponible, estado, detalleEstado, ambientes, cocina);
        habitaciones.add(apartamento);
        actualizarHabitacionesJson();
        return  apartamento;
    }

    //Metodo para crear una habitacion (Presidencial)
    public Presidencial crearHabitacionPresidencial(int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, List<String> adicionales, double dimension) {

        Presidencial presidencial = new Presidencial(numero, capacidad, camas, disponible, estado, detalleEstado, adicionales, dimension);
        habitaciones.add(presidencial);
        actualizarHabitacionesJson();
        return  presidencial;
    }

    //Metodo para crear una habitacion (Suite)
    public Suite crearHabitacionSuite(int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, boolean balcon, boolean comedor) {

        Suite suite = new Suite(numero, capacidad, camas, disponible, estado, detalleEstado, balcon, comedor);
        habitaciones.add(suite);
        actualizarHabitacionesJson();
        return  suite;
    }

    // Metodo para eliminar una habitación por su número (como int)
    @Override
    public boolean eliminar(String id) {
        try {
            int numero = Integer.parseInt(id);  // Convertimos el id String a int
            Habitacion habitacionAEliminar = buscarPorId(String.valueOf(numero));
            if (habitacionAEliminar != null) {
                habitaciones.remove(habitacionAEliminar);
                actualizarHabitacionesJson();
                return true;
            }
        } catch (NumberFormatException e) {
            // Si no se puede convertir el id a int, lanzamos una excepción o manejamos el error
            System.out.println("El ID no es válido: " + id);
        }
        return false;
    }

    // Metodo para guardar una habitación
    @Override
    public void guardar(Habitacion objeto) {
        habitaciones.add(objeto);  // Agrega la habitación a la lista
        actualizarHabitacionesJson();  // Guarda los cambios en el archivo JSON
    }

    // Metodo para actualizar el archivo JSON cuando se realice algún cambio
    private void actualizarHabitacionesJson() {
        try {
            GestionJSON.guardarHabitacionesJson(habitaciones, "HotelManagement_I/habitaciones.json");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para mostrar el mensaje temporal
    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Mostrar el alert
        alert.show();

        // Usamos un Timeline para cerrar el alert después de 2 segundos (2000 milisegundos)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /*-----------------------  FIN METODOS ABM  -----------------------*/
}
