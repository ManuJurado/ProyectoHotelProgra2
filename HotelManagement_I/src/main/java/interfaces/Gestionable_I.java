package interfaces;

import exceptions.UsuarioDuplicadoException;

public interface Gestionable_I<T> {
        // Metodo para buscar un objeto T por su identificador (puede ser un número o cualquier tipo)
        T buscarPorId(String id);

        // Metodo para eliminar un objeto T por un identificador
        boolean eliminar(String id);

        // Metodo para guardar un objeto T
        void guardar(T objeto) throws UsuarioDuplicadoException;
}
