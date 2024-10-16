package Clases;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Gestionamiento {

    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;

    public Gestionamiento(List<Habitacion> habitaciones, List<Reserva> reservas) {
        this.habitaciones = habitaciones;
        this.reservas = reservas;
    }

    public void agregarHabitacion(Habitacion habitacion) {
        habitaciones.add(habitacion);
    }

    public List<Habitacion> habitacionesDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.isEstadoDisponible()) {  /// al ser booleano el get es asi
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    public boolean reservarHabitacion(Pasajero pasajero, Habitacion habitacion, LocalDate inicio, LocalDate fin) {
        if (habitacion.isEstadoDisponible()) {
            Reserva reserva = new Reserva(pasajero,habitacion, inicio, fin);
            reservas.add(reserva);
            habitacion.setEstadoDisponible(false); /// la habitación queda no disponible
            habitacion.setEstado("reservada");
            return true;
        }
        return false; /// si la habitación no está disponible
    }
}
