package exceptions;

public class ReservaNoEncontradaException extends RuntimeException {
    public ReservaNoEncontradaException(String message) {
        super(message);
    }
}
