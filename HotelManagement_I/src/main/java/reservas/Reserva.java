package reservas;

import java.util.ArrayList;
import java.util.List;

// Clase Reserva para manejar reservas
public class Reserva {
    private String nombreCliente;
    private String fechaReserva;

    // Lista para almacenar las reservas
    private static List<Reserva> reservas = new ArrayList<>();

    // Constructor
    public Reserva(String nombreCliente, String fechaReserva) {
        this.nombreCliente = nombreCliente;
        this.fechaReserva = fechaReserva;
    }

    // Métodos para obtener datos
    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    // Metodo para crear una nueva reserva
    public static void crearReserva(String nombreCliente, String fechaReserva) {
        Reserva nuevaReserva = new Reserva(nombreCliente, fechaReserva);
        reservas.add(nuevaReserva);
        System.out.println("Reserva creada: " + nombreCliente + " para la fecha " + fechaReserva);
    }

    // Metodo para obtener todas las reservas
    public static List<Reserva> obtenerReservas() {
        return reservas;
    }
}
