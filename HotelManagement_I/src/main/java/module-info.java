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
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens controllers to javafx.fxml; // Permite que JavaFX acceda a los controladores en el paquete
    exports controllers; // Exporta el paquete de controladores
    exports hotel_management_ui;
    exports controllers.crear;
    opens controllers.crear to javafx.fxml;
    exports controllers.modificar;
    opens controllers.modificar to javafx.fxml;
    exports controllers.gestionar;
    opens controllers.gestionar to javafx.fxml;
    exports controllers.details;
    opens controllers.details to javafx.fxml;
    opens models.Usuarios to javafx.base;  // Esto permite el acceso a la clase Cliente
    // Abre el paquete models.Habitacion a javafx.base para el acceso reflexivo
    opens models.Habitacion to javafx.base;
    // Si deseas que el paquete sea accesible fuera del módulo:
    exports models.Habitacion;
    exports models;
}
