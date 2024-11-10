package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.event.ActionEvent;
import java.io.IOException;

public class BaseController {

    private Scene previousScene; // Aquí guardamos la Scene actual, no el Stage

    // Metodo para establecer la Scene anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
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

    // Metodo genérico para cambiar de escena
    public void cambiarEscenaConSceneAnterior(String fxml, String title, Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();
        Scene currentScene = currentStage.getScene();  // Guardamos la Scene actual

        // Cargar la nueva escena desde el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            Parent root = loader.load();

            // Obtener el controlador del nuevo FXML
            BaseController controller = loader.getController();

            // Pasar la escena anterior al nuevo controlador
            controller.setPreviousScene(currentScene);

            // Cambiar la escena al nuevo contenido
            currentStage.setScene(new Scene(root));
            currentStage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo genérico para volver a la escena anterior, pasando 'previousScene' como parámetro
    public void volverAEscenaAnterior(ActionEvent event, Scene previousScene) {
        if (previousScene != null) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Asegúrate de que currentStage esté apuntando al Stage correcto (la ventana principal)
            if (currentStage != null) {
                currentStage.setScene(previousScene);  // Establecer la Scene previamente guardada
            } else {
                System.out.println("Error: No se pudo obtener el Stage actual.");
            }
        } else {
            System.out.println("Error: previousScene es null.");
        }
    }

}
