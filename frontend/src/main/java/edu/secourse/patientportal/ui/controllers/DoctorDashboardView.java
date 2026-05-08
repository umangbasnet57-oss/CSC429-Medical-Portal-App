package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DoctorDashboardView {

    public static void open(Stage stage, String username) {
        Label title = new Label("Doctor Dashboard");
        Label welcome = new Label("Welcome, " + getDoctorName(username));

        TableView<AppointmentRow> table = new TableView<>();
        table.setItems(loadDoctorAppointments(username));

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
        statusCol.setCellFactory(column -> new TableCell<>() {

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(status);

                if ("ACTIVE".equalsIgnoreCase(status)) {

                    setStyle("""
        
                -fx-text-fill: #22c55e;
                -fx-font-weight: bold;
            """);

                } else if ("CANCELLED".equalsIgnoreCase(status)) {

                    setStyle("""
   
                -fx-text-fill: #ef4444;
                -fx-font-weight: bold;
            """);

                } else if ("COMPLETED".equalsIgnoreCase(status)) {

                    setStyle("""
         
                -fx-text-fill: #3b82f6;
                -fx-font-weight: bold;
            """);

                } else {

                    setStyle("""
     
                -fx-text-fill: white;
                -fx-font-weight: bold;
            """);
                }
            }
        });
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

    private static ObservableList<AppointmentRow> loadDoctorAppointments(String username) {
        ObservableList<AppointmentRow> appointments = FXCollections.observableArrayList();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments/user/" + username))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode data = root.isArray() ? root : root.path("data");

            if (data.isArray()) {
                for (JsonNode appt : data) {
                    String dateTime = appt.path("appointmentDateTime").asText();
                    String status = appt.path("status").asText();

                    try {

                        LocalDateTime appointmentTime = LocalDateTime.parse(dateTime);

                        if ("ACTIVE".equalsIgnoreCase(status)
                                && appointmentTime.isBefore(LocalDateTime.now())) {

                            status = "COMPLETED";
                        }

                    } catch (Exception e) {

                    }
                    String[] parts = dateTime.split("T");

                    appointments.add(new AppointmentRow(
                            appt.path("appointmentId").asInt(),
                            parts.length > 0 ? parts[0] : "",
                            parts.length > 1 ? formatTimeForDisplay(parts[1]) : "",
                            appt.path("patient").path("username").asText(),
                            appt.path("doctor").path("username").asText(),
                            status
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    private static String formatTimeForDisplay(String time) {
        try {
            String[] split = time.split(":");

            int hour = Integer.parseInt(split[0]);
            String minute = split[1];

            String ampm = hour >= 12 ? "PM" : "AM";

            hour = hour % 12;
            if (hour == 0) {
                hour = 12;
            }

            return hour + ":" + minute + " " + ampm;

        } catch (Exception e) {
            return time;
        }

    }
    private static String getDoctorName(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users/" + username))
                    .header("Authorization", "Bearer " + AppState.token)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode user = mapper.readTree(response.body());

            return user.path("name").asText(username);

        } catch (Exception e) {
            return username;
        }
    }
}