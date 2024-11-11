package interfaces;

import models.Usuarios.Usuario;

public interface Gestionable_I<T> {
        // Metodo para guardar un objeto T
        void guardar(T objeto);

        // Metodo para eliminar un objeto T por un identificador
        boolean eliminar(String id);

        // Metodo para buscar un objeto T por un identificador
        T buscarPorId(String id);

}