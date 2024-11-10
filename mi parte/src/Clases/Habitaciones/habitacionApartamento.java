package Clases.Habitaciones;

import java.util.List;

public class habitacionApartamento extends Habitacion {
    private int ambientes;
    private boolean cocina;


    public habitacionApartamento(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado, int ambientes, boolean cocina) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        this.ambientes = ambientes;
        this.cocina = cocina;
    }

    public habitacionApartamento() {
        super();
        this.ambientes = 0;
        this.cocina = false;
    }

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

    public String toString() {
        return "habitacionApartamento{" +
                "tipo='" + getTipo() + '\'' +
                ", numero=" + getNumero() +
                ", capacidad=" + getCapacidad() +
                ", ambientes=" + ambientes +
                ", cocina=" + cocina +
                ", camas=" + getCamas() +
                ", disponible=" + isDisponible() +
                ", estado='" + getEstado() + '\'' +
                ", detalleEstado='" + getDetalleEstado() + '\'' +
                '}';
    }

}
