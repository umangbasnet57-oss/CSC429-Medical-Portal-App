package edu.secourse.patientportal.ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
public class AdminDashboardView {

    public static void open(Stage stage, String username) {
        Label title = new Label("Admin / Clerk Dashboard");
        Label welcome = new Label("Welcome, " + username);

        TabPane tabPane = new TabPane();

        Tab patientsTab = new Tab("Patients", buildPatientsPane());
        patientsTab.setClosable(false);

        Tab doctorsTab = new Tab("Doctors", buildDoctorsPane());
        doctorsTab.setClosable(false);

        Tab appointmentsTab = new Tab("Appointments", buildAppointmentsPane());
        appointmentsTab.setClosable(false);

        tabPane.getTabs().addAll(patientsTab, doctorsTab, appointmentsTab);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> ChangePasswordView.open());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> LoginView.open(stage));

        HBox topButtons = new HBox(10, changePasswordButton, logoutButton);

        VBox root = new VBox(15, title, welcome, topButtons, tabPane);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private static VBox buildPatientsPane() {

        TableView<PatientRow> table = new TableView<>();

        TableColumn<PatientRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PatientRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<PatientRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, usernameCol, emailCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Sample data
        table.getItems().addAll(
                new PatientRow(1, "John Smith", "john123", "john@gmail.com"),
                new PatientRow(2, "Jane Doe", "jane456", "jane@gmail.com")
        );

        // 🔍 SEARCH
        TextField searchField = new TextField();
        searchField.setPromptText("Search patient...");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            table.setItems(FXCollections.observableArrayList(
                    table.getItems().stream()
                            .filter(p -> p.getName().toLowerCase().contains(newVal.toLowerCase()))
                            .toList()
            ));
        });

        // 📝 FORM
        TextField nameField = new TextField();
        nameField.setPromptText("Patient Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button addButton = new Button("Add Patient");

        Button deleteButton = new Button("Delete Selected");

        // ➕ ADD PATIENT
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();

            if (name.isBlank() || username.isBlank() || email.isBlank()) {
                return;
            }

            int newId = table.getItems().size() + 1;

            table.getItems().add(new PatientRow(newId, name, username, email));

            nameField.clear();
            usernameField.clear();
            emailField.clear();
        });

        // ❌ DELETE PATIENT
        deleteButton.setOnAction(e -> {
            PatientRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                table.getItems().remove(selected);
            }
        });

        VBox formBox = new VBox(10,
                new Label("Create Patient"),
                nameField,
                usernameField,
                emailField,
                addButton,
                deleteButton
        );

        HBox layout = new HBox(20, table, formBox);

        VBox root = new VBox(15,
                new Label("All Patients"),
                searchField,
                layout
        );

        root.setPadding(new Insets(15));

        return root;
    }

    private static VBox buildDoctorsPane() {

        TableView<DoctorRow> table = new TableView<>();

        TableColumn<DoctorRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<DoctorRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DoctorRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<DoctorRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.getColumns().addAll(idCol, nameCol, usernameCol, emailCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getItems().addAll(
                new DoctorRow(1, "Dr Adams", "adams01", "adams@clinic.com"),
                new DoctorRow(2, "Dr Brown", "brown02", "brown@clinic.com")
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search doctor...");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            table.setItems(FXCollections.observableArrayList(
                    table.getItems().stream()
                            .filter(d -> d.getName().toLowerCase().contains(newVal.toLowerCase()))
                            .toList()
            ));
        });

        TextField nameField = new TextField();
        nameField.setPromptText("Doctor Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button addButton = new Button("Add Doctor");
        Button deleteButton = new Button("Delete Selected");

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();

            if (name.isBlank() || username.isBlank() || email.isBlank()) {
                return;
            }

            int newId = table.getItems().size() + 1;
            table.getItems().add(new DoctorRow(newId, name, username, email));

            nameField.clear();
            usernameField.clear();
            emailField.clear();
        });

        deleteButton.setOnAction(e -> {
            DoctorRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                table.getItems().remove(selected);
            }
        });

        VBox formBox = new VBox(10,
                new Label("Create Doctor"),
                nameField,
                usernameField,
                emailField,
                addButton,
                deleteButton
        );

        HBox layout = new HBox(20, table, formBox);

        VBox root = new VBox(15,
                new Label("All Doctors"),
                searchField,
                layout
        );

        root.setPadding(new Insets(15));
        return root;
    }

    private static VBox buildAppointmentsPane() {

        TableView<AppointmentRow> table = new TableView<>();

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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getItems().addAll(
                new AppointmentRow(1, "2026-03-26", "10:00 AM", "John Smith", "Dr Adams", "ACTIVE"),
                new AppointmentRow(2, "2026-03-27", "02:00 PM", "Jane Doe", "Dr Brown", "ACTIVE")
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search patient or doctor...");

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String search = newVal.toLowerCase();
            table.setItems(FXCollections.observableArrayList(
                    table.getItems().stream()
                            .filter(a ->
                                    a.getPatientName().toLowerCase().contains(search) ||
                                            a.getDoctorName().toLowerCase().contains(search))
                            .toList()
            ));
        });

        TextField dateField = new TextField();
        dateField.setPromptText("Date");

        TextField timeField = new TextField();
        timeField.setPromptText("Time");

        TextField patientField = new TextField();
        patientField.setPromptText("Patient Name");

        TextField doctorField = new TextField();
        doctorField.setPromptText("Doctor Name");

        Button addButton = new Button("Add Appointment");
        Button cancelButton = new Button("Cancel Selected");
        Button deleteButton = new Button("Delete Selected");

        addButton.setOnAction(e -> {
            String date = dateField.getText();
            String time = timeField.getText();
            String patient = patientField.getText();
            String doctor = doctorField.getText();

            if (date.isBlank() || time.isBlank() || patient.isBlank() || doctor.isBlank()) {
                return;
            }

            int newId = table.getItems().size() + 1;
            table.getItems().add(new AppointmentRow(newId, date, time, patient, doctor, "ACTIVE"));

            dateField.clear();
            timeField.clear();
            patientField.clear();
            doctorField.clear();
        });

        cancelButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus("CANCELLED");
                table.refresh();
            }
        });

        deleteButton.setOnAction(e -> {
            AppointmentRow selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                table.getItems().remove(selected);
            }
        });

        VBox formBox = new VBox(10,
                new Label("Create Appointment"),
                dateField,
                timeField,
                patientField,
                doctorField,
                addButton,
                cancelButton,
                deleteButton
        );

        HBox layout = new HBox(20, table, formBox);

        VBox root = new VBox(15,
                new Label("All Appointments"),
                searchField,
                layout
        );

        root.setPadding(new Insets(15));
        return root;
    }
}