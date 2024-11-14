package controllers.modificar;

import controllers.BaseController;
import controllers.GlobalData;
import enums.EstadoHabitacion;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Habitacion.*;
import services.GestionHabitaciones;

import java.util.ArrayList;
import java.util.List;

public class ModificarHabitacionController extends BaseController {

    // Campos comunes
    @FXML private ComboBox<String> comboBoxCama1, comboBoxCama2, comboBoxCama3, comboBoxCama4;
    @FXML private ComboBox<String> comboBoxEstadoHabitacion;

    @FXML private CheckBox checkBoxEstadoDisponible;

    // Campos exclusivos Presidencial
    @FXML private CheckBox mesaPoolCheckBox, jacuzziCheckBox, cineCheckBox, entretenimientoCheckBox, terrazaCheckBox, saunaCheckBox;

    // Campos exclusivos Suite
    @FXML private CheckBox comedorCheckBox, balconCheckBox;

    // Campos exclusivos Apartamento
    @FXML private CheckBox cocinaCheckBox;
    @FXML private ComboBox<String> comboBoxAmbientes;

    // Campo exclusivo para presidencial
    @FXML private TextField dimensionField;

    private Habitacion habitacion;

    @FXML
    public void initialize() {
        habitacion = GlobalData.getHabitacionSeleccionada();

        // Ocultar todos los campos al principio
        mesaPoolCheckBox.setVisible(false);
        jacuzziCheckBox.setVisible(false);
        cineCheckBox.setVisible(false);
        entretenimientoCheckBox.setVisible(false);
        terrazaCheckBox.setVisible(false);
        saunaCheckBox.setVisible(false);
        comedorCheckBox.setVisible(false);
        balconCheckBox.setVisible(false);
        cocinaCheckBox.setVisible(false);
        comboBoxAmbientes.setVisible(false);
        dimensionField.setVisible(false);

        comboBoxEstadoHabitacion.getItems().addAll("Disponible", "Alquilada", "En limpieza", "En reparación", "Desinfección");
        comboBoxEstadoHabitacion.setValue("Disponible");  // Estado inicial

        if (habitacion != null) {
            // Cargar el estado de la habitación en el ComboBox
            comboBoxEstadoHabitacion.setValue(String.valueOf(habitacion.getEstado()));

            // Cargar la disponibilidad del CheckBox
            checkBoxEstadoDisponible.setSelected(habitacion.isDisponible());

            // Llenar las camas seleccionadas (si hay camas predefinidas en la habitación)
            List<String> camas = habitacion.getCamas();
            for (int i = 0; i < camas.size(); i++) {
                // Dependiendo de la cantidad de camas, asignamos a cada ComboBox
                if (i < 4) { // Limitar hasta 4 camas, puedes ajustar según tu necesidad
                    ComboBox<String> comboBoxCama = obtenerComboBoxCama(i + 1);
                    assert comboBoxCama != null;
                    comboBoxCama.setValue(camas.get(i));
                }
            }

            // Comprobar el tipo de habitación y hacer ajustes correspondientes
            if (habitacion instanceof Individual) {
                // Ocultar los ComboBoxes de camas adicionales
                comboBoxCama2.setVisible(false);
                comboBoxCama3.setVisible(false);
                comboBoxCama4.setVisible(false);
            } else if (habitacion instanceof Presidencial) {
                // Mostrar y configurar los campos exclusivos para Presidencial
                setCamposVisibles(true, false, false, false);  // Solo mostrar campos de Presidencial
                // Inicializar los CheckBoxes exclusivos de Presidencial
                System.out.printf("afsadasdbasbfdbasbsafdbasdfbadf");
                Presidencial habitacionPresidencial = (Presidencial) habitacion;
                mesaPoolCheckBox.setSelected(habitacionPresidencial.tieneMesaPool());
                jacuzziCheckBox.setSelected(habitacionPresidencial.tieneJacuzzi());
                cineCheckBox.setSelected(habitacionPresidencial.tieneCine());
                entretenimientoCheckBox.setSelected(habitacionPresidencial.tieneEntretenimiento());
                terrazaCheckBox.setSelected(habitacionPresidencial.tieneTerraza());
                saunaCheckBox.setSelected(habitacionPresidencial.tieneSauna());
                dimensionField.setText(String.valueOf(habitacionPresidencial.getDimension()));
            } else if (habitacion instanceof Suite) {
                // Mostrar y configurar los campos exclusivos para Suite
                setCamposVisibles(false, true, false, false);  // Solo mostrar campos de Suite
                // Inicializar los CheckBoxes exclusivos de Suite
                Suite habitacionSuite = (Suite) habitacion;
                comedorCheckBox.setSelected(habitacionSuite.isComedor());
                balconCheckBox.setSelected(habitacionSuite.isBalcon());
            } else if (habitacion instanceof Apartamento) {
                // Mostrar y configurar los campos exclusivos para Apartamento
                setCamposVisibles(false, false, true, false);  // Solo mostrar campos de Apartamento
                // Inicializar los CheckBoxes exclusivos de Apartamento
                Apartamento habitacionApartamento = (Apartamento) habitacion;
                cocinaCheckBox.setSelected(habitacionApartamento.isCocina());
                comboBoxAmbientes.setValue(String.valueOf(habitacionApartamento.getAmbientes()));
            } else {
                // Si el tipo no coincide, puedes agregar un comportamiento por defecto
                setCamposVisibles(false, false, false, false);
            }
        }else {
            System.out.println("HABITACIONNULL");
        }

        // Inicializar ComboBox de camas con las opciones disponibles
        List<String> tiposCama = List.of("No seleccionado", "Simple", "Doble", "QueenSize", "KingSize");
        comboBoxCama1.getItems().addAll(tiposCama);
        comboBoxCama2.getItems().addAll(tiposCama);
        comboBoxCama3.getItems().addAll(tiposCama);
        comboBoxCama4.getItems().addAll(tiposCama);

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
    }

