package edu.secourse.patientportal.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppState {
    public static final ObservableList<PatientRow> patients = FXCollections.observableArrayList();
    public static final ObservableList<DoctorRow> doctors = FXCollections.observableArrayList();
    public static final ObservableList<AppointmentRow> appointments = FXCollections.observableArrayList();

    static {
        patients.add(new PatientRow(1, "John Smith", "john123", "john@gmail.com"));
        patients.add(new PatientRow(2, "Jane Doe", "jane456", "jane@gmail.com"));

        doctors.add(new DoctorRow(1, "Dr Adams", "adams01", "adams@clinic.com"));
        doctors.add(new DoctorRow(2, "Dr Brown", "brown02", "brown@clinic.com"));

        appointments.add(new AppointmentRow(1, "2026-03-26", "10:00 AM", "John Smith", "Dr Adams", "ACTIVE"));
        appointments.add(new AppointmentRow(2, "2026-03-27", "02:00 PM", "Jane Doe", "Dr Brown", "ACTIVE"));
    }

    private AppState() {
    }
}