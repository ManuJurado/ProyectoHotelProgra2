package controllers.modificar;

import controllers.BaseController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Habitacion.Habitacion;
import services.GestionHabitaciones;

import java.util.ArrayList;
import java.util.List;

public class ModificarHabitacionController extends BaseController {

    // Campos comunes
    @FXML private ComboBox<String> comboBoxCama1;
    @FXML private ComboBox<String> comboBoxCama2;
    @FXML private ComboBox<String> comboBoxCama3;
    @FXML private ComboBox<String> comboBoxCama4;

    // Campos exclusivos Presidencial
    @FXML private CheckBox mesaPoolCheckBox;
    @FXML private CheckBox jacuzziCheckBox;
    @FXML private CheckBox cineCheckBox;
    @FXML private CheckBox entretenimientoCheckBox;
    @FXML private CheckBox terrazaCheckBox;
    @FXML private CheckBox saunaCheckBox;

    // Campos exclusivos Suite
    @FXML private CheckBox comedorCheckBox;
    @FXML private CheckBox balconCheckBox;

    // Campos exclusivos Apartamento
    @FXML private CheckBox cocinaCheckBox;
    @FXML private ComboBox<String> comboBoxAmbientes;

    // Campo exclusivo para dimensión
    @FXML private TextField dimensionField;

    private Habitacion habitacion;