    private void setCamposVisibles(boolean presidencialVisible, boolean suiteVisible, boolean apartamentoVisible, boolean individualVisible) {

        // Mostrar solo los campos correspondientes al tipo de habitación
        if (presidencialVisible) {
            mesaPoolCheckBox.setVisible(true);
            jacuzziCheckBox.setVisible(true);
            cineCheckBox.setVisible(true);
            entretenimientoCheckBox.setVisible(true);
            terrazaCheckBox.setVisible(true);
            saunaCheckBox.setVisible(true);
            dimensionField.setVisible(true); // Solo para Presidencial
        }

        if (suiteVisible) {
            comedorCheckBox.setVisible(true);
            balconCheckBox.setVisible(true);
        }

        if (apartamentoVisible) {
            cocinaCheckBox.setVisible(true);
            comboBoxAmbientes.setVisible(true);
        }

        if (individualVisible) {
            comboBoxCama2.setVisible(false);
            comboBoxCama3.setVisible(false);
            comboBoxCama4.setVisible(false);
        }
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

        // Obtener el estado seleccionado en el ComboBox
        String estadoSeleccionado = comboBoxEstadoHabitacion.getValue();

        // Validar que se haya seleccionado un estado
        if (estadoSeleccionado == null || estadoSeleccionado.isEmpty()) {
            errores.add("Debe seleccionar un estado para la habitación.");
        }

        // Validar si la habitación está disponible o no
        boolean disponible = checkBoxEstadoDisponible.isSelected();

        // Implementar validaciones adicionales o guardar la habitación
        if (errores.isEmpty()) {
            // Aquí deberías guardar los datos de la habitación modificada
            // Por ejemplo, actualizar el objeto habitacion con los nuevos datos.
            habitacion.setCamas(camasSeleccionadas);
            habitacion.setEstado(EstadoHabitacion.valueOf(estadoSeleccionado));
            habitacion.setDisponible(disponible);

            if (habitacion instanceof Presidencial) {
                Presidencial habitacionPresidencial = (Presidencial) habitacion;
                habitacionPresidencial.setMesaPool(mesaPoolCheckBox.isSelected());
                habitacionPresidencial.setJacuzzi(jacuzziCheckBox.isSelected());
                habitacionPresidencial.setCine(cineCheckBox.isSelected());
                habitacionPresidencial.setEntretenimiento(entretenimientoCheckBox.isSelected());
                habitacionPresidencial.setTerraza(terrazaCheckBox.isSelected());
                habitacionPresidencial.setSauna(saunaCheckBox.isSelected());
                habitacionPresidencial.setDimension(Double.parseDouble(dimensionField.getText()));
            } else if (habitacion instanceof Suite) {
                Suite habitacionSuite = (Suite) habitacion;
                habitacionSuite.setComedor(comedorCheckBox.isSelected());
                habitacionSuite.setBalcon(balconCheckBox.isSelected());
            } else if (habitacion instanceof Apartamento) {
                Apartamento habitacionApartamento = (Apartamento) habitacion;
                habitacionApartamento.setCocina(cocinaCheckBox.isSelected());
                habitacionApartamento.setAmbientes(Integer.parseInt(comboBoxAmbientes.getValue()));
            }
            // Aquí va la lógica para guardar la habitación modificada (por ejemplo, en un archivo o base de datos)
            closeStage();
        } else {
            mostrarErrores(errores);
        }
    }

    private int obtenerValorCama(String tipoCama) {
        switch (tipoCama) {
            case "Simple": return 1;
            case "Doble": return 2;
            case "QueenSize": return 2;
            case "KingSize": return 3;
            default: return 0;
        }
    }

    private void mostrarErrores(List<String> errores) {
        // Función para mostrar errores
        StringBuilder mensajeErrores = new StringBuilder("Errores encontrados:");
        for (String error : errores) {
            mensajeErrores.append("\n").append(error);
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, mensajeErrores.toString(), ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) comboBoxCama1.getScene().getWindow();
        stage.close();
    }

    private ComboBox<String> obtenerComboBoxCama(int numero) {
        switch (numero) {
            case 1: return comboBoxCama1;
            case 2: return comboBoxCama2;
            case 3: return comboBoxCama3;
            case 4: return comboBoxCama4;
            default: return null;
        }
    }
}
