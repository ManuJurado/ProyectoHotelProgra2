package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Individual extends Habitacion {
    //Constructor
    public Individual() {
    }

    public Individual(int numero, List<String> camas, boolean disponible, String detalleEstado) {
        super(numero, camas, disponible, detalleEstado);
        super.setTipo("INDIVIDUAL");
        super.setCapacidad(1);
    }
}
