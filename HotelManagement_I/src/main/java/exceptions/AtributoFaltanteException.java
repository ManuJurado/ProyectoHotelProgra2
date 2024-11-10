package exceptions;

public class AtributoFaltanteException extends Exception {
    public AtributoFaltanteException(String mensaje) {
        super(mensaje); // Llama al constructor de la clase Exception con el mensaje
    }
}
