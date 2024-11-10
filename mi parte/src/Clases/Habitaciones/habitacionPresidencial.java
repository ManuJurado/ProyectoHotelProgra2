package Clases.Habitaciones;

import java.util.List;

public class habitacionPresidencial extends Habitacion {

    private List<String> adicionales;
    private double dimension;

    public habitacionPresidencial(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado, List<String> adicionales, double dimension) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);  // Llamamos al constructor de la clase base
        this.adicionales = adicionales;
        this.dimension = dimension;
    }

    public habitacionPresidencial() {
        super();
        this.adicionales = null;
        this.dimension = 0;
    }

    public List<String> getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(List<String> adicionales) {
        this.adicionales = adicionales;
    }

    public double getDimension() {
        return dimension;
    }

    public void setDimension(double dimension) {
        this.dimension = dimension;
    }

    public String toString() {
        return "habitacionPresidencial{" +
                "tipo='" + getTipo() + '\'' +
                ", numero=" + getNumero() +
                ", capacidad=" + getCapacidad() +
                ",adicionales=" +getAdicionales() +
                ",dimension=" +getDimension() +
                ", camas=" + camas +
                ", disponible=" + isDisponible() +
                ", estado='" + getEstado() + '\'' +
                ", detalleEstado='" + getDetalleEstado() + '\'' +
                '}';
    }

}
