package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    public static void open(Stage stage) {
        Label title = new Label("Medical Portal App");
        Label subtitle = new Label("Common Landing / Login Page");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username / Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<UserRole> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(UserRole.ADMIN, UserRole.PATIENT, UserRole.DOCTOR);
        roleBox.setPromptText("Select Role");

        Label message = new Label();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            UserRole role = roleBox.getValue();

            if (usernameField.getText().isBlank() || passwordField.getText().isBlank() || role == null) {
                message.setText("Please fill all fields.");
                return;
            }

            switch (role) {
                case ADMIN -> AdminDashboardView.open(stage, usernameField.getText());
                case PATIENT -> PatientDashboardView.open(stage, usernameField.getText());
                case DOCTOR -> DoctorDashboardView.open(stage, usernameField.getText());
            }
        });

        VBox root = new VBox(12, title, subtitle, usernameField, passwordField, roleBox, loginButton, message);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 420, 280);
        stage.setTitle("Medical Portal - Login");
        stage.setScene(scene);
        stage.show();
    }
}