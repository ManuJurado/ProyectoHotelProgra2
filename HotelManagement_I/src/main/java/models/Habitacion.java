package models;

public class Habitacion {
    private String tipoHabitacion; // Tipo de habitación (ej. "Simple", "Doble", "Suite")
    private String estado; // Estado de la habitación (ej. "Disponible", "No disponible", etc.)
    private int metrosCuadrados; // Metros cuadrados de la habitación
    private int cantidadCamas; // Cantidad de camas en la habitación
    private int capacidad; // Capacidad de la habitación basada en la cantidad de camas

    // Constructor
    public Habitacion(String tipoHabitacion, String estado, int metrosCuadrados, int cantidadCamas) {
        this.tipoHabitacion = tipoHabitacion;
        this.estado = estado;
        this.metrosCuadrados = metrosCuadrados;
        this.cantidadCamas = cantidadCamas;
        this.capacidad = cantidadCamas; // Asumiendo que cada cama tiene capacidad para 1 persona
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(int metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public int getCantidadCamas() {
        return cantidadCamas;
    }

    public void setCantidadCamas(int cantidadCamas) {
        this.cantidadCamas = cantidadCamas;
        this.capacidad = cantidadCamas; // Actualizar la capacidad si se cambia la cantidad de camas
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad; // Establecer capacidad directamente
    }

    public int getCapacidad() {
        return capacidad; // Establecer capacidad directamente
    }

    // Método para modificar la habitación
    public void modificarHabitacion(String nuevoTipo, String nuevoEstado, int nuevosMetrosCuadrados, int nuevaCantidadCamas) {
        this.tipoHabitacion = nuevoTipo;
        this.estado = nuevoEstado;
        this.metrosCuadrados = nuevosMetrosCuadrados;
        setCantidadCamas(nuevaCantidadCamas); // Utiliza el setter para actualizar la capacidad
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                ", tipoHabitacion='" + tipoHabitacion + '\'' +
                ", estado='" + estado + '\'' +
                ", metrosCuadrados=" + metrosCuadrados +
                ", cantidadCamas=" + cantidadCamas +
                ", capacidad=" + capacidad +
                '}';
    }
}
