package edu.secourse.patientportal.ui.controllers;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


// class definition
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

        patientsCount = makeCard("Patients: " + loadPatientsFromBackend().size(), "#d1e7dd");
        doctorsCount = makeCard("Doctors: " + loadDoctorsFromBackend().size(), "#cff4fc");
        appointmentsCount = makeCard("Appointments: " + loadAppointmentsFromBackend().size(), "#f8d7da");

        HBox statsBox = new HBox(20, patientsCount, doctorsCount, appointmentsCount);

        TabPane tabPane = new TabPane();

        Tab patientsTab = new Tab("Patients", buildPatientsPane());
        Tab doctorsTab = new Tab("Doctors", buildDoctorsPane());
        Tab appointmentsTab = new Tab("Appointments", buildAppointmentsPane());
        appointmentsTab.setOnSelectionChanged(e -> {
            if (appointmentsTab.isSelected()) {
                appointmentsTab.setContent(buildAppointmentsPane());
            }
        });

        patientsTab.setClosable(false);
        doctorsTab.setClosable(false);
        appointmentsTab.setClosable(false);

        tabPane.getTabs().addAll(patientsTab, doctorsTab, appointmentsTab);

        VBox root = new VBox(18, title, welcome, topButtons, statsBox, tabPane);
        root.setPadding(new Insets(25));
        UIStyle.page(root);


        Scene scene = new Scene(root, 1080, 860);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.centerOnScreen();

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
        patientsCount.setText("Patients: " + loadPatientsFromBackend().size());
        doctorsCount.setText("Doctors: " + loadDoctorsFromBackend().size());
        appointmentsCount.setText("Appointments: " + loadAppointmentsFromBackend().size());
    }

    private static VBox buildPatientsPane() {
        TableView<PatientRow> table = new TableView<>();
        table.setItems(loadPatientsFromBackend());

        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<PatientRow, Number> rowCol =
                new TableColumn<>("ID");

        rowCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        table.getItems().indexOf(cellData.getValue()) + 1
                ));

        TableColumn<PatientRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<PatientRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        table.getColumns().addAll(
                rowCol,
                nameCol,
                usernameCol,
                emailCol
        );

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
                table.setItems(loadPatientsFromBackend());
            } else {
                String search = newVal.toLowerCase();
                table.setItems(FXCollections.observableArrayList(
                        loadPatientsFromBackend().stream()
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
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill all patient fields.");
                return;
            }

            if (username.contains(" ")) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Username cannot contain spaces.");
                return;
            }

            boolean created = createUserInBackend(username, password, name, email, "PATIENT");

            if (created) {
                table.setItems(loadPatientsFromBackend());

                nameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Patient added to database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not add patient to database.");
            }
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

            String oldUsername = selected.getUsername();
            String newUsername = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (newUsername.isBlank() || name.isBlank() || email.isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Name, username, and email are required.");
                return;
            }

            boolean updated = updateUserInBackend(oldUsername, newUsername, name, email, password, "PATIENT");

            if (updated) {
                table.setItems(loadPatientsFromBackend());

                nameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Patient updated in database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not update patient.");
            }
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {
            PatientRow selected = table.getSelectionModel().getSelectedItem();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete Patient");
            confirm.setContentText(
                    "Are you sure you want to permanently delete this Patient?"
            );
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }

            if (selected != null) {
                boolean deleted = deleteUserFromBackend(selected.getUsername());

                if (deleted) {
                    table.setItems(loadPatientsFromBackend());
                    refreshStats();

                    nameField.clear();
                    usernameField.clear();
                    emailField.clear();

                    message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    message.setText("Patient deleted from database.");
                } else {
                    message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    message.setText("Could not delete patient from database.");
                }
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a patient first.");
            }
        });

        Label sectionTitle = new Label("All Patients");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Patient");
        UIStyle.subtitle(formTitle);

        VBox formBox = new VBox(10, formTitle, nameField, usernameField, emailField, passwordField, addButton, updateButton, deleteButton, message);
        HBox layout = new HBox(25, table, formBox);

        VBox root = new VBox(15, sectionTitle, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        return root;
    }

    private static VBox buildDoctorsPane() {
        TableView<DoctorRow> table = new TableView<>();
        table.setItems(loadDoctorsFromBackend());
        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<DoctorRow, Number> rowCol =
                new TableColumn<>("ID");

        rowCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        table.getItems().indexOf(cellData.getValue()) + 1
                ));

        TableColumn<DoctorRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DoctorRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<DoctorRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        table.getColumns().addAll(
                rowCol,
                nameCol,
                usernameCol,
                emailCol
        );

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
                table.setItems(loadDoctorsFromBackend());
            } else {
                String search = newVal.toLowerCase();
                table.setItems(FXCollections.observableArrayList(
                        loadDoctorsFromBackend().stream()
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
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please fill all doctor fields.");
                return;
            }

            if (username.contains(" ")) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Username cannot contain spaces.");
                return;
            }

            boolean created = createUserInBackend(username, password, name, email, "DOCTOR");

            if (created) {
                table.setItems(loadDoctorsFromBackend());

                nameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Doctor added to database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not add doctor to database.");
            }
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

            String oldUsername = selected.getUsername();
            String newUsername = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (newUsername.isBlank() || name.isBlank() || email.isBlank()) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Name, username, and email are required.");
                return;
            }

            boolean updated = updateUserInBackend(oldUsername, newUsername, name, email, password, "DOCTOR");

            if (updated) {
                table.setItems(loadDoctorsFromBackend());

                nameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Doctor updated in database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not update doctor.");
            }
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {

            DoctorRow selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Doctor");
            alert.setContentText("Are you sure you want to permanently delete this doctor?");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result != ButtonType.OK) {
                return;
            }

            if (selected != null) {
                boolean deleted = deleteUserFromBackend(selected.getUsername());

                if (deleted) {
                    table.setItems(loadDoctorsFromBackend());
                    refreshStats();

                    nameField.clear();
                    usernameField.clear();
                    emailField.clear();

                    message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    message.setText("Doctor deleted from database.");
                } else {
                    message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    message.setText("Could not delete doctor from database.");
                }
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select a doctor first.");
            }
        });


        Label sectionTitle = new Label("All Doctors");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Doctor");
        UIStyle.subtitle(formTitle);

        VBox formBox = new VBox(10, formTitle, nameField, usernameField, emailField, passwordField, addButton, updateButton, deleteButton, message);
        HBox layout = new HBox(25, table, formBox);

        VBox root = new VBox(15, sectionTitle, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        return root;
    }

    private static VBox buildAppointmentsPane() {
        TableView<AppointmentRow> table = new TableView<>();
        table.setItems(loadAppointmentsFromBackend());
        table.setPrefHeight(420);
        UIStyle.table(table);

        TableColumn<AppointmentRow, Number> rowCol =
                new TableColumn<>("ID");

        rowCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        table.getItems().indexOf(cellData.getValue()) + 1
                ));


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
        -fx-text-fill: green;
        -fx-font-weight: bold;
    """);

                } else if ("CANCELLED".equalsIgnoreCase(status)) {

                    setStyle("""
        -fx-text-fill: red;
        -fx-font-weight: bold;
    """);

                } else if ("COMPLETED".equalsIgnoreCase(status)) {

                    setStyle("""
        -fx-text-fill: #3b82f6;
        -fx-font-weight: bold;
    """);

                } else {

                    setStyle("""
        -fx-font-weight: bold;
    """);
                }
            }
        });

        table.getColumns().addAll(
                rowCol,
                dateCol,
                timeCol,
                patientCol,
                doctorCol,
                statusCol
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search by patient last name, doctor name, or full name...");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("ALL", "ACTIVE", "CANCELLED");
        statusFilter.setValue("ALL");


        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        ComboBox<Integer> hourBox = new ComboBox<>();
        hourBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        hourBox.setPromptText("Hour");

        ComboBox<String> minuteBox = new ComboBox<>();
        minuteBox.getItems().addAll("00", "15", "30", "45");
        minuteBox.setPromptText("Minute");

        ComboBox<String> amPmBox = new ComboBox<>();
        amPmBox.getItems().addAll("AM", "PM");
        amPmBox.setPromptText("AM/PM");

        HBox timeBox = new HBox(8, hourBox, minuteBox, amPmBox);


        ComboBox<PatientRow> patientBox = new ComboBox<>();
        patientBox.setItems(loadPatientsFromBackend());
        patientBox.setPromptText("Select Patient");

        ComboBox<DoctorRow> doctorBox = new ComboBox<>();
        doctorBox.setItems(loadDoctorsFromBackend());
        doctorBox.setPromptText("Select Doctor");

        Runnable refreshAppointmentDropdowns = () -> {
            patientBox.setItems(loadPatientsFromBackend());
            doctorBox.setItems(loadDoctorsFromBackend());
        };

        patientBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(PatientRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUsername() + ")");
            }
        });

        patientBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(PatientRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUsername() + ")");
            }
        });

        doctorBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(DoctorRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUsername() + ")");
            }
        });

        doctorBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DoctorRow item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUsername() + ")");
            }
        });


        Label message = new Label();
        message.setStyle("-fx-font-weight: bold;");

        Runnable applyFilters = () -> {
            String search = searchField.getText().toLowerCase();
            String selectedStatus = statusFilter.getValue();

            table.setItems(FXCollections.observableArrayList(
                    loadAppointmentsFromBackend().stream()
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

                datePicker.setValue(java.time.LocalDate.parse(selected.getDate()));

                setTimeDropdowns(selected.getTime(), hourBox, minuteBox, amPmBox);

                patientBox.getItems().stream()
                        .filter(p -> p.getUsername().equals(selected.getPatientName()))
                        .findFirst()
                        .ifPresent(patientBox::setValue);

                doctorBox.getItems().stream()
                        .filter(d -> d.getUsername().equals(selected.getDoctorName()))
                        .findFirst()
                        .ifPresent(doctorBox::setValue);
            }
        });


        Button addButton = new Button("Add Appointment");
        UIStyle.primary(addButton);

        addButton.setOnAction(e -> {
            PatientRow selectedPatient = patientBox.getValue();
            DoctorRow selectedDoctor = doctorBox.getValue();

            Integer hour = hourBox.getValue();
            String minute = minuteBox.getValue();
            String amPm = amPmBox.getValue();

            if (datePicker.getValue() == null || hour == null || minute == null || amPm == null
                    || selectedPatient == null || selectedDoctor == null) {
            }


            int hour24 = hourBox.getValue();

            if ("AM".equals(amPmBox.getValue()) && hour24 == 12) {
                hour24 = 0;
            } else if ("PM".equals(amPmBox.getValue()) && hour24 != 12) {
                hour24 += 12;
            }

            String time24 = String.format("%02d:%s:00", hour24, minuteBox.getValue());

            String dateTime =
                    datePicker.getValue().toString() + "T" + time24;

            boolean created = createAppointmentInBackend(
                    selectedPatient.getUsername(),
                    selectedDoctor.getUsername(),
                    dateTime
            );

            if (created) {
                table.setItems(loadAppointmentsFromBackend());

                datePicker.setValue(null);
                hourBox.setValue(null);
                minuteBox.setValue(null);
                amPmBox.setValue(null);
                patientBox.setValue(null);
                doctorBox.setValue(null);

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment added to database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not add appointment to database.");
            }
        });

        Button updateButton = new Button("Update Selected");
        UIStyle.primary(updateButton);
        updateButton.setOnAction(e -> {
            AppointmentRow selectedAppointment = table.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
                return;
            }

            PatientRow selectedPatient = patientBox.getValue();
            DoctorRow selectedDoctor = doctorBox.getValue();
            Integer hour = hourBox.getValue();
            String minute = minuteBox.getValue();
            String amPm = amPmBox.getValue();

            if (datePicker.getValue() == null || selectedPatient == null || selectedDoctor == null
                    || hour == null || minute == null || amPm == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Please select patient, doctor, date, hour, minute, and AM/PM.");
                return;
            }

            int hour24 = hour;

            if ("AM".equals(amPm) && hour == 12) {
                hour24 = 0;
            } else if ("PM".equals(amPm) && hour != 12) {
                hour24 = hour + 12;
            }

            String time24 = String.format("%02d:%s:00", hour24, minute);
            String dateTime = datePicker.getValue().toString() + "T" + time24;

            boolean updated = updateAppointmentInBackend(
                    selectedAppointment.getAppointmentId(),
                    selectedPatient.getUsername(),
                    selectedDoctor.getUsername(),
                    dateTime
            );

            if (updated) {
                table.setItems(loadAppointmentsFromBackend());

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment updated in database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not update appointment.");
            }
        });

        Button statusButton = new Button("Toggle Status");

        UIStyle.primary(statusButton);
        statusButton.setOnAction(e -> {

            AppointmentRow selectedAppointment =
                    table.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {

                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
                return;
            }

            boolean success =
                    toggleAppointmentStatusInBackend(
                            selectedAppointment.getAppointmentId());

            if (success) {

                table.setItems(loadAppointmentsFromBackend());

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment status updated.");

            } else {

                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not update appointment status.");
            }
        });

        Button deleteButton = new Button("Delete Selected");
        UIStyle.danger(deleteButton);
        deleteButton.setOnAction(e -> {
            AppointmentRow selectedAppointment = table.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Select an appointment first.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete Appointment");
            confirm.setContentText("Are you sure you want to permanently delete this appointment?");

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }

            boolean deleted = deleteAppointmentInBackend(selectedAppointment.getAppointmentId());

            if (deleted) {
                table.setItems(loadAppointmentsFromBackend());

                datePicker.setValue(null);
                hourBox.setValue(null);
                minuteBox.setValue(null);
                amPmBox.setValue(null);
                patientBox.setValue(null);
                doctorBox.setValue(null);

                refreshStats();

                message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                message.setText("Appointment deleted from database.");
            } else {
                message.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                message.setText("Could not delete appointment.");
            }
        });

        Button refreshButton = new Button("Refresh Data");
        UIStyle.primary(refreshButton);

        refreshButton.setOnAction(ex -> {

            table.setItems(loadAppointmentsFromBackend());

            refreshStats();

            message.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            message.setText("Data refreshed from database.");
        });

        Label sectionTitle = new Label("All Appointments");
        UIStyle.subtitle(sectionTitle);

        Label formTitle = new Label("Create / Update Appointment");
        UIStyle.subtitle(formTitle);

        HBox addDeleteRow = new HBox(10, addButton, deleteButton);
        HBox updateRefreshRow = new HBox(10, updateButton, refreshButton);
        HBox statusRow = new HBox(10, statusButton);
        VBox formBox = new VBox(
                15,
                formTitle,
                patientBox,
                doctorBox,
                datePicker,
                timeBox,
                addDeleteRow,
                updateRefreshRow,
                statusRow,
                message
        );
        addDeleteRow.setFillHeight(true);
        updateRefreshRow.setFillHeight(true);
        statusRow.setFillHeight(true);
        HBox filters = new HBox(15, new Label("Status:"), statusFilter);
        HBox layout = new HBox(25, table, formBox);
        VBox root = new VBox(15, sectionTitle, filters, searchField, layout);
        root.setPadding(new Insets(15));
        UIStyle.page(root);
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox wrapper = new VBox(scrollPane);

        return wrapper;
    }

    private static ObservableList<PatientRow> loadPatientsFromBackend() {
        ObservableList<PatientRow> patients = FXCollections.observableArrayList();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to load patients: " + response.body());
                return patients;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode users;

            if (root.isArray()) {
                users = root;
            } else {
                users = root.path("data");
            }

            if (users.isArray()) {
                for (JsonNode user : users) {
                    String role = user.path("role").asText();

                    if ("PATIENT".equalsIgnoreCase(role)) {
                        patients.add(new PatientRow(
                                user.path("id").asInt(),
                                user.path("name").asText(),
                                user.path("username").asText(),
                                user.path("email").asText()
                        ));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return patients;
    }

    private static ObservableList<DoctorRow> loadDoctorsFromBackend() {
        ObservableList<DoctorRow> doctors = FXCollections.observableArrayList();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to load doctors: " + response.body());
                return doctors;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode users = root.isArray() ? root : root.path("data");

            if (users.isArray()) {
                for (JsonNode user : users) {
                    String role = user.path("role").asText();

                    if ("DOCTOR".equalsIgnoreCase(role)) {
                        doctors.add(new DoctorRow(
                                user.path("id").asInt(),
                                user.path("name").asText(),
                                user.path("username").asText(),
                                user.path("email").asText()
                        ));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctors;
    }

    private static boolean deleteUserFromBackend(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users/" + username))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createUserInBackend(String username, String password, String name, String email, String role) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = """
                {
                  "username": "%s",
                  "password": "%s",
                  "name": "%s",
                  "email": "%s",
                  "role": "%s"
                }
                """.formatted(username, password, name, email, role);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users/create"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("CREATE USER status: " + response.statusCode());
            System.out.println("CREATE USER body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static ObservableList<AppointmentRow> loadAppointmentsFromBackend() {
        ObservableList<AppointmentRow> appointments = FXCollections.observableArrayList();

        try {
            ObservableList<PatientRow> patients = loadPatientsFromBackend();
            ObjectMapper mapper = new ObjectMapper();

            for (PatientRow patient : patients) {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments/user/" + patient.getUsername()))
                        .header("Authorization", "Bearer " + AppState.token)
                        .header("Content-Type", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
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

                            } catch (Exception ignored) {
                            }
                            String[] parts = dateTime.split("T");

                            appointments.add(new AppointmentRow(
                                    appt.path("appointmentId").asInt(),
                                    parts.length > 0 ? parts[0] : "",
                                    parts.length > 1 ? formatTimeForDisplay(parts[1]) : "",
                                    appt.path("patientName").asText(appt.path("patient").path("username").asText()),
                                    appt.path("doctorName").asText(appt.path("doctor").path("username").asText()),
                                    status
                            ));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
    private static boolean createAppointmentInBackend(String patientUsername, String doctorUsername, String dateTime) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = """
                {
                  "patientUsername": "%s",
                  "doctorUsername": "%s",
                  "dateTime": "%s"
                }
                """.formatted(patientUsername, doctorUsername, dateTime);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("CREATE APPOINTMENT status: " + response.statusCode());
            System.out.println("CREATE APPOINTMENT body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    private static void setTimeDropdowns(String displayTime, ComboBox<Integer> hourBox,
                                         ComboBox<String> minuteBox,
                                         ComboBox<String> amPmBox) {
        if (displayTime == null || displayTime.isBlank()) {
            return;
        }

        String[] mainParts = displayTime.trim().split(" ");
        String timePart = mainParts[0];
        String amPm = mainParts.length > 1 ? mainParts[1] : "AM";

        String[] timeParts = timePart.split(":");

        int hour = Integer.parseInt(timeParts[0]);
        String minute = timeParts[1];

        hourBox.setValue(hour);
        minuteBox.setValue(minute);
        amPmBox.setValue(amPm);
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

    private static boolean updateAppointmentInBackend(int appointmentId, String patientUsername, String doctorUsername, String dateTime) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = """
                {
                  "patientUsername": "%s",
                  "doctorUsername": "%s",
                  "dateTime": "%s"
                }
                """.formatted(patientUsername, doctorUsername, dateTime);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments/" + appointmentId))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("UPDATE APPOINTMENT status: " + response.statusCode());
            System.out.println("UPDATE APPOINTMENT body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

            System.out.println("CANCEL APPOINTMENT status: " + response.statusCode());
            System.out.println("CANCEL APPOINTMENT body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean deleteAppointmentInBackend(int appointmentId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/appointments/" + appointmentId))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("DELETE APPOINTMENT status: " + response.statusCode());
            System.out.println("DELETE APPOINTMENT body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean toggleAppointmentStatusInBackend(int appointmentId) {
        try {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "http://localhost:8080/maclogixapi/v1/appointments/"
                                    + appointmentId + "/toggle-status"))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            return response.statusCode() >= 200
                    && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean updateUserInBackend(String oldUsername, String username, String name, String email, String password, String role) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json;

            if (password == null || password.isBlank()) {
                json = """
                    {
                      "username": "%s",
                      "name": "%s",
                      "email": "%s",
                      "role": "%s"
                    }
                    """.formatted(username, name, email, role);
            } else {
                json = """
                    {
                      "username": "%s",
                      "name": "%s",
                      "email": "%s",
                      "password": "%s",
                      "role": "%s"
                    }
                    """.formatted(username, name, email, password, role);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/maclogixapi/v1/users/" + oldUsername))
                    .header("Authorization", "Bearer " + AppState.token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("UPDATE USER status: " + response.statusCode());
            System.out.println("UPDATE USER body: " + response.body());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}