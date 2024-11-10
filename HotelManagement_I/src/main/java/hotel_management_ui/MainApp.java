package hotel_management_ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import java.io.File;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage; // Guardar la referencia al escenario
        playIntroVideo(); // Reproducir el video al iniciar
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Metodo para reproducir el video
    private void playIntroVideo() {
        String videoPath = new File("C:/Users/emili/OneDrive/Documentos/Tecnicatura Progra/2024/Programacion 2 (Java)/TP_FINAL_RAMA_HABITACIONES/ProyectoHotelProgra2/HotelManagement_I/video.mp4").toURI().toString();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false);

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose();
            showLoginScreen(); // Mostrar la pantalla de login al finalizar el video
        });

        Scene videoScene = new Scene(new VBox(mediaView), 800, 600);
        primaryStage.setScene(videoScene);
        primaryStage.show();
        mediaPlayer.play();
    }

    // Metodo para mostrar la pantalla de inicio (login)
    private void showLoginScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/menu/login.fxml")); // Carga el archivo FXML de inicio
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Tu Aplicación");
            primaryStage.setResizable(false); // Esto impide que la ventana se pueda redimensionar
            primaryStage.setWidth(800); // Establece el ancho de la ventana
            primaryStage.setHeight(600); // Establece la altura de la ventana
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de errores
        }
    }
}
