package Enums;

public enum EstadoHabitacion {
    OCUPADA("Ocupada"),
    LIMPIEZA("Limpieza"),
    REPARACION("Reparacion"),
    DESINFECCION("Desinfeccion");

    private final String estado;

    // Constructor del enum que asigna la descripción a cada constante
    EstadoHabitacion(String estado) {
        this.estado = estado;
    }

    // Método getter para obtener la descripción del enum
    public String getDescripcion() {
        return estado;
    }
}