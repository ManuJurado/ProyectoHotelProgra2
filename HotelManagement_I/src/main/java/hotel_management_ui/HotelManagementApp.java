package hotel_management_ui;

import reservas.Reserva;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class HotelManagementApp extends Application {
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Reserva reserva; // Instancia de la clase Reserva

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hotel Management System");

        // Reproducir el video
        playIntroVideo();
    }

    // Método para reproducir el video
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
            showMainMenu(); // Mostrar el menú principal al finalizar el video
        });

        Scene videoScene = new Scene(new VBox(mediaView), 800, 600);
        primaryStage.setScene(videoScene);
        primaryStage.show();
        mediaPlayer.play();
    }

    // Método para mostrar el menú principal
    private void showMainMenu() {
        VBox mainMenuLayout = createMainMenu();
        mainMenuScene = new Scene(mainMenuLayout, 800, 600);
        primaryStage.setScene(mainMenuScene);
    }

    // Crear el menú principal
    private VBox createMainMenu() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Button btnReserve = new Button("Hacer Reserva");
        Button btnCheckIn = new Button("Hacer Check-in");
        Button btnCheckOut = new Button("Hacer Check-out");
        Button btnViewOccupied = new Button("Ver Habitaciones Ocupadas");
        Button btnViewAvailable = new Button("Ver Habitaciones Disponibles");
        Button btnViewUnavailable = new Button("Ver Habitaciones No Disponibles");
        Button btnViewHistory = new Button("Ver Historial de Pasajeros");
        Button btnViewReservations = new Button("Ver Reservas");
        Button btnExit = new Button("Salir");

        btnReserve.setOnAction(e -> showReservationForm());
        btnCheckIn.setOnAction(e -> showCheckInForm());
        btnCheckOut.setOnAction(e -> showCheckOutForm());
        btnViewOccupied.setOnAction(e -> showOccupiedRooms());
        btnViewAvailable.setOnAction(e -> showAvailableRooms());
        btnViewUnavailable.setOnAction(e -> showUnavailableRooms());
        btnViewHistory.setOnAction(e -> showHistory());
        btnViewReservations.setOnAction(e -> showReservations()); // Agregar acción para mostrar reservas
        btnExit.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(btnReserve, btnCheckIn, btnCheckOut, btnViewOccupied,
                btnViewAvailable, btnViewUnavailable, btnViewHistory, btnViewReservations, btnExit);

        return layout;
    }

    // Metodo para mostrar el formulario de reserva
    private void showReservationForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del Cliente");
        TextField dateField = new TextField();
        dateField.setPromptText("Fecha de Reserva (YYYY-MM-DD)");

        Button btnSubmit = new Button("Confirmar Reserva");
        btnSubmit.setOnAction(e -> {
            String nombreCliente = nameField.getText();
            String fechaReserva = dateField.getText();
            Reserva.crearReserva(nombreCliente, fechaReserva); // Usar el metodo estático para crear la reserva
            showReservationConfirmation(nombreCliente, fechaReserva);
        });

        layout.getChildren().addAll(new Label("Formulario de Reserva"), nameField, dateField, btnSubmit, createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    // Método para mostrar la confirmación de la reserva
    private void showReservationConfirmation(String nombreCliente, String fechaReserva) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Reserva creada: " + nombreCliente + " para la fecha " + fechaReserva), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    // Método para mostrar todas las reservas
    private void showReservations() {
        List<Reserva> reservas = Reserva.obtenerReservas(); // Obtener la lista de reservas
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        StringBuilder reservasText = new StringBuilder("Reservas:\n");
        for (Reserva r : reservas) {
            reservasText.append("Cliente: ").append(r.getNombreCliente())
                    .append(", Fecha: ").append(r.getFechaReserva()).append("\n");
        }

        layout.getChildren().addAll(new Label(reservasText.toString()), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    // Otros métodos de menú
    private void showCheckInForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría la opción de check-in."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    private void showCheckOutForm() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría la opción de check-out."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    private void showOccupiedRooms() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría la lista de habitaciones ocupadas."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    private void showAvailableRooms() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría la lista de habitaciones disponibles."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    private void showUnavailableRooms() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría la lista de habitaciones no disponibles."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    private void showHistory() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Aquí iría el historial de pasajeros."), createBackButton());
        primaryStage.setScene(new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight()));
    }

    // Método para crear el botón "Volver"
    private Button createBackButton() {
        Button btnBack = new Button("Volver al Menú Principal");
        btnBack.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        return btnBack;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
