package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class BaseController {
    public <T> T cambiarEscena(String fxmlFileName, String title, Node someNode) {
        try {
            System.out.println("Cargando FXML: " + fxmlFileName); // Imprime la carga
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) someNode.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
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
