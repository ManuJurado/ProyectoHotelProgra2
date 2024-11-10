package Clases.Habitaciones;

import java.util.List;

public class habitacionIndividual extends Habitacion {

    public habitacionIndividual(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
    }

    public habitacionIndividual() {
        super();
    }

    public String toString() {
        return "habitacionIndividual{" +
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
