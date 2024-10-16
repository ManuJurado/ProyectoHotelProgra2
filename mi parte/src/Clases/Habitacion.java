package Clases;

public class Habitacion {
    private  int numero;
    private String tipo;
    private boolean estadoDisponible;
    private String estado;

    public Habitacion(int numero, String tipo, boolean estadoDisponible, String estado) {
        this.numero = numero;
        this.tipo = tipo;
        this.estadoDisponible = estadoDisponible;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isEstadoDisponible() {
        return estadoDisponible;
    }

    public void setEstadoDisponible(boolean estadoDisponible) {
        this.estadoDisponible = estadoDisponible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String toString() {
        return "Habitacion [numero: " + numero + ", tipo: " + tipo + ", estado: " + estado + "]";
    }
}
