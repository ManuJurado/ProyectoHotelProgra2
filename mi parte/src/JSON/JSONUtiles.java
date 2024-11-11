package JSON;

import java.io.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class JSONUtiles {

    public static void grabar(JSONArray array, String nombreArchivo) {
        try {
            FileWriter file = new FileWriter("reservas.json");
            file.write(array.toString());
            file.flush();
            file.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void grabarR(JSONArray array, String nombreArchivo) {
        try {

            // Leer el archivo actual si existe, para no sobrescribirlo
            JSONArray reservasExistentes = new JSONArray();
            File file = new File(nombreArchivo);

            if (file.exists()) {
                // Si el archivo existe, leer su contenido
                FileReader fileReader = new FileReader(nombreArchivo);
                JSONTokener tokener = new JSONTokener(fileReader);
                try {
                    reservasExistentes = new JSONArray(tokener);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                fileReader.close();
            }

            // Añadir las nuevas reservas al array existente
            for (int i = 0; i < array.length(); i++) {
                try {
                    reservasExistentes.put(array.get(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            // Escribir el array combinado (existente y nuevo)
            FileWriter file2 = new FileWriter(nombreArchivo);
            file2.write(reservasExistentes.toString());
            file2.flush();
            file2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static JSONTokener leer(String archivo) {
        JSONTokener tokener = null;

        try {
            tokener = new JSONTokener(new FileReader(archivo));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokener;
    }
}