package Clases;

import Clases.Habitaciones.Habitacion;

import java.util.List;

public class listaHabitaciones<T extends Habitacion> {

    private List<T> listaHabitaciones;

    public listaHabitaciones() {
    }

    public List<T> getListaHabitaciones() {
        return listaHabitaciones;
    }

    public void setListaHabitaciones(List<T> listaHabitaciones) {
        this.listaHabitaciones = listaHabitaciones;
    }

    @Override
    public String toString() {
        return "listaHabitaciones{" +
                "listaHabitaciones=" + listaHabitaciones +
                '}';
    }

}
