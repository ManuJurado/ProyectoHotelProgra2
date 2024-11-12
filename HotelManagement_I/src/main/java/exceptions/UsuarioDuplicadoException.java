package exceptions;

public class UsuarioDuplicadoException extends Exception {
    public UsuarioDuplicadoException(String e) {
        super("El usuario esta duplicado" + e);
    }
}
