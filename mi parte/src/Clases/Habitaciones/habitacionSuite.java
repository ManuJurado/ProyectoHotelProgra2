package Clases.Habitaciones;

import java.util.List;

public class habitacionSuite extends Habitacion {

    private boolean balcon;
    private boolean comedor;


    public habitacionSuite(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado, boolean balcon, boolean comedor) {
        super(tipo, numero, capacidad, camas, disponible, estado, detalleEstado);
        this.balcon = balcon;
        this.comedor = comedor;
    }

    public habitacionSuite(boolean balcon, boolean comedor) {
        this.balcon = balcon;
        this.comedor = comedor;
    }

    public habitacionSuite() {
        super();
        this.balcon = true;
        this.comedor = true;
    }

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

    @Override
    public String toString() {
        return "habitacionSuite{" +
                "tipo='" + getTipo() + '\'' +
                ", numero=" + getNumero() +
                ", capacidad=" + getCapacidad() +
                ", balcon=" + isBalcon() +
                ", comedor=" + isComedor() +
                ", camas=" + getCamas() +
                ", disponible=" + isDisponible() +
                ", estado='" + getEstado() + '\'' +
                ", detalleEstado='" + getDetalleEstado() + '\'' +
                '}';
    }
}
