package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PatientDashboardView {

    public static void open(Stage stage, String username) {
        Label title = new Label("Patient Dashboard");
        Label welcome = new Label("Welcome, " + username);

        TableView<AppointmentRow> table = new TableView<>(AppState.appointments);

        TableColumn<AppointmentRow, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> cell.getValue().appointmentIdProperty());

        TableColumn<AppointmentRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> cell.getValue().dateProperty());

        TableColumn<AppointmentRow, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cell -> cell.getValue().timeProperty());

        TableColumn<AppointmentRow, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(cell -> cell.getValue().doctorNameProperty());

        TableColumn<AppointmentRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        table.getColumns().addAll(idCol, dateCol, timeCol, doctorCol, statusCol);

        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus("CANCELLED");
                table.refresh();
            }
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> LoginView.open(stage));

        VBox root = new VBox(15, title, welcome, table, cancelButton, logoutButton);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Patient Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}