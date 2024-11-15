package controllers.gestionar;

import controllers.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Pasajero;
import java.util.ArrayList;
import java.util.List;

public class CheckInController extends BaseController {

    @FXML
    private VBox vdniFields;
    @FXML
    private Button confirmarButton;

    public int cantidadPasajeros;

    private final List<TextField> dniFields = new ArrayList<>();

    // Inicializa los campos de DNI en función de la cantidad de pasajeros
    public void inicializarCampos(int cantidadPasajeros) {
        vdniFields.getChildren().clear();  // Limpia los campos previos si los hubiera
        dniFields.clear();  // Limpia la lista de campos de texto

        for (int i = 0; i < cantidadPasajeros; i++) {
            TextField dniField = new TextField();
            dniField.setPromptText("DNI del pasajero " + (i + 1));
            dniFields.add(dniField);
            vdniFields.getChildren().add(dniField);
        }
    }

    public void setCantidadPasajeros(int cantidadPasajeros){
        this.cantidadPasajeros = cantidadPasajeros;
    }

    // Mrtodo para obtener la lista de pasajeros
    public List<Pasajero> obtenerPasajerosCheckIn() {
        List<Pasajero> pasajeros = new ArrayList<>();
        for (TextField dniField : dniFields) {
            String dni = dniField.getText().trim();
            if (!dni.isEmpty()) {
                pasajeros.add(new Pasajero(null, null, dni)); // Solo se requiere DNI
            }
        }
        return pasajeros;
    }

    // Acción para el botón Confirmar
    @FXML
    private void confirmacion() {
        vdniFields.getScene().getWindow().hide();  // Cierra la ventana de check-in
    }
}
