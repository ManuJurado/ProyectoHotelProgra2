package Clases;
import java.util.ArrayList;
import java.util.List;

public class Historial  {
    private Pasajero pasajero;
    private List<Reserva> reservas;

    public Historial(Pasajero pasajero) {
        this.pasajero = pasajero;
        this.reservas = new ArrayList<>();
    }

    public void agregarReserva (Reserva reserva)
    {
        reservas.add(reserva);
    }
    public List<Reserva> getReservas() {
        return reservas;
    }

    public String toString() {
        StringBuilder historial = new StringBuilder();
        historial.append("Historial del pasajero " + pasajero.getNombre() + ":\n");  /// para el nombre del pasajero
        for (Reserva reserva : reservas) {
            historial.append(reserva.toString() + "\n");  /// la informacion extra osea el historial del pasajero
        }
        return historial.toString();
    }


}
