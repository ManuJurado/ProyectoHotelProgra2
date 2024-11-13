package Clases;
import Clases.Usuario.Usuario;

import java.util.List;

public class listaUsuarios< T extends Usuario>{

    private List<T> listaUsuariso;

    public listaUsuarios(List<T> listaUsuariso) {
        this.listaUsuariso = listaUsuariso;
    }

    public listaUsuarios() {

    }

    public List<T> getListaUsuariso() {
        return listaUsuariso;
    }

    public void setListaUsuariso(List<T> listaUsuariso) {
        this.listaUsuariso = listaUsuariso;
    }

    @Override
    public String toString() {
        return "listaUsuarios{" +
                "listaUsuariso=" + listaUsuariso +
                '}';
    }
}
