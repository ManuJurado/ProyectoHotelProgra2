package manejoJson;

import java.io.*;
import org.json.JSONArray;
import org.json.JSONTokener;

public class JSONUtiles {

    //Se agrega parametro para pasarle el nombre del archivo y reutilizar el metodo
    public static void grabar(JSONArray array, String fileName) {
        try {
            FileWriter file = new FileWriter(fileName);
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