    @FXML
    public void initialize() {
        // Inicializar ComboBox de camas con las opciones disponibles
        List<String> tiposCama = List.of("No seleccionado", "Simple", "Doble", "QueenSize", "KingSize");
        comboBoxCama1.getItems().addAll(tiposCama);
        comboBoxCama2.getItems().addAll(tiposCama);
        comboBoxCama3.getItems().addAll(tiposCama);
        comboBoxCama4.getItems().addAll(tiposCama);

        comboBoxCama1.setValue("No seleccionado");
        comboBoxCama2.setValue("No seleccionado");
        comboBoxCama3.setValue("No seleccionado");
        comboBoxCama4.setValue("No seleccionado");

        // Deshabilitar los ComboBox de camas adicionales
        comboBoxCama2.setDisable(true);
        comboBoxCama3.setDisable(true);
        comboBoxCama4.setDisable(true);

        // Listener para el ComboBox de la Cama 1
        comboBoxCama1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama2.setDisable(true);
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama2.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 2
        comboBoxCama2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama3.setDisable(true);
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama3.setDisable(false);
            }
        });

        // Listener para el ComboBox de la Cama 3
        comboBoxCama3.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("No seleccionado".equals(newValue)) {
                comboBoxCama4.setDisable(true);
            } else {
                comboBoxCama4.setDisable(false);
            }
        });

        // Inicializar todos los controles como no visibles
        setVisibilidad(false);
    }

    public void sethabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
        ajustarVisibilidad();
    }

    private void ajustarVisibilidad() {
        // Establecer todos los controles en false por defecto
        setVisibilidad(false);

        // Campos comunes (siempre visibles)
        comboBoxCama1.setVisible(true);
        comboBoxCama2.setVisible(true);
        comboBoxCama3.setVisible(true);
        comboBoxCama4.setVisible(true);

        // Lógica basada en el tipo de habitación
        switch (habitacion.getTipo()) {
            case "Presidencial":
                mesaPoolCheckBox.setVisible(true);
                jacuzziCheckBox.setVisible(true);
                cineCheckBox.setVisible(true);
                entretenimientoCheckBox.setVisible(true);
                terrazaCheckBox.setVisible(true);
                saunaCheckBox.setVisible(true);
                dimensionField.setVisible(true); // Solo para Presidencial
                break;

            case "Suite":
                comedorCheckBox.setVisible(true);
                balconCheckBox.setVisible(true);
                break;

            case "Apartamento":
                cocinaCheckBox.setVisible(true);
                comboBoxAmbientes.setVisible(true);
                break;
        }
    }

    private void setVisibilidad(boolean visible) {
        // Función auxiliar para ocultar todos los elementos
        mesaPoolCheckBox.setVisible(visible);
        jacuzziCheckBox.setVisible(visible);
        cineCheckBox.setVisible(visible);
        entretenimientoCheckBox.setVisible(visible);
        terrazaCheckBox.setVisible(visible);
        saunaCheckBox.setVisible(visible);
        comedorCheckBox.setVisible(visible);
        balconCheckBox.setVisible(visible);
        cocinaCheckBox.setVisible(visible);
        comboBoxAmbientes.setVisible(visible);
        dimensionField.setVisible(visible);
    }

    @FXML
    private void guardarCambios() {
        List<String> errores = new ArrayList<>();
        int capacidad = 0;

        // Crear una lista para las camas seleccionadas
        List<String> camasSeleccionadas = new ArrayList<>();

        // Validar las camas seleccionadas
        for (ComboBox<String> comboBox : List.of(comboBoxCama1, comboBoxCama2, comboBoxCama3, comboBoxCama4)) {
            String camaSeleccionada = comboBox.getValue();
            if (camaSeleccionada != null && !camaSeleccionada.equals("No seleccionado")) {
                camasSeleccionadas.add(camaSeleccionada);
                capacidad += obtenerValorCama(camaSeleccionada);
            }
        }

        if (capacidad > 4) {
            errores.add("Capacidad máxima excedida, reducir cantidad o tamaño de camas.");
        }

        if (camasSeleccionadas.isEmpty()) {
            errores.add("Debe seleccionar al menos una cama.");
        }

        // Verificar tipo de habitación y realizar validaciones específicas
        try {
            // Validación para habitaciones Presidenciales
            if (habitacion.getTipo().equals("Presidencial")) {
                double dimensionHabitacion = 0;
                try {
                    dimensionHabitacion = Double.parseDouble(dimensionField.getText());
                } catch (NumberFormatException e) {
                    errores.add("La dimensión debe ser un número válido.");
                }

                if (capacidad > 4) {
                    errores.add("La capacidad máxima para una habitación presidencial es 4.");
                }

                if (!errores.isEmpty()) {
                    showAlert(String.join("\n", errores));
                } else {
                    // Actualizar la habitación presidencial
                    GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                    gestionHabitacion.modificarHabitacionPresidencial(habitacion.getNumero(), capacidad, camasSeleccionadas, true, "Disponible", obtenerAdicionalesSeleccionados(), dimensionHabitacion);
                    mostrarMensajeTemporal("Habitación Presidencial modificada correctamente");
                    cerrarVentana();
                }

                // Validación para habitaciones Suite
            } else if (habitacion.getTipo().equals("Suite")) {
                if (capacidad > 4) {
                    errores.add("La capacidad máxima para una habitación suite es 4.");
                }

                if (!errores.isEmpty()) {
                    showAlert(String.join("\n", errores));
                } else {
                    // Actualizar la habitación suite
                    GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                    gestionHabitacion.modificarHabitacionSuite(habitacion.getNumero(), capacidad, camasSeleccionadas, true, "Disponible", balconCheckBox.isSelected(), comedorCheckBox.isSelected());
                    mostrarMensajeTemporal("Habitación Suite modificada correctamente");
                    cerrarVentana();
                }

                // Validación para habitaciones Apartamento
            } else if (habitacion.getTipo().equals("Apartamento")) {
                if (capacidad > 4) {
                    errores.add("La capacidad máxima para una habitación apartamento es 4.");
                }

                if (!errores.isEmpty()) {
                    showAlert(String.join("\n", errores));
                } else {
                    // Actualizar la habitación apartamento
                    GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                    gestionHabitacion.modificarHabitacionApartamento(habitacion.getNumero(), capacidad, camasSeleccionadas, true, "Disponible", obtenerAmbientes(), cocinaCheckBox.isSelected());
                    mostrarMensajeTemporal("Habitación Apartamento modificada correctamente");
                    cerrarVentana();
                }

                // Validación para habitaciones Dobles
            } else if (habitacion.getTipo().equals("Doble")) {
                if (capacidad > 2) {
                    errores.add("La capacidad máxima para una habitación doble es 2.");
                }

                // Validar camas seleccionadas para habitaciones dobles
                if (camasSeleccionadas.size() > 2) {
                    errores.add("Solo se pueden seleccionar hasta 2 camas.");
                }

                if (!errores.isEmpty()) {
                    showAlert(String.join("\n", errores));
                } else {
                    // Actualizar la habitación doble
                    GestionHabitaciones gestionHabitacion = GestionHabitaciones.getInstancia("HotelManagement_I/habitaciones.json");
                    gestionHabitacion.modificarHabitacionDoble(habitacion.getNumero(), capacidad, camasSeleccionadas, true, "Disponible");
                    mostrarMensajeTemporal("Habitación Doble modificada correctamente");
                    cerrarVentana();
                }

            } else {
                // Si el tipo de habitación no es válido
                errores.add("Tipo de habitación no válido.");
                showAlert(String.join("\n", errores));
            }

        } catch (Exception e) {
            // Manejo de excepciones acumuladas
            errores.add("Ocurrió un error al guardar la habitación: " + e.getMessage());
            showAlert(String.join("\n", errores));
        }

    }

    // Métodos auxiliares para obtener los valores de los campos
    private List<String> obtenerAdicionalesSeleccionados() {
        List<String> adicionalesSeleccionados = new ArrayList<>();
        if (mesaPoolCheckBox.isSelected()) adicionalesSeleccionados.add("Mesa de pool");
        if (jacuzziCheckBox.isSelected()) adicionalesSeleccionados.add("Jacuzzi");
        if (cineCheckBox.isSelected()) adicionalesSeleccionados.add("Cine");
        if (entretenimientoCheckBox.isSelected()) adicionalesSeleccionados.add("Servicios de entretenimiento");
        if (terrazaCheckBox.isSelected()) adicionalesSeleccionados.add("Terraza");
        if (saunaCheckBox.isSelected()) adicionalesSeleccionados.add("Sauna");
        return adicionalesSeleccionados;
    }

    private int obtenerValorCama(String tipoCama) {
        switch (tipoCama) {
            case "Simple":
                return 1;
            case "Doble":
            case "QueenSize":
            case "KingSize":
                return 2;
            default:
                return 0;
        }
    }

    private int obtenerAmbientes() {
        try {
            return Integer.parseInt(comboBoxAmbientes.getValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText("Se encontraron los siguientes errores:");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensajeTemporal(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cargando");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> alert.close()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    public void cerrarVentana() {
        Stage stage = (Stage) comboBoxCama1.getScene().getWindow();
        stage.close();
    }

}
