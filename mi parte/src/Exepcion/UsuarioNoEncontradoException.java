package Exepcion;

public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}