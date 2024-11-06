package main.java.services;

import main.java.models.Habitacion.Habitacion;
import java.util.*;

public class GestionHabitaciones <H extends Habitacion> {
    //Atributos
    private List<H> habitaciones;

    //Constructor
    public GestionHabitaciones() {
        this.habitaciones = new ArrayList<>();
    }

    //Getter y Setter
    public List<H> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<H> habitaciones) {
        this.habitaciones = habitaciones;
    }

    /**METODOS DE FILTRADO*/
    //Metodo que devuelve todas las habitaciones que se encuentran disponibles para alquilar
    public List<Habitacion> listadoHabitacionesDisponibles() {

        List<Habitacion> habitacionesDisponibles = new ArrayList<>();

        for (Habitacion h : habitaciones) {

            if (h.isDisponible()) {
                habitacionesDisponibles.add(h);
            }
        }

        return habitacionesDisponibles;
    }

    //Metodo que devuelve todas las habitaciones que estan alquiladas

}
