package services;
import models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionarUsuarios {
    private List<Usuario> usuarios;

    public GestionarUsuarios() {
        usuarios = new ArrayList<>();
        // Agregamos algunos usuarios hardcodeados
        usuarios.add(new Usuario("Juan Perez", "juan@example.com","Cliente"));
        usuarios.add(new Usuario("Maria Lopez", "maria@example.com","Cliente"));
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public void agregarUsuario(Usuario nuevoUsuario) {
        usuarios.add(nuevoUsuario);
        guardarUsuariosEnArchivo(); // Persistir cambios después de agregar
    }

    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        guardarUsuariosEnArchivo(); // Persistir cambios después de eliminar
    }

    public Usuario buscarUsuarioPorNombre(String nombre) {
        return usuarios.stream()
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> filtrarUsuariosPorRol(String rol) {
        return usuarios.stream()
                .filter(usuario -> usuario.getRol().equalsIgnoreCase(rol))
                .collect(Collectors.toList());
    }

    public void modificarUsuario(Usuario usuarioOriginal, Usuario usuarioModificado) {
        if (usuarioOriginal != null) {
            usuarioOriginal.setNombre(usuarioModificado.getNombre());
            usuarioOriginal.setEmail(usuarioModificado.getEmail());
            usuarioOriginal.setRol(usuarioModificado.getRol());
            guardarUsuariosEnArchivo(); // Persistir cambios después de modificar
        }
    }

    private void cargarUsuariosDesdeArchivo() {
        // Lógica para cargar usuarios desde un archivo o base de datos
        // Aquí podrías leer de un JSON o base de datos y llenar la lista
    }

    private void guardarUsuariosEnArchivo() {
        // Lógica para guardar usuarios en un archivo o base de datos
        // Aquí podrías escribir en un JSON o base de datos
    }

    public void modificarUsuario(int index, String nuevoNombre, String nuevoEmail,String nuevoRol) {
        if (index >= 0 && index < usuarios.size()) {
            Usuario usuario = usuarios.get(index);
            usuario.setNombre(nuevoNombre);
            usuario.setEmail(nuevoEmail);
            usuario.setRol(nuevoRol);
        }
    }
}
