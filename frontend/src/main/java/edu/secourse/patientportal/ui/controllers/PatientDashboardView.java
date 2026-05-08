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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class PatientDashboardView {

    public static void open(Stage stage, String username) {
        Label title = new Label("Patient Dashboard");
        Label welcome = new Label("Welcome, " + getPatientName(username));


        ObservableList<AppointmentRow> allAppointments =
                loadPatientAppointments(username);
        ObservableList<AppointmentRow> upcomingAppointments =
                FXCollections.observableArrayList(
                        allAppointments.stream()
                                .filter(a ->
                                        !"CANCELLED".equalsIgnoreCase(a.getStatus())
                                                && !LocalDate.parse(a.getDate()).isBefore(LocalDate.now()))
                                .toList()
                );
        ObservableList<AppointmentRow> pastAppointments =
                FXCollections.observableArrayList(
                        allAppointments.stream()
                                .filter(a ->
                                        "CANCELLED".equalsIgnoreCase(a.getStatus())
                                                || LocalDate.parse(a.getDate()).isBefore(LocalDate.now()))
                                .peek(a -> {
                                    if (!"CANCELLED".equalsIgnoreCase(a.getStatus())
                                            && LocalDate.parse(a.getDate()).isBefore(LocalDate.now())) {
                                        a.setStatus("COMPLETED");
                                    }
                                })
                                .sorted((a, b) ->
                                        LocalDate.parse(b.getDate())
                                                .compareTo(LocalDate.parse(a.getDate())))
                                .toList()

                );

        for (int i = 0; i < pastAppointments.size(); i++) {
            pastAppointments.get(i).setAppointmentId(i + 1);
        }

        TableView<AppointmentRow> upcomingTable = new TableView<>();
        upcomingTable.setItems(upcomingAppointments);
        TableView<AppointmentRow> pastTable = new TableView<>();
        pastTable.setItems(pastAppointments);

        UIStyle.table(upcomingTable);
        UIStyle.darkTable(upcomingTable);

        upcomingTable.setStyle(upcomingTable.getStyle() + """
    -fx-base: #1e1e1e;
    -fx-control-inner-background: #1e1e1e;
    """);

        pastTable.setStyle(pastTable.getStyle() + """
    -fx-base: #1e1e1e;
    -fx-control-inner-background: #1e1e1e;
    """);

        UIStyle.table(pastTable);
        UIStyle.darkTable(pastTable);

        pastTable.setItems(pastAppointments);


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
    -fx-background-color: #1e1e1e;
    -fx-border-color: #3c3f41;
    -fx-text-fill: green;
    -fx-font-weight: bold;
    """);

                } else if ("CANCELLED".equalsIgnoreCase(status)) {

                    setStyle("""
    -fx-background-color: #1e1e1e;
    -fx-border-color: #3c3f41;
    -fx-text-fill: #ef4444;
    -fx-font-weight: bold;
    """);
                } else if ("COMPLETED".equalsIgnoreCase(status)) {

                    setStyle("""
    -fx-background-color: #1e1e1e;
    -fx-border-color: #3c3f41;
    -fx-text-fill: blue;
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
        upcomingTable.getColumns().addAll(idCol, dateCol, timeCol, doctorCol, statusCol);
        TableColumn<AppointmentRow, Number> pastIdCol = new TableColumn<>("ID");
        pastIdCol.setCellValueFactory(cell -> cell.getValue().appointmentIdProperty());


        TableColumn<AppointmentRow, String> pastDateCol = new TableColumn<>("Date");
        pastDateCol.setCellValueFactory(cell -> cell.getValue().dateProperty());


        TableColumn<AppointmentRow, String> pastTimeCol = new TableColumn<>("Time");
        pastTimeCol.setCellValueFactory(cell -> cell.getValue().timeProperty());


        TableColumn<AppointmentRow, String> pastDoctorCol = new TableColumn<>("Doctor");
        pastDoctorCol.setCellValueFactory(cell -> cell.getValue().doctorNameProperty());


        TableColumn<AppointmentRow, String> pastStatusCol = new TableColumn<>("Status");
        pastStatusCol.setCellValueFactory(cell -> cell.getValue().statusProperty());

        pastIdCol.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        pastDateCol.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        pastTimeCol.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        pastDoctorCol.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-font-weight: bold;");
        pastStatusCol.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-font-weight: bold;");

        pastStatusCol.setCellFactory(column -> new TableCell<>() {
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
    -fx-background-color: #1e1e1e;
    -fx-text-fill: green;
    -fx-font-weight: bold;
    """);

                } else if ("CANCELLED".equalsIgnoreCase(status)) {

                    setStyle("""
    -fx-background-color: #1e1e1e;
    -fx-text-fill: #ef4444;
    -fx-font-weight: bold;
    """);

                } else if ("COMPLETED".equalsIgnoreCase(status)) {

                    setStyle("""
    -fx-background-color: #1e1e1e;
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
        pastTable.getColumns().addAll(
                pastIdCol,
                pastDateCol,
                pastTimeCol,
                pastDoctorCol,
                pastStatusCol
        );

        Button cancelButton = new Button("Cancel Selected");
        cancelButton.setOnAction(e -> {
            AppointmentRow selected = upcomingTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                return;
            }

            boolean cancelled = cancelAppointmentInBackend(selected.getAppointmentId());

            if (cancelled) {
                upcomingAppointments.remove(selected);

                selected.setStatus("CANCELLED");
                pastAppointments.add(0, selected);

                for (int i = 0; i < pastAppointments.size(); i++) {
                    pastAppointments.get(i).setAppointmentId(i + 1);
                }

                upcomingTable.refresh();
                pastTable.refresh();
            }
        });


        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> LoginView.open(stage));

        UIStyle.title(title);
        UIStyle.subtitle(welcome);
        UIStyle.primary(cancelButton);
        UIStyle.danger(logoutButton);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("""
        -fx-background-color: #1e1e1e;
        -fx-control-inner-background: #1e1e1e;
        -fx-tab-min-height: 36;
        """);
        Tab upcomingTab = new Tab("Upcoming Appointments", upcomingTable);
        upcomingTab.setClosable(false);
        Tab pastTab = new Tab("Past Appointments", pastTable);
        pastTab.setClosable(false);
        tabPane.getTabs().addAll(upcomingTab, pastTab);

        VBox root = new VBox(
                15,
                title,
                welcome,
                tabPane,
                cancelButton,
                logoutButton
        );

        root.setStyle("""
    -fx-background-color: #1e1e1e;
    """);

        root.setPadding(new Insets(20));



        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Patient Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private static ObservableList<AppointmentRow> loadPatientAppointments(String username) {
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
                    String[] parts = dateTime.split("T");

                    appointments.add(new AppointmentRow(
                            appt.path("appointmentId").asInt(),
                            parts.length > 0 ? parts[0] : "",
                            parts.length > 1 ? formatTimeForDisplay(parts[1]) : "",
                            appt.path("patient").path("username").asText(),
                            appt.path("doctor").path("name").asText(
                                    appt.path("doctor").path("username").asText()
                            ),
                            appt.path("status").asText()
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
    private static String formatTimeForDisplay(String time24) {
        if (time24 == null || time24.isBlank()) {
            return "";
        }

        String[] parts = time24.split(":");
        int hour = Integer.parseInt(parts[0]);
        String minute = parts[1];

        String amPm = hour >= 12 ? "PM" : "AM";
        int hour12 = hour % 12;

        if (hour12 == 0) {
            hour12 = 12;
        }

        return hour12 + ":" + minute + " " + amPm;
    }

    private static boolean cancelAppointmentInBackend(int appointmentId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments/" + appointmentId + "/cancel"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("PATIENT CANCEL status: " + response.statusCode());
            System.out.println("PATIENT CANCEL body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getPatientName(String username) {
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