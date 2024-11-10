package Clases.Usuario;

public class Administrador extends Usuario {

    private String nivelAcceso;

    public Administrador(String nombre, String apellido, String dni,String email, String password, String nivelAcceso) {
        super(nombre, apellido, dni, email, password);
        this.nivelAcceso = nivelAcceso;
    }

    public Administrador() {
        super();
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "nombre=" + getNombre() + '\'' +
                "apellido=" + getApellido() + '\'' +
                "dni=" +getDni()+ '\'' +
                "email=" + getEmail() +  '\'' +
                "nivelAcceso='" + nivelAcceso + '\'' +
                '}';
    }
}

