package models.Habitacion;

public class Apartamento extends Habitacion {
    //Atributos
    private int ambientes;
    private boolean cocina;

    //Constructor
    public Apartamento() {
    }

    //Getters y Setters
    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public boolean isCocina() {
        return cocina;
    }

    public void setCocina(boolean cocina) {
        this.cocina = cocina;
    }

    //Sobreescritura de metodos
    @Override
    public String toString() {
        return "Apartamento{" +
                "ambientes=" + ambientes +
                ", cocina=" + cocina +
                '}' + super.toString();
    }
}
