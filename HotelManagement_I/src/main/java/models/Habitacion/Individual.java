package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Individual extends Habitacion {
    //Constructor
    public Individual() {
    }

    public Individual(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
    }
}
