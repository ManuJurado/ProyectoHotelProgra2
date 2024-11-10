package Clases.Habitaciones;

import java.util.List;

public class habitacionDoble extends Habitacion {

    public habitacionDoble(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
    }

    public habitacionDoble() {
        super();
    }

    public String toString() {
        return "habitacionDoble{" +
                "tipo='" + getTipo() + '\'' +
                ", numero=" + getNumero() +
                ", capacidad=" + getCapacidad() +
                ", camas=" + camas +
                ", disponible=" + isDisponible() +
                ", estado='" + getEstado() + '\'' +
                ", detalleEstado='" + getDetalleEstado() + '\'' +
                '}';
    }
}
