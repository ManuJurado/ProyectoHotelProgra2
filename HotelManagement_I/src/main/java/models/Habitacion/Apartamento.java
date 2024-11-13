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

    public Apartamento(int numero, int capacidad, List<String> camas, boolean disponible, String detalleEstado, int ambientes, boolean cocina) {
        super(numero, capacidad, camas, disponible, detalleEstado);
            super.setTipo("APARTAMENTO");
        this.ambientes = ambientes;
        this.cocina = cocina;
    }

    //Getters y Setters
    public int getAmbientes() {
        return ambientes;
    }

    //Si la cantidad de ambientes no cumple las condiciones se lanza una excepcion
    public void setAmbientes(int ambientes) throws IllegalArgumentException{
        if (ambientes < 1 || ambientes > 10) {
            throw new IllegalArgumentException("La cantidad de ambientes debe ser mayor a 0.");
        }
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
