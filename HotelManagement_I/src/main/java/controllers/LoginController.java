package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class LoginController extends BaseController {

    @FXML
    private void loginAdmin(ActionEvent event) {
        cambiarEscena("/views/menu/menuAdministrador.fxml", "Menú Administrador", (Node) event.getSource());
    }

    @FXML
    private void salir() {
        System.exit(0);
    }
}
