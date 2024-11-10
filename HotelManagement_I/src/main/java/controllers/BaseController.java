package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class BaseController {

    private Stage stageAnterior;  // Variable para almacenar el Stage anterior

    // Metodo para establecer el stage anterior
    public void setStageAnterior(Stage stage) {
        this.stageAnterior = stage;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public <T> T cambiarEscena(String fxmlFileName, String title, Node someNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            System.out.println("Cargando FXML: " + fxmlFileName); // Imprime la carga
            System.out.println("Ruta FXML: " + loader.getLocation());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) someNode.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            return loader.getController();
        } catch (IOException e) {
            // Captura la excepción y muestra un error detallado
            System.err.println("Error al cargar el archivo FXML: " + fxmlFileName);
            e.printStackTrace(); // Imprime el stack trace completo para depuración
            return null;
        }
    }

    // Este metodo se llamará para volver a la ventana anterior
    protected void volver(Node source) {
        // Obtener el Stage (ventana actual) y cerrarlo
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();  // Cierra la ventana actual

        // Aquí puedes agregar lógica para reabrir la ventana anterior (por ejemplo, el menú principal)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menuAdmin.fxml")); // Asegúrate de tener el path correcto
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Administrador");
            stage.show(); // Muestra la ventana del menú administrador

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
