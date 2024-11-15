package controllers;

import models.Habitacion.Habitacion;
import models.Reserva;

public class GlobalData {
    // Atributo estático para almacenar la habitación
    private static Habitacion habitacionSeleccionada;
    private static Reserva reservaSeleccionada;

    // Metodo estático para establecer la habitación seleccionada
    public static void setHabitacionSeleccionada(Habitacion habitacion) {
        habitacionSeleccionada = habitacion;
    }

    // Metodo estático para obtener la habitación seleccionada
    public static Habitacion getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }

    public static void setReservaSeleccionada(Reserva reseraSeleccionada) {
        GlobalData.reservaSeleccionada = reseraSeleccionada;
    }

    public static Reserva getReservaSeleccionada() {
        return reservaSeleccionada;
    }

    public static void limpiarReservaSeleccionada() {
        GlobalData.reservaSeleccionada = null;
    }

    public static void limpiarHabitacionSeleccionada() {
        GlobalData.habitacionSeleccionada = null;
    }

}