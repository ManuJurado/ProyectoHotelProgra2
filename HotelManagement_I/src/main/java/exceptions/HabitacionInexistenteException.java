package exceptions;

public class HabitacionInexistenteException extends RuntimeException {
    public HabitacionInexistenteException(String message) {
        super(message);
    }
}
