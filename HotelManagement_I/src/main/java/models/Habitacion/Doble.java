package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Doble extends Habitacion {
    //Constructor
    public Doble() {
    }

    public Doble(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
    }
}
