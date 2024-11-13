package controllers;

import controllers.gestionar.GestionarUsuariosController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.event.ActionEvent;
import java.io.IOException;

public abstract class BaseController {

    private Scene previousScene; // Aquí guardamos la Scene actual, no el Stage

    // Metodo para establecer la Scene anterior
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    private BaseController previousController;

    public void setPreviousController(BaseController previousController) {
        this.previousController = previousController;
    }

    protected void mostrarAlerta(String mensaje) {
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

    public void cambiarEscenaConSceneAnterior(String fxml, String title, Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();
        Scene currentScene = currentStage.getScene();  // Guardamos la Scene actual

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            Parent root = loader.load();

            // Configurar la nueva escena y guardar el controlador en userData
            Scene newScene = new Scene(root);
            newScene.setUserData(loader.getController());  // Guarda el controlador en userData

            // Pasar la escena anterior al nuevo controlador
            BaseController controller = loader.getController();
            controller.setPreviousScene(currentScene);

            // Cambiar la escena al nuevo contenido
            currentStage.setScene(newScene);
            currentStage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para limitar la cantidad de caracteres permitidos en un TextField
    public void setTextFieldLimit(TextField textField, int maxLength) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > maxLength) {
                return null;  // No permite el cambio si el texto supera el límite
            }
            return change;
        });
        textField.setTextFormatter(formatter);
    }


    public void volverAEscenaAnterior(ActionEvent event, Scene previousScene) {
        if (previousScene != null) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Asegurarnos de que el controlador esté correctamente cargado
            if (currentStage != null) {
                // Establecer la escena de vuelta
                currentStage.setScene(previousScene);

                // Obtener el controlador de la escena anterior y actualizar la lista
                BaseController controller = (BaseController) previousScene.getUserData();  // Obtener el controlador

            } else {
                System.out.println("Error: No se pudo obtener el Stage actual.");
            }
        } else {
            System.out.println("Error: previousScene es null.");
        }
    }



}
