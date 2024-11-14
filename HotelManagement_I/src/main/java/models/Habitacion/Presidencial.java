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
        super.setTipo("Presidencial");
        this.adicionales = adicionales;
        this.dimension = dimension;
    }

    // Métodos para agregar o quitar elementos adicionales
    public void setMesaPool(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Mesa de Pool")) {
                adicionales.add("Mesa de Pool");
            }
        } else {
            adicionales.remove("Mesa de Pool");
        }
    }

    public void setJacuzzi(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Jacuzzi")) {
                adicionales.add("Jacuzzi");
            }
        } else {
            adicionales.remove("Jacuzzi");
        }
    }

    public void setCine(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Cine")) {
                adicionales.add("Cine");
            }
        } else {
            adicionales.remove("Cine");
        }
    }

    public void setEntretenimiento(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Entretenimiento")) {
                adicionales.add("Entretenimiento");
            }
        } else {
            adicionales.remove("Entretenimiento");
        }
    }

    public void setTerraza(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Terraza")) {
                adicionales.add("Terraza");
            }
        } else {
            adicionales.remove("Terraza");
        }
    }

    public void setSauna(boolean estaDisponible) {
        if (estaDisponible) {
            if (!adicionales.contains("Sauna")) {
                adicionales.add("Sauna");
            }
        } else {
            adicionales.remove("Sauna");
        }
    }

    // Métodos para verificar si un adicional está presente
    public boolean tieneMesaPool() {
        return adicionales.contains("Mesa de Pool");
    }

    public boolean tieneJacuzzi() {
        return adicionales.contains("Jacuzzi");
    }

    public boolean tieneCine() {
        return adicionales.contains("Cine");
    }

    public boolean tieneEntretenimiento() {
        return adicionales.contains("Entretenimiento");
    }

    public boolean tieneTerraza() {
        return adicionales.contains("Terraza");
    }

    public boolean tieneSauna() {
        return adicionales.contains("Sauna");
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
