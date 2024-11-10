package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Presidencial extends Habitacion {
    //Atributos
    private List<String> adicionales;
    private double dimension;

    //Constructor
    public Presidencial() {
    }

    public Presidencial(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, List<String> adicionales, double dimension) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        this.adicionales = adicionales;
        this.dimension = dimension;
    }

    //Getters y Setters
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

    //Sobreescritura de metodos
    @Override
    public String toString() {
        return "Presidencial{" +
                "adicionales=" + adicionales +
                ", dimension=" + dimension +
                '}' + super.toString();
    }
}
