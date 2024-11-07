package models.Habitacion;

import main.java.exceptions.CapacidadInvalidaException;
import main.java.exceptions.NroHabitacionInvalidoException;
import main.java.enums.EstadoHabitacion;

import java.util.*;

public abstract class Habitacion {
    //Atributos
    private String tipo;
    private int numero;
    private int capacidad;
    private List<String> camas;
    private boolean disponible;
    private EstadoHabitacion estado;  //(alquilada, limpieza, reparación, desinfección, etc. Detallar el motivo)
    private String detalleEstado;

    //Constructor
    public Habitacion() {
        this.camas = new ArrayList<>();
    }

    //Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumero() {
        return numero;
    }

    //Arroja una excepcion si se ingresa un numero de habitacion menor a 1
    public void setNumero(int numero) {
        if (numero < 0) {
            throw new NroHabitacionInvalidoException("El Nro de habitacion debe ser mayor a 0");
        }
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    //Arroja una excepcion si se ingresa una capacidad menor a 1
    public void setCapacidad(int capacidad) {
        if (capacidad < 1) {
            throw new CapacidadInvalidaException("La capacidad debe ser mayor a 0");
        }
        this.capacidad = capacidad;
    }

    public List<String> getCamas() {
        return camas;
    }

    public void setCamas(List<String> camas) {
        this.camas = camas;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public String getDetalleEstado() {
        return detalleEstado;
    }

    public void setDetalleEstado(String detalleEstado) {
        this.detalleEstado = detalleEstado;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "tipo='" + tipo + '\'' +
                ", numero=" + numero +
                ", capacidad=" + capacidad +
                ", camas=" + camas +
                ", disponible=" + disponible +
                ", estado=" + estado +
                ", detalleEstado='" + detalleEstado + '\'' +
                '}';
    }
}
