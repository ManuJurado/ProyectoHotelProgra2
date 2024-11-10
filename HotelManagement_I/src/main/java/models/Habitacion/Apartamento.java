package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Apartamento extends Habitacion {
    //Atributos
    private int ambientes;
    private boolean cocina;

    //Constructor
    public Apartamento() {
    }

    public Apartamento(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, int ambientes, boolean cocina) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        this.ambientes = ambientes;
        this.cocina = cocina;
    }

    //Getters y Setters
    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public boolean isCocina() {
        return cocina;
    }

    public void setCocina(boolean cocina) {
        this.cocina = cocina;
    }

    //Sobreescritura de metodos
    @Override
    public String toString() {
        return "Apartamento{" +
                "ambientes=" + ambientes +
                ", cocina=" + cocina +
                '}' + super.toString();
    }
}
