package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Doble extends Habitacion {
    //Constructor
    public Doble() {
    }

    public Doble(int numero, List<String> camas, boolean disponible, String detalleEstado) {
        super(numero, camas, disponible, detalleEstado);
        super.setTipo("DOBLE");
        super.setCapacidad(2);
    }
}
