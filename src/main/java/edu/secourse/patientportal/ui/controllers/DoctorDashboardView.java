package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DoctorDashboardView {

    public static void open(Stage stage, String username) {
        Label title = new Label("Doctor Dashboard");
        Label welcome = new Label("Welcome, " + username);

        TableView<AppointmentRow> table = new TableView<>(AppState.appointments);

        TableColumn<AppointmentRow, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> cell.getValue().appointmentIdProperty());

        TableColumn<AppointmentRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty());

        TableColumn<AppointmentRow, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cell -> cell.getValue().timeProperty());

        TableColumn<AppointmentRow, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cell -> cell.getValue().patientNameProperty());

        TableColumn<AppointmentRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        table.getColumns().addAll(idCol, dateCol, timeCol, patientCol, statusCol);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> ChangePasswordView.open());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> LoginView.open(stage));

        UIStyle.title(title);
        UIStyle.subtitle(welcome);
        UIStyle.table(table);
        UIStyle.success(changePasswordButton);
        UIStyle.danger(logoutButton);

        VBox root = new VBox(15, title, welcome, table, changePasswordButton, logoutButton);
        UIStyle.page(root);

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Doctor Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}