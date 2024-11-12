package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Doble extends Habitacion {
    //Constructor
    public Doble() {
    }

    public Doble(String tipo, int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        super(tipo, numero, camas, disponible, estado, detalleEstado);
        super.setCapacidad(2);
    }
}
