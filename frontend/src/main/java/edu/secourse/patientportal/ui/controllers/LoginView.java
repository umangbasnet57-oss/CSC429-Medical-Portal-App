package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class LoginView {

    public static void open(Stage stage) {
        Label title = new Label("Medical Portal");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1f3b57;");


        Label subtitle = new Label("Secure Patient Management System");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #5f6c7b;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);


        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(320);
        loginButton.setStyle("-fx-background-color: #1f6feb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");


        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            try {
                HttpClient client = HttpClient.newHttpClient();
                String jsonBody = """
            {
              "username": "%s",
              "password": "%s"
            }
            """.formatted(username, password);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/maclogixapi/v1/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    message.setText("Login failed.");
                    return;
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.path("data");

                String role = data.path("role").asText();

                String returnedUsername = data.path("username").asText();
                String returnedRole = data.path("role").asText();
                String token = data.path("token").asText();

                AppState.token = token;
                AppState.currentUsername = returnedUsername;
                AppState.currentRole = returnedRole;

                if (role.equalsIgnoreCase("ADMIN")) {
                    AdminDashboardView.open(stage, username);
                } else if (role.equalsIgnoreCase("PATIENT")) {
                    PatientDashboardView.open(stage, username);
                } else if (role.equalsIgnoreCase("DOCTOR")) {
                    DoctorDashboardView.open(stage, username);
                } else {
                    message.setStyle("-fx-text-fill: red;");
                    message.setText("Unknown role.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not connect to backend.");
            }
        });


        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> loginButton.fire());


        VBox card = new VBox(15, title, subtitle, usernameField, passwordField, loginButton, message);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35));
        card.setMaxWidth(380);
        card.setMaxHeight(320);
        card.setStyle("""
        -fx-background-color: rgba(255,255,255,0.90);
        -fx-background-radius: 22;
        -fx-padding: 28;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0.2, 0, 8);
        """);

        ImageView background = new ImageView(
                new Image(
                        Thread.currentThread()
                                .getContextClassLoader()
                                .getResource("images/gifwallpaper.gif")
                                .toExternalForm()
                )
        );

        background.fitWidthProperty().bind(stage.widthProperty());
        background.fitHeightProperty().bind(stage.heightProperty());
        background.setPreserveRatio(false);

        StackPane root = new StackPane(background, card);

        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Medical Portal - Login");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.centerOnScreen();
        stage.show();
    }
}