package edu.secourse.patientportal.ui.controllers;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardView {

    private static Label patientsCount;
    private static Label doctorsCount;
    private static Label appointmentsCount;

    public static void open(Stage stage, String username) {
        Label title = new Label("Medical Portal - Admin Dashboard");
        Label welcome = new Label("Welcome, " + username);

        UIStyle.title(title);
        UIStyle.subtitle(welcome);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> ChangePasswordView.open());
        UIStyle.success(changePasswordButton);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> LoginView.open(stage));
        UIStyle.danger(logoutButton);

        HBox topButtons = new HBox(10, changePasswordButton, logoutButton);

        patientsCount = makeCard("Patients: " + AppState.patients.size(), "#d1e7dd");
        doctorsCount = makeCard("Doctors: " + AppState.doctors.size(), "#cff4fc");
        appointmentsCount = makeCard("Appointments: " + AppState.appointments.size(), "#f8d7da");

        HBox statsBox = new HBox(20, patientsCount, doctorsCount, appointmentsCount);

        TabPane tabPane = new TabPane();

        Tab patientsTab = new Tab("Patients", buildPatientsPane());
        Tab doctorsTab = new Tab("Doctors", buildDoctorsPane());
        Tab appointmentsTab = new Tab("Appointments", buildAppointmentsPane());

        patientsTab.setClosable(false);
        doctorsTab.setClosable(false);
        appointmentsTab.setClosable(false);

        tabPane.getTabs().addAll(patientsTab, doctorsTab, appointmentsTab);

        VBox root = new VBox(18, title, welcome, topButtons, statsBox, tabPane);
        root.setPadding(new Insets(25));
        UIStyle.page(root);

        Scene scene = new Scene(root, 1150, 720);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private static Label makeCard(String text, String color) {
        Label card = new Label(text);
        card.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-padding: 16;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #cbd5e1;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );
        return card;
    }

    private static void refreshStats() {
        patientsCount.setText("Patients: " + AppState.patients.size());
        doctorsCount.setText("Doctors: " + AppState.doctors.size());
        appointmentsCount.setText("Appointments: " + AppState.appointments.size());
    }

    private static VBox buildPatientsPane() {
        TableView<PatientRow> table = new TableView<>(AppState.patients);
        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<PatientRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PatientRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<PatientRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, usernameCol, emailCol);

        TextField searchField = new TextField();
        searchField.setPromptText("Search patient by first or last name...");

        TextField nameField = new TextField();
        nameField.setPromptText("Patient Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                table.setItems(AppState.patients);
            } else {
                String search = newVal.toLowerCase();
                table.setItems(FXCollections.observableArrayList(
                        AppState.patients.stream()
                                .filter(p -> p.getName().toLowerCase().contains(search))
                                .toList()
                ));
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                usernameField.setText(selected.getUsername());
                emailField.setText(selected.getEmail());
            }
        });

        Button addButton = new Button("Add Patient");
        UIStyle.primary(addButton);
        addButton.setOnAction(e -> {
            if (nameField.getText().isBlank() || usernameField.getText().isBlank() || emailField.getText().isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill all patient fields.");
                return;
            }

            int nextId = AppState.patients.size() + 1;
            AppState.patients.add(new PatientRow(nextId, nameField.getText(), usernameField.getText(), emailField.getText()));
            table.setItems(AppState.patients);
            nameField.clear();
            usernameField.clear();
            emailField.clear();
            refreshStats();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Patient added successfully.");
        });

        Button updateButton = new Button("Update Selected");
        UIStyle.primary(updateButton);
        updateButton.setOnAction(e -> {
            PatientRow selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a patient first.");
                return;
            }

            selected.setName(nameField.getText());
            selected.setUsername(usernameField.getText());
            selected.setEmail(emailField.getText());
            table.refresh();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Patient updated.");
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {
            PatientRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AppState.patients.remove(selected);
                table.setItems(AppState.patients);
                nameField.clear();
                usernameField.clear();
                emailField.clear();
                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Patient deleted.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a patient first.");
            }
        });

        Label sectionTitle = new Label("All Patients");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Patient");
        UIStyle.subtitle(formTitle);

        VBox formBox = new VBox(10, formTitle, nameField, usernameField, emailField, addButton, updateButton, deleteButton, message);
        HBox layout = new HBox(25, table, formBox);

        VBox root = new VBox(15, sectionTitle, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        return root;
    }

    private static VBox buildDoctorsPane() {
        TableView<DoctorRow> table = new TableView<>(AppState.doctors);
        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<DoctorRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<DoctorRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DoctorRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<DoctorRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, usernameCol, emailCol);

        TextField searchField = new TextField();
        searchField.setPromptText("Search doctor by first or last name...");

        TextField nameField = new TextField();
        nameField.setPromptText("Doctor Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                table.setItems(AppState.doctors);
            } else {
                String search = newVal.toLowerCase();
                table.setItems(FXCollections.observableArrayList(
                        AppState.doctors.stream()
                                .filter(d -> d.getName().toLowerCase().contains(search))
                                .toList()
                ));
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                usernameField.setText(selected.getUsername());
                emailField.setText(selected.getEmail());
            }
        });

        Button addButton = new Button("Add Doctor");
        UIStyle.primary(addButton);
        addButton.setOnAction(e -> {
            if (nameField.getText().isBlank() || usernameField.getText().isBlank() || emailField.getText().isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill all doctor fields.");
                return;
            }

            int nextId = AppState.doctors.size() + 1;
            AppState.doctors.add(new DoctorRow(nextId, nameField.getText(), usernameField.getText(), emailField.getText()));
            table.setItems(AppState.doctors);
            nameField.clear();
            usernameField.clear();
            emailField.clear();
            refreshStats();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Doctor added successfully.");
        });

        Button updateButton = new Button("Update Selected");
        UIStyle.primary(updateButton);
        updateButton.setOnAction(e -> {
            DoctorRow selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a doctor first.");
                return;
            }

            selected.setName(nameField.getText());
            selected.setUsername(usernameField.getText());
            selected.setEmail(emailField.getText());
            table.refresh();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Doctor updated.");
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {
            DoctorRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AppState.doctors.remove(selected);
                table.setItems(AppState.doctors);
                nameField.clear();
                usernameField.clear();
                emailField.clear();
                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Doctor deleted.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a doctor first.");
            }
        });

        Label sectionTitle = new Label("All Doctors");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Doctor");
        UIStyle.subtitle(formTitle);

        VBox formBox = new VBox(10, formTitle, nameField, usernameField, emailField, addButton, updateButton, deleteButton, message);
        HBox layout = new HBox(25, table, formBox);

        VBox root = new VBox(15, sectionTitle, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        return root;
    }

    private static VBox buildAppointmentsPane() {
        TableView<AppointmentRow> table = new TableView<>(AppState.appointments);
        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<AppointmentRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));

        TableColumn<AppointmentRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<AppointmentRow, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<AppointmentRow, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        TableColumn<AppointmentRow, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));

        TableColumn<AppointmentRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, dateCol, timeCol, patientCol, doctorCol, statusCol);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by patient last name, doctor name, or full name...");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("ALL", "ACTIVE", "CANCELLED");
        statusFilter.setValue("ALL");

        TextField dateField = new TextField();
        dateField.setPromptText("Date ex: 2026-05-07");

        TextField timeField = new TextField();
        timeField.setPromptText("Time ex: 10:30 AM");

        TextField patientField = new TextField();
        patientField.setPromptText("Patient Name");

        TextField doctorField = new TextField();
        doctorField.setPromptText("Doctor Name");

        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        Runnable applyFilters = () -> {
            String search = searchField.getText().toLowerCase();
            String selectedStatus = statusFilter.getValue();

            table.setItems(FXCollections.observableArrayList(
                    AppState.appointments.stream()
                            .filter(a -> selectedStatus.equals("ALL") || a.getStatus().equals(selectedStatus))
                            .filter(a -> a.getPatientName().toLowerCase().contains(search)
                                    || a.getDoctorName().toLowerCase().contains(search))
                            .toList()
            ));
        };

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters.run());
        statusFilter.setOnAction(e -> applyFilters.run());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                dateField.setText(selected.getDate());
                timeField.setText(selected.getTime());
                patientField.setText(selected.getPatientName());
                doctorField.setText(selected.getDoctorName());
            }
        });

        Button addButton = new Button("Add Appointment");
        UIStyle.primary(addButton);
        addButton.setOnAction(e -> {
            if (dateField.getText().isBlank() || timeField.getText().isBlank()
                    || patientField.getText().isBlank() || doctorField.getText().isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill all appointment fields.");
                return;
            }

            int nextId = AppState.appointments.size() + 1;
            AppState.appointments.add(new AppointmentRow(nextId, dateField.getText(), timeField.getText(), patientField.getText(), doctorField.getText(), "ACTIVE"));

            table.setItems(AppState.appointments);
            dateField.clear();
            timeField.clear();
            patientField.clear();
            doctorField.clear();
            refreshStats();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Appointment added successfully.");
        });

        Button updateButton = new Button("Update Selected");
        UIStyle.primary(updateButton);
        updateButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
                return;
            }

            selected.setDate(dateField.getText());
            selected.setTime(timeField.getText());
            selected.setPatientName(patientField.getText());
            selected.setDoctorName(doctorField.getText());
            table.refresh();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Appointment updated.");
        });

        Button cancelButton = new Button("Cancel Selected");
        UIStyle.primary(cancelButton);
        cancelButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus("CANCELLED");
                table.refresh();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment cancelled.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
            }
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AppState.appointments.remove(selected);
                table.setItems(AppState.appointments);
                dateField.clear();
                timeField.clear();
                patientField.clear();
                doctorField.clear();
                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment deleted.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
            }
        });

        Label sectionTitle = new Label("All Appointments");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Appointment");
        UIStyle.subtitle(formTitle);

        VBox formBox = new VBox(10, formTitle, dateField, timeField, patientField, doctorField, addButton, updateButton, cancelButton, deleteButton, message);

        HBox filters = new HBox(15, new Label("Status:"), statusFilter);
        HBox layout = new HBox(25, table, formBox);

        VBox root = new VBox(15, sectionTitle, filters, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        return root;
    }
}