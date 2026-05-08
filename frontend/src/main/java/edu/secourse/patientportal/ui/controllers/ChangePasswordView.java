package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChangePasswordView {

    public static void open() {
        Stage stage = new Stage();

        Label title = new Label("Change Password");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Label message = new Label();

        Button saveButton = new Button("Update Password");

        saveButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (newPassword.isBlank() || confirmPassword.isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill both fields.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Passwords do not match.");
                return;
            }

            boolean updated = updatePasswordInBackend(newPassword);

            if (updated) {
                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Password updated successfully.");
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not update password.");
            }
        });

        VBox root = new VBox(12, title, newPasswordField, confirmPasswordField, saveButton, message);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 380, 250);
        stage.setTitle("Change Password");
        stage.setScene(scene);
        stage.show();
    }

    private static boolean updatePasswordInBackend(String newPassword) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = """
                    {
                      "password": "%s"
                    }
                    """.formatted(newPassword);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users/" + AppState.currentUsername))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("CHANGE PASSWORD status: " + response.statusCode());
            System.out.println("CHANGE PASSWORD body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}