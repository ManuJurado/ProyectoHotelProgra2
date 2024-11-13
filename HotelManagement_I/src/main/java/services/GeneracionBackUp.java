package services;

import exceptions.BackUpException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneracionBackUp {

    public static boolean GenerarBackUp(){

        LocalDateTime fechaActual = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        Path habitacionesPath = Paths.get("HotelManagement_I/habitaciones.json");
        Path usuariosPath = Paths.get("HotelManagement_I/usuarios.json");
        Path reservasPath = Paths.get("HotelManagement_I/reservas.json");
        Path habitacionesBkpPath = Paths.get("HotelManagement_I/src/main/java/backUp/habitacionesBkp"+ fechaActual.format(formatoFecha).toString() +".json");
        Path usuariosBkpPath = Paths.get("HotelManagement_I/src/main/java/backUp/usuariosBkp"+ fechaActual.format(formatoFecha).toString() +".json");
        Path reservasBkpPath = Paths.get("HotelManagement_I/src/main/java/backUp/reservasBkp"+ fechaActual.format(formatoFecha).toString() +".json");

        try{
            if (Files.exists(habitacionesPath)){
                Files.copy(habitacionesPath, habitacionesBkpPath);
            }
            if (Files.exists(usuariosPath)){
                Files.copy(usuariosPath, usuariosBkpPath);
            }
            if (Files.exists(reservasPath)){
                Files.copy(reservasPath, reservasBkpPath);
            }

            return true;
        } catch (BackUpException | IOException e) {
            throw new BackUpException("El backup no se ha podido realizar.");
        }
    }
}
