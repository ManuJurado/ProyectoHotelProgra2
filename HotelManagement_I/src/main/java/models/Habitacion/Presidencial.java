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

    public Presidencial(int numero, int capacidad, List<String> camas, boolean disponible, String detalleEstado, List<String> adicionales, double dimension) {
        super(numero, capacidad, camas, disponible, detalleEstado);
        super.setTipo("PRESIDENCIAL");
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

    public void setDimension(double dimension) throws IllegalArgumentException {
        if (dimension < 9 || dimension > 200) {
            throw new IllegalArgumentException("La dimension ingresada debe estar entre 9m2 y 200m2.");
        }
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
