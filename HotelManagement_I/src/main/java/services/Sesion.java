package services;

import models.Usuarios.*;

public class Sesion {

    private static Usuario usuarioLogueado; // Usuario logueado (puede ser Administrador, Cliente, etc.)

    // Metodo para obtener el usuario logueado
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    // Metodo para establecer el usuario logueado
    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    // Metodo para verificar si el usuario es Administrador
    public static boolean esAdministrador() {
        return usuarioLogueado instanceof Administrador;
    }

    // Metodo para verificar si el usuario es Cliente
    public static boolean esCliente() {
        return usuarioLogueado instanceof Cliente;
    }

    // Metodo para verificar si el usuario es Conserje
    public static boolean esConserje() {
        return usuarioLogueado instanceof Conserje;
    }
}
