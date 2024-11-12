package models.Habitacion;

import enums.EstadoHabitacion;

import java.util.List;

public class Suite extends Habitacion {
    //Atributos
    private boolean balcon;
    private boolean comedor;

    //Constructor
    public Suite() {
    }

    public Suite(int numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado, boolean balcon, boolean comedor) {
        super(numero, capacidad, camas, disponible, estado, detalleEstado);
        super.setTipo("SUITE");
        this.balcon = balcon;
        this.comedor = comedor;
    }

    //Getters y Setters
    public boolean isBalcon() {
        return balcon;
    }

    public void setBalcon(boolean balcon) {
        this.balcon = balcon;
    }

    public boolean isComedor() {
        return comedor;
    }

    public void setComedor(boolean comedor) {
        this.comedor = comedor;
    }

    //Sobreescritura de metodos

    @Override
    public String toString() {
        return "Suite{" +
                "balcon=" + balcon +
                ", comedor=" + comedor +
                '}' + super.toString();
    }
}
