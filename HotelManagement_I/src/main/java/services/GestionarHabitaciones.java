package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import models.Habitacion;
import java.util.ArrayList;
import java.util.List;

public class GestionarHabitaciones {

    private List<Habitacion> habitaciones; // Lista de habitaciones

    public GestionarHabitaciones() {
        habitaciones = new ArrayList<>();
        cargarHabitacionesHardcodeadas(); // Cargar habitaciones hardcodeadas
    }

    private void cargarHabitacionesHardcodeadas() {
        agregarHabitacion(new Habitacion("Simple", "disponible", 20, 2));
        agregarHabitacion(new Habitacion("Doble", "no disponible", 30, 3));
        agregarHabitacion(new Habitacion("Suite", "disponible", 50, 1));
        agregarHabitacion(new Habitacion("Familiar", "disponible", 60, 4));
        // Puedes agregar más habitaciones según necesites
    }

    public void agregarHabitacion(Habitacion nuevaHabitacion) {
        // Establecer el número de habitación como la cantidad actual de habitaciones + 1
        int nuevoNumeroHabitacion = habitaciones.size() + 1; // Asumiendo que 'habitaciones' es la lista que almacena las habitaciones
        nuevaHabitacion.setNroHabitacion(nuevoNumeroHabitacion); // Necesitas un setter para nroHabitacion

        habitaciones.add(nuevaHabitacion); // Agrega la habitación a la lista
    }

    // Método para modificar una habitación existente
    public void modificarHabitacion(Habitacion habitacion, String nuevoTipo, String nuevoEstado, int nuevosMetrosCuadrados, int nuevaCantidadCamas) {
        habitacion.setTipoHabitacion(nuevoTipo);
        habitacion.setEstado(nuevoEstado);
        habitacion.setMetrosCuadrados(nuevosMetrosCuadrados);
        habitacion.setCantidadCamas(nuevaCantidadCamas);
        habitacion.setCapacidad(nuevaCantidadCamas); // Asumiendo que la capacidad es igual a la cantidad de camas
    }

    // Método para eliminar una habitación
    public void eliminarHabitacion(Habitacion habitacion) {
        habitaciones.remove(habitacion);
    }

    // Método para obtener la lista de habitaciones
    public ObservableList<Habitacion> obtenerHabitaciones() {
        return FXCollections.observableArrayList(habitaciones);
    }

    // Método para habilitar o inhabilitar una habitación
    public void habilitarInhabilitarHabitacion(Habitacion habitacion) {
        String nuevoEstado = habitacion.getEstado().equals("disponible") ? "no disponible" : "disponible";
        habitacion.setEstado(nuevoEstado);
    }

    public List<Habitacion> getHabitaciones(){
        return habitaciones;
    }


}
