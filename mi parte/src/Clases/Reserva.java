package Clases;
import java.time.LocalDate;

public class Reserva {
    private Pasajero pasajero;
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Reserva(Pasajero pasajero, Habitacion fechaInicio, LocalDate habitacion, LocalDate fechaFin) {
        this.pasajero = pasajero;
        this.fechaInicio = fechaInicio;
        this.habitacion = habitacion;
        this.fechaFin = fechaFin;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String toString() {
        return "Reserva [pasajero=" + pasajero.getNombre() + ", habitacion=" + habitacion.getNumero() +
                ", desde=" + fechaInicio + ", hasta=" + fechaFin + "]";
    }
}
