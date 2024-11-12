package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Doble extends Habitacion {
    //Constructor
    public Doble() {
    }

    public Doble(int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        super(numero, camas, disponible, estado, detalleEstado);
        super.setTipo("DOBLE");
        super.setCapacidad(2);
    }
}
