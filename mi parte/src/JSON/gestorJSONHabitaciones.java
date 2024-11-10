package JSON;

import Clases.*;
import Clases.Habitaciones.*;
import org.json.JSONArray;
import org.json.JSONObject;
import Clases.Habitaciones.Habitacion;
import java.util.ArrayList;
import java.util.List;

public class gestorJSONHabitaciones {

    public static listaHabitaciones<Habitacion> cargarHabitaciones() {
        listaHabitaciones<Habitacion> listaHabitaciones = new listaHabitaciones<>();

        try {

            JSONArray jsonHabitaciones = new JSONArray(JSONUtiles.leer("habitaciones.json"));

            listaHabitaciones.setListaHabitaciones(listaHabitaciones(jsonHabitaciones));

        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON", e);
        }
        return listaHabitaciones;
    }

    public static List<Habitacion> listaHabitaciones(JSONArray jsonArray) {
        List<Habitacion> habitaciones = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String tipo = jsonObject.getString("tipo");

                switch (tipo) {
                    case "Doble":
                        habitacionDoble hDoble = new habitacionDoble();
                        hDoble.setCamas(jsonArrayToList(jsonObject.getJSONArray("camas")));
                        mapeoHabitacionIndividual(jsonObject, hDoble);
                        habitaciones.add(hDoble);
                        break;
                    case "Apartamento":
                        habitacionApartamento hApartamento = new habitacionApartamento();
                        hApartamento.setAmbientes(jsonObject.getInt("ambientes"));
                        hApartamento.setCocina(jsonObject.getBoolean("cocina"));
                        hApartamento.setCamas(jsonArrayToList(jsonObject.getJSONArray("camas")));
                        mapeoHabitacionIndividual(jsonObject, hApartamento);
                        habitaciones.add(hApartamento);
                        break;
                    case "Individual":
                        habitacionIndividual hIndividual = new habitacionIndividual();
                        hIndividual.setCamas(jsonArrayToList(jsonObject.getJSONArray("camas")));
                        mapeoHabitacionIndividual(jsonObject, hIndividual);
                        habitaciones.add(hIndividual);
                        break;
                    case "Presidencial":
                        habitacionPresidencial hPresidencial = new habitacionPresidencial();
                        hPresidencial.setDimension(jsonObject.getDouble("dimension"));
                        hPresidencial.setAdicionales(jsonArrayToList(jsonObject.getJSONArray("adicionales")));
                        hPresidencial.setCamas(jsonArrayToList(jsonObject.getJSONArray("camas")));
                        mapeoHabitacionIndividual(jsonObject, hPresidencial);
                        habitaciones.add(hPresidencial);
                        break;
                    case "Suite":
                        habitacionSuite hSuite = new habitacionSuite();
                        hSuite.setBalcon(jsonObject.getBoolean("balcon"));
                        hSuite.setComedor(jsonObject.getBoolean("comedor"));
                        hSuite.setCamas(jsonArrayToList(jsonObject.getJSONArray("camas")));
                        mapeoHabitacionIndividual(jsonObject, hSuite);
                        habitaciones.add(hSuite);
                        break;
                    default:
                        throw new RuntimeException("Tipo de habitación desconocido: " + tipo);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar la habitación", e);
            }
        }
        return habitaciones;
    }

    public static void mapeoHabitacionIndividual(JSONObject jsonObject, Habitacion habitacion) {
        try {
            habitacion.setNumero(jsonObject.getInt("numero"));
            habitacion.setCapacidad(jsonObject.getInt("capacidad"));
            habitacion.setDisponible(jsonObject.getBoolean("disponible"));
            habitacion.setEstado(jsonObject.getString("estado"));
            habitacion.setDetalleEstado(jsonObject.getString("detalleEstado"));
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear la habitación", e);
        }
    }

    private static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getString(i));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
