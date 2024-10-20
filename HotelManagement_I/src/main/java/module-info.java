module tu.paquete {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.json;

    opens controllers to javafx.fxml; // Permite que JavaFX acceda a los controladores en el paquete
    opens models to javafx.base; // Permite que JavaFX acceda de forma reflexiva al paquete models (donde se encuentra Usuario)
    exports controllers; // Exporta el paquete de controladores
    exports hotel_management_ui;
    exports controllers.crear;
    opens controllers.crear to javafx.fxml;
    exports controllers.modificar;
    opens controllers.modificar to javafx.fxml;
    exports controllers.gestionar;
    opens controllers.gestionar to javafx.fxml;
}
