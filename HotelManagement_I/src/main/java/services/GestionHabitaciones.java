package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import enums.EstadoHabitacion;
import exceptions.*;
import interfaces.Gestionable_I;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import manejoJson.GestionJSON;
import models.Habitacion.*;
import models.Usuarios.Usuario;
import org.json.JSONException;

import java.io.File;
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
            return buscarPorIdInt(numero);  // Usamos el metodo que ya tenías para buscar por número
        } catch (NumberFormatException e) {
            // Si no se puede convertir el id a int, manejamos el error
            System.out.println("El ID no es válido: " + id);
        }
        return null;
    }

    //Metodo para buscar por id INT
    public Habitacion buscarPorIdInt(int numero) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null;
    }
    /*-----------------------  FIN METODOS DE FILTRADO Y BÚSQUEDA  -----------------------*/

    /**-----------------------  INICIO METODOS ABM  -----------------------*/

    //Metodo para crear una habitacion (Individual)
    public Individual crearHabitacionIndividual(List<String> camas, boolean disponible, String detalleEstado) {
        int numeroHabitacion = establecerNroHabitacion();
        Individual individual = new Individual(numeroHabitacion, camas, disponible, detalleEstado);
        guardar(individual);
        return individual;
    }

    //Metodo para crear una habitacion (Doble)
    public Doble crearHabitacionDoble(List<String> camas, boolean disponible, String detalleEstado) {
        int numeroHabitacion = establecerNroHabitacion();
        Doble doble = new Doble(numeroHabitacion, camas, disponible, detalleEstado);
        guardar(doble);
        return doble;
    }

    //Metodo para crear una habitacion (Apartamento)
    public Apartamento crearHabitacionApartamento(int capacidad, List<String> camas, boolean disponible, String detalleEstado, int ambientes, boolean cocina) {
        // Obtener el siguiente número de habitación disponible
        int numeroHabitacion = establecerNroHabitacion();
        // Crear la instancia de Apartamento con el número generado automáticamente
        Apartamento apartamento = new Apartamento(numeroHabitacion, capacidad, camas, disponible, detalleEstado, ambientes, cocina);
        guardar(apartamento);
        return  apartamento;
    }

    //Metodo para crear una habitacion (Presidencial)
    public Presidencial crearHabitacionPresidencial(int capacidad, List<String> camas, boolean disponible, String detalleEstado, List<String> adicionales, double dimension) {
        int numeroHabitacion = establecerNroHabitacion();
        Presidencial presidencial = new Presidencial(numeroHabitacion, capacidad, camas, disponible, detalleEstado, adicionales, dimension);
        guardar(presidencial);
        return  presidencial;
    }

    //Metodo para crear una habitacion (Suite)
    public Suite crearHabitacionSuite(int capacidad, List<String> camas, boolean disponible, String detalleEstado, boolean balcon, boolean comedor) {
        int numeroHabitacion = establecerNroHabitacion();
        Suite suite = new Suite(numeroHabitacion, capacidad, camas, disponible, detalleEstado, balcon, comedor);
        guardar(suite);
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
    public void guardar(Habitacion hab) {
        // Verificar si el numero de habitacion ya existe
        if (buscarPorIdInt(hab.getNumero()) != null) {
            throw new HabitacionDuplicadaException("Ya existe una habitacion con el numero: " + hab.getNumero());
        }
        habitaciones.add(hab);  // Agrega la habitación a la lista
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

    // Metodo para actualizar una habitación en la lista de habitaciones
    public void actualizarHabitacion(Habitacion habitacionActualizada) {
        // Buscar la habitación en la lista
        Habitacion habitacionExistente = buscarPorIdInt(habitacionActualizada.getNumero());

        if (habitacionExistente != null) {
            // Actualizar los valores de la habitación existente
            habitacionExistente.setEstado(habitacionActualizada.getEstado());
            habitacionExistente.setDisponible(habitacionActualizada.isDisponible());
            // Aquí puedes actualizar otros atributos según sea necesario
        } else {
            // Si la habitación no se encuentra, lanzamos una excepción o la agregamos (si lo deseas)
            throw new HabitacionInexistenteException("La habitación con el ID " + habitacionActualizada.getNumero() + " no existe.");
        }

        // Después de actualizar, guardamos la lista de habitaciones
        actualizarHabitacionesJson();
    }

    // Metodo para establecer el número de habitación automáticamente
    public int establecerNroHabitacion() {
        // Lista de números de habitaciones existentes
        Set<Integer> numerosHabitaciones = new HashSet<>();

        // Añadir los números de habitaciones actuales a un conjunto (Set)
        for (Habitacion h : habitaciones) {
            numerosHabitaciones.add(h.getNumero());
        }

        // Buscar el primer número faltante
        int siguienteNumero = 1;
        while (numerosHabitaciones.contains(siguienteNumero)) {
            siguienteNumero++;  // Si el número existe, buscamos el siguiente
        }

        // Retornar el siguiente número disponible
        return siguienteNumero;
    }

    public void guardarHabitaciones() {
        try {
            // Código para guardar las habitaciones actualizadas en el archivo JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("HotelManagement_I/habitaciones.json"), this.habitaciones);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar las habitaciones en el archivo JSON.");
        }
    }

    // Metodo para mostrar un mensaje temporal
    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Mostrar el alert
        alert.show();

        // Usamos un Timeline para cerrar el alert después de 2 segundos (2000 milisegundos)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }


    // Metodo para modificar una habitación (Suite)
    public void modificarHabitacionSuite(int idHabitacion, int capacidad, List<String> camas, boolean disponible, String estado, boolean balcon, boolean comedor) {
        Habitacion habitacion = buscarPorIdInt(idHabitacion);  // Buscar la habitación por ID
        if (habitacion != null && habitacion instanceof Suite) {
            Suite habitacionSuite = (Suite) habitacion;  // Convertimos a la clase Suite

            // Modificamos los atributos
            habitacionSuite.setCapacidad(capacidad);
            habitacionSuite.setCamas(camas);
            habitacionSuite.setDisponible(disponible);
            habitacionSuite.setEstado(EstadoHabitacion.valueOf(estado));
            habitacionSuite.setBalcon(balcon);
            habitacionSuite.setComedor(comedor);

            // Actualizamos la habitación modificada en la lista de habitaciones
            int index = habitaciones.indexOf(habitacion);  // Encontramos la posición de la habitación en la lista
            if (index != -1) {
                habitaciones.set(index, habitacionSuite);  // Reemplazamos la habitación en la lista
            }

            // Guardamos la habitación modificada
            actualizarHabitacionesJson();
        } else {
            throw new HabitacionInexistenteException("La habitación con ID " + idHabitacion + " no existe o no es una habitación de tipo Suite.");
        }
    }

    // Metodo para modificar una habitación (Presidencial)
    public void modificarHabitacionPresidencial(int idHabitacion, int capacidad, List<String> camas, boolean disponible, String estado, List<String> adicionales, double dimension) {
        Habitacion habitacion = buscarPorIdInt(idHabitacion);  // Buscar la habitación por ID
        if (habitacion != null && habitacion instanceof Presidencial) {
            Presidencial habitacionPresidencial = (Presidencial) habitacion;  // Convertimos a la clase Presidencial

            // Modificamos los atributos
            habitacionPresidencial.setCapacidad(capacidad);
            habitacionPresidencial.setCamas(camas);
            habitacionPresidencial.setDisponible(disponible);
            habitacionPresidencial.setEstado(EstadoHabitacion.valueOf(estado));
            habitacionPresidencial.setAdicionales(adicionales);
            habitacionPresidencial.setDimension(dimension);

            // Actualizamos la habitación modificada en la lista de habitaciones
            int index = habitaciones.indexOf(habitacion);  // Encontramos la posición de la habitación en la lista
            if (index != -1) {
                habitaciones.set(index, habitacionPresidencial);  // Reemplazamos la habitación en la lista
            }

            // Guardamos la habitación modificada
            actualizarHabitacionesJson();
        } else {
            throw new HabitacionInexistenteException("La habitación con ID " + idHabitacion + " no existe o no es una habitación de tipo Presidencial.");
        }
    }

    // Metodo para modificar una habitación (Apartamento)
    public void modificarHabitacionApartamento(int idHabitacion, int capacidad, List<String> camas, boolean disponible, String estado, int ambientes, boolean cocina) {
        Habitacion habitacion = buscarPorIdInt(idHabitacion);  // Buscar la habitación por ID
        if (habitacion != null && habitacion instanceof Apartamento) {
            Apartamento habitacionApartamento = (Apartamento) habitacion;  // Convertimos a la clase Apartamento

            // Modificamos los atributos
            habitacionApartamento.setCapacidad(capacidad);
            habitacionApartamento.setCamas(camas);
            habitacionApartamento.setDisponible(disponible);
            habitacionApartamento.setEstado(EstadoHabitacion.valueOf(estado));
            habitacionApartamento.setAmbientes(ambientes);
            habitacionApartamento.setCocina(cocina);

            // Actualizamos la habitación modificada en la lista de habitaciones
            int index = habitaciones.indexOf(habitacion);  // Encontramos la posición de la habitación en la lista
            if (index != -1) {
                habitaciones.set(index, habitacionApartamento);  // Reemplazamos la habitación en la lista
            }

            // Guardamos la habitación modificada
            actualizarHabitacionesJson();
        } else {
            throw new HabitacionInexistenteException("La habitación con ID " + idHabitacion + " no existe o no es una habitación de tipo Apartamento.");
        }
    }

    // Metodo para modificar una habitación (Doble)
    public void modificarHabitacionDoble(int idHabitacion, int capacidad, List<String> camas, boolean disponible, String estado) {
        Habitacion habitacion = buscarPorIdInt(idHabitacion);  // Buscar la habitación por ID
        if (habitacion != null && habitacion instanceof Doble) {
            Doble habitacionDoble = (Doble) habitacion;  // Convertimos a la clase Doble

            // Modificamos los atributos
            habitacionDoble.setCapacidad(capacidad);
            habitacionDoble.setCamas(camas);
            habitacionDoble.setDisponible(disponible);
            habitacionDoble.setEstado(EstadoHabitacion.valueOf(estado));

            // Actualizamos la habitación modificada en la lista de habitaciones
            int index = habitaciones.indexOf(habitacion);  // Encontramos la posición de la habitación en la lista
            if (index != -1) {
                habitaciones.set(index, habitacionDoble);  // Reemplazamos la habitación en la lista
            }

            // Guardamos los cambios en el archivo JSON
            actualizarHabitacionesJson();
        } else {
            throw new HabitacionInexistenteException("La habitación con ID " + idHabitacion + " no existe o no es una habitación de tipo Doble.");
        }
    }


    /*-----------------------  FIN METODOS ABM  -----------------------*/
}
