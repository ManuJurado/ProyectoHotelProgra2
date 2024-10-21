package services;

import models.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class GestionarUsuarios {
    private List<Usuario> usuarios; // Lista de usuarios

    public GestionarUsuarios() {
        usuarios = new ArrayList<>();
        cargarUsuariosHardcodeados(); // Cargar usuarios hardcodeados
    }

    private void cargarUsuariosHardcodeados() {
        // Ejemplo de carga de usuarios, puedes modificar esto según necesites
        usuarios.add(new Usuario("Juan", "juan@example.com", "administrador"));
        usuarios.add(new Usuario("Maria", "maria@example.com", "cliente"));
        // Agrega más usuarios según lo necesites
    }

    // Método para agregar un nuevo usuario
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        guardarUsuarios(); // Guarda los cambios en la lista
    }

    // Método para habilitar o inhabilitar un usuario
    public void habilitarInhabilitarUsuario(Usuario usuario) {
        String nuevoEstado = usuario.getHabilitacion().equals("Habilitado") ? "Inhabilitado" : "Habilitado";
        usuario.setHabilitacion(nuevoEstado);
        guardarUsuarios(); // Guarda los cambios en la lista
    }

    // Método para modificar un usuario existente
    public void modificarUsuario(Usuario usuario, String nuevoNombre, String nuevoEmail, String nuevoRol) {
        usuario.setNombre(nuevoNombre);
        usuario.setEmail(nuevoEmail);
        usuario.setRol(nuevoRol);
        guardarUsuarios(); // Guarda los cambios en la lista
    }

    // Método para eliminar un usuario
    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        guardarUsuarios(); // Guarda los cambios en la lista
    }

    // Método para obtener la lista de usuarios
    public ObservableList<Usuario> obtenerUsuarios() {
        return FXCollections.observableArrayList(usuarios);
    }

    // Método para guardar la lista de usuarios (puede tener lógica adicional si es necesario)
    private void guardarUsuarios() {
        // Aquí puedes implementar la lógica para guardar cambios en la lista si es necesario
        // En este caso, simplemente actualiza la lista en memoria
        System.out.println("Lista de usuarios actualizada. Total de usuarios: " + usuarios.size());
    }

    public List<Usuario> getUsuarios(){
        return usuarios;
    }
}
