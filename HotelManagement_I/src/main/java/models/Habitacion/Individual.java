package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Individual extends Habitacion {
    //Constructor
    public Individual() {
    }

    public Individual(int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        super(numero, camas, disponible, estado, detalleEstado);
        super.setTipo("INDIVIDUAL");
        super.setCapacidad(1);
    }
}
