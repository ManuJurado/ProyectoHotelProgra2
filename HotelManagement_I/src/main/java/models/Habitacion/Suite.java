package models.Habitacion;

public class Suite extends Habitacion {
    //Atributos
    private boolean balcon;
    private boolean comedor;

    //Constructor
    public Suite() {
    }

    //Getters y Setters
    public boolean isBalcon() {
        return balcon;
    }

    public void setBalcon(boolean balcon) {
        this.balcon = balcon;
    }

    public boolean isComedor() {
        return comedor;
    }

    public void setComedor(boolean comedor) {
        this.comedor = comedor;
    }

    //Sobreescritura de metodos

    @Override
    public String toString() {
        return "Suite{" +
                "balcon=" + balcon +
                ", comedor=" + comedor +
                '}' + super.toString();
    }
}
