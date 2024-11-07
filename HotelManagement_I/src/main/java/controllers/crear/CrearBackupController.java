package controllers.crear;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class CrearBackupController {

    @FXML
    public void volverAlMenuAdmin() throws IOException {
        cargarNuevaVentana("/views/administradorMenu.fxml", "Menú Administrador");
    }

    private void cargarNuevaVentana(String fxmlPath, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle(titulo);
    }
}
