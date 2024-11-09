package manejoJson;

import enums.EstadoHabitacion;
import models.Habitacion.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


//Se realiza el mapeo de los 3 Json del proyecto
public class GestionJSON {

    public static List<Habitacion> mapeoHabitaciones(String archivoJson) {

        List<Habitacion> habitaciones = new ArrayList<>();

        try {
            JSONArray JHabitaciones = new JSONArray(JSONUtiles.leer(archivoJson));

            for (int i = 0; i < JHabitaciones.length(); i++) {

                JSONObject JHabitacion = JHabitaciones.getJSONObject(i);

                if (JHabitacion.getString("tipo").equals("Apartamento")) {
                    Apartamento a = new Apartamento();
                    a.setAmbientes(JHabitacion.getInt("ambientes"));
                    a.setCocina(JHabitacion.getBoolean("cocina"));
                    mapeoHabitacion(JHabitacion, a);
                    habitaciones.add(a);
                } else if (JHabitacion.getString("tipo").equals("Doble")) {
                    Habitacion d = new Doble();
                    mapeoHabitacion(JHabitacion, d);
                    habitaciones.add(d);
                } else if (JHabitacion.getString("tipo").equals("Individual")) {
                    Habitacion ind = new Individual();
                    mapeoHabitacion(JHabitacion, ind);
                    habitaciones.add(ind);
                } else if (JHabitacion.getString("tipo").equals("Presidencial")) {
                    Presidencial p = new Presidencial();
                    JSONArray JAdicionales = JHabitacion.getJSONArray("adicionales");
                    List<String> adicionales = new ArrayList<>();
                    for (int z = 0; z < JAdicionales.length(); z++){
                        adicionales.add(JAdicionales.getString(z));
                    }
                    p.setAdicionales(adicionales);
                    p.setDimension(JHabitacion.getDouble("dimension"));
                    mapeoHabitacion(JHabitacion, p);
                    habitaciones.add(p);
                } else if (JHabitacion.getString("tipo").equals("Suite")) {
                    Suite s = new Suite();
                    s.setBalcon(JHabitacion.getBoolean("balcon"));
                    s.setComedor(JHabitacion.getBoolean("comedor"));
                    mapeoHabitacion(JHabitacion, s);
                    habitaciones.add(s);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return habitaciones;
    }

    public static void mapeoHabitacion(JSONObject JHabitacion, Habitacion habitacion) {

        try {

            habitacion.setTipo(JHabitacion.getString("tipo"));
            habitacion.setNumero(JHabitacion.getInt("numero"));
            habitacion.setCapacidad(JHabitacion.getInt("capacidad"));

            JSONArray JCamas = JHabitacion.getJSONArray("camas");
            List<String> camas = new ArrayList<>();
            for (int i = 0; i < JCamas.length(); i++) {
                camas.add(JCamas.getString(i));
            }
            habitacion.setCamas(camas);

            habitacion.setDisponible(JHabitacion.getBoolean("disponible"));
            habitacion.setEstado(EstadoHabitacion.valueOf(JHabitacion.getString("estado").toUpperCase()));
            habitacion.setDetalleEstado(JHabitacion.getString("detalleEstado"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
