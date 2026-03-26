package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangePasswordView {

    public static void open() {
        Stage stage = new Stage();

        Label title = new Label("Change Password");

        PasswordField oldPass = new PasswordField();
        oldPass.setPromptText("Old Password");

        PasswordField newPass = new PasswordField();
        newPass.setPromptText("New Password");

        PasswordField confirmPass = new PasswordField();
        confirmPass.setPromptText("Confirm New Password");

        Label message = new Label();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (oldPass.getText().isBlank() || newPass.getText().isBlank() || confirmPass.getText().isBlank()) {
                message.setText("Please fill all fields.");
                return;
            }

            if (!newPass.getText().equals(confirmPass.getText())) {
                message.setText("New passwords do not match.");
                return;
            }

            message.setText("Password changed successfully.");
        });

        VBox root = new VBox(12, title, oldPass, newPass, confirmPass, saveButton, message);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 260);
        stage.setTitle("Change Password");
        stage.setScene(scene);
        stage.show();
    }
}