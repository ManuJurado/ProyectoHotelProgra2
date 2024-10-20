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
        habitaciones.add(new Habitacion("Simple", "Disponible", 20, 1));
        habitaciones.add(new Habitacion("Doble", "No disponible", 30, 2));
        habitaciones.add(new Habitacion("Suite", "Disponible", 50, 3));
        habitaciones.add(new Habitacion("Familiar", "Disponible", 60, 4));
        // Puedes agregar más habitaciones según necesites
    }

    // Método para agregar una nueva habitación
    public void agregarHabitacion(Habitacion habitacion) {
        habitaciones.add(habitacion);
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

}
