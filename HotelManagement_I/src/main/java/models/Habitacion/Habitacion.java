package models.Habitacion;

import exceptions.CapacidadInvalidaException;
import exceptions.NroHabitacionInvalidoException;
import enums.EstadoHabitacion;
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

    public Habitacion(Integer numero, int capacidad, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.camas = camas;
        this.disponible = disponible;
        this.estado = estado;
        this.detalleEstado = detalleEstado;
    }

    public Habitacion(int numero, List<String> camas, boolean disponible, EstadoHabitacion estado, String detalleEstado) {
        this.numero = numero;
        this.camas = camas;
        this.estado = estado;
        this.disponible = disponible;
        this.detalleEstado = detalleEstado;
    }

    //Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) throws IllegalArgumentException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de habitacion.");
        }
        this.tipo = tipo;
    }

    public int getNumero() {
        return numero;
    }

    //Arroja una excepcion si se ingresa un numero de habitacion menor a 1
    public void setNumero(int numero) throws NroHabitacionInvalidoException{
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

    public void setCamas(List<String> camas) throws IllegalArgumentException {
        if(camas.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una cama.");
        }
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

    public void setEstado(EstadoHabitacion estado) throws IllegalArgumentException {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede estar vacio.");
        }
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
