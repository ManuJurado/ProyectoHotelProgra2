package Clases.Habitaciones;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Habitacion {

    private String tipo;
    private int numero;
    private int capacidad;
    protected  List<String> camas;
    private boolean disponible;
    private String estado;
    private String detalleEstado;


    public Habitacion(String tipo, int numero, int capacidad, List<String> camas, boolean disponible, String estado, String detalleEstado) {
        this.tipo = tipo;
        this.numero = numero;
        this.capacidad = capacidad;
        this.camas = camas;
        this.disponible = disponible;
        this.estado = estado;
        this.detalleEstado = detalleEstado;
    }

    public Habitacion() {

    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<String> getCamas() {
        return camas;
    }

    public void setCamas(List<String> camas) {
        this.camas = camas;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalleEstado() {
        return detalleEstado;
    }

    public void setDetalleEstado(String detalleEstado) {
        this.detalleEstado = detalleEstado;
    }


    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tipo", tipo);
            jsonObject.put("numero", numero);
            jsonObject.put("capacidad", capacidad);
            jsonObject.put("camas", new JSONArray(camas));
            jsonObject.put("disponible", disponible);
            jsonObject.put("estado", estado);
            jsonObject.put("detalleEstado", detalleEstado);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }


    @Override
    public String toString() {
        return "Habitacion{" +
                "tipo='" + tipo + '\'' +
                ", numero=" + numero +
                ", capacidad=" + capacidad +
                ", camas=" + camas +
                ", disponible=" + disponible +
                ", estado='" + estado + '\'' +
                ", detalleEstado='" + detalleEstado + '\'' +
                '}';
    }
}