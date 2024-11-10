package controllers.details;

import enums.TipoUsuario;

public class DatosUsuario {

    // Campos para almacenar los datos del usuario
    private static String nombre;
    private static String email;
    private static TipoUsuario tipoUsuario;  // Nuevo campo para el tipo de usuario

    // Métodos para acceder y modificar el nombre
    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        DatosUsuario.nombre = nombre;
    }

    // Métodos para acceder y modificar el email
    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        DatosUsuario.email = email;
    }

    // Métodos para acceder y modificar el tipo de usuario
    public static TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public static void setTipoUsuario(TipoUsuario tipoUsuario) {
        DatosUsuario.tipoUsuario = tipoUsuario;
    }

    // Metodo para limpiar los datos después de usarlos
    public static void limpiarDatos() {
        nombre = null;
        email = null;
        tipoUsuario = null;
    }
}
