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

    protected void volver(String fxmlFileName, String title, Node someNode, Runnable postLoadAction) {
        cambiarEscena(fxmlFileName, title, someNode);
        if (postLoadAction != null) {
            postLoadAction.run(); // Ejecuta la acción después de cargar la nueva escena
        }
    }


}
