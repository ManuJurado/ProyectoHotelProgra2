package ManejoJSON;

import java.io.*;
import org.json.JSONArray;
import org.json.JSONTokener;

public class JSONUtiles {

    public static void grabar(JSONArray array) {
        try {
            FileWriter file = new FileWriter("habitaciones.json");
            file.write(array.toString());
            file.flush();
            file.close();
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