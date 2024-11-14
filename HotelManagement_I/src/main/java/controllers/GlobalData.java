package controllers;

import models.Habitacion.Habitacion;

public class GlobalData {
    // Atributo estático para almacenar la habitación
    private static Habitacion habitacionSeleccionada;

    // Metodo estático para establecer la habitación seleccionada
    public static void setHabitacionSeleccionada(Habitacion habitacion) {
        habitacionSeleccionada = habitacion;
    }

    // Metodo estático para obtener la habitación seleccionada
    public static Habitacion getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }
}