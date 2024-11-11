package Clases;

import JSON.JSONUtiles;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.List;

public class listaReservas {

    List<Reserva> listaReservas;

    public listaReservas(List<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void setListaReservas(List<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    public void agregarReserva(Reserva reserva) {
        listaReservas.add(reserva);
    }
    public void listaReservas(Reserva reserva)
    {
        listaReservas.add(reserva);
    }

    public void cargarReservasDesdeJSON() {
        try (FileReader reader = new FileReader("reservas.json")) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray reservasArray = new JSONArray(tokener);

            for (int i = 0; i < reservasArray.length(); i++) {
                JSONObject reservaJSON = reservasArray.getJSONObject(i);

                int id = reservaJSON.getInt("id");
                String usuarioNombre = reservaJSON.getString("usuario");
                String habitacionId = reservaJSON.getString("habitacionId");
                String fechaReserva = reservaJSON.getString("fechaReserva");
                String estadoReserva = reservaJSON.getString("estadoReserva");

                // Agregar la reserva a la lista después de crearla
                listaReservas.add(new Reserva(id, usuarioNombre, habitacionId, fechaReserva, estadoReserva));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void guardarReservasComoJSON() {
        JSONArray reservasArray = new JSONArray();

        for (Reserva reserva : listaReservas) {
            JSONObject reservaJSON = new JSONObject();
            try {
                reservaJSON.put("id", reserva.getId());
                reservaJSON.put("usuario", reserva.getUsuario().getNombre());
                reservaJSON.put("habitacionId", reserva.getHabitacion().getNumero());
                reservaJSON.put("fechaReserva", reserva.getFechaReserva());
                reservaJSON.put("estadoReserva", reserva.getEstadoReserva());
            } catch (Exception e) {
                e.printStackTrace();
            }
            reservasArray.put(reservaJSON);
        }

        JSONUtiles.grabarR(reservasArray, "reservas.json");
    }

    @Override
    public String toString() {
        return "listaReservas{" +
                "listaReservas=" + listaReservas +
                '}';
    }
}
