package hotel_management_ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage; // Guardar la referencia al escenario
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);

        // Crear un Pane para contener el fondo animado
        Pane root = new Pane();
        root.setPrefSize(900, 700);

        // Ahora que tenemos el fondo animado, cargamos el video de introducción
        playIntroVideo(root);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void playIntroVideo(Pane root) {
        String videoPath = new File("HotelManagement_I/video.mp4").toURI().toString();
        javafx.scene.media.Media media = new javafx.scene.media.Media(videoPath);
        javafx.scene.media.MediaPlayer mediaPlayer = new javafx.scene.media.MediaPlayer(media);
        javafx.scene.media.MediaView mediaView = new javafx.scene.media.MediaView(mediaPlayer);

        mediaView.setFitWidth(900);
        mediaView.setFitHeight(700);
        mediaView.setPreserveRatio(false);

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose();
            showLoginScreen(); // Mostrar la pantalla de login al finalizar el video
        });

        // Agregar el video al fondo animado
        root.getChildren().add(mediaView);
        mediaPlayer.play();
    }

    private void showLoginScreen() {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/menu/login.fxml")));
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());

            primaryStage.setWidth(900);
            primaryStage.setHeight(700);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
