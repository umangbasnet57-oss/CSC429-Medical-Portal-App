package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

        ComboBox<UserRole> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(UserRole.ADMIN, UserRole.PATIENT, UserRole.DOCTOR);
        roleBox.setPromptText("Select Role");
        roleBox.setMaxWidth(320);

        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(320);
        loginButton.setStyle("-fx-background-color: #1f6feb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");

        Label demoInfo = new Label("Demo Accounts: admin/admin123 | patient/patient123 | doctor/doctor123");
        demoInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #6c757d;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            UserRole role = roleBox.getValue();

            if (username.isBlank() || password.isBlank() || role == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill username, password, and role.");
                return;
            }

            boolean validLogin =
                    (role == UserRole.ADMIN && username.equals("admin") && password.equals("admin123")) ||
                            (role == UserRole.PATIENT && username.equals("patient") && password.equals("patient123")) ||
                            (role == UserRole.DOCTOR && username.equals("doctor") && password.equals("doctor123"));

            if (!validLogin) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Invalid login. Use the correct demo account for the selected role.");
                return;
            }

            switch (role) {
                case ADMIN -> AdminDashboardView.open(stage, username);
                case PATIENT -> PatientDashboardView.open(stage, username);
                case DOCTOR -> DoctorDashboardView.open(stage, username);
            }
        });

        VBox card = new VBox(15, title, subtitle, usernameField, passwordField, roleBox, loginButton, message, demoInfo);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35));
        card.setMaxWidth(430);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-color: #d0d7de;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #dbeafe, #f8fafc);");

        Scene scene = new Scene(root, 650, 500);
        stage.setTitle("Medical Portal - Login");
        stage.setScene(scene);
        stage.show();
    }
}