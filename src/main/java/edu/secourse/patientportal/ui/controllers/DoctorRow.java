package edu.secourse.patientportal.ui.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DoctorRow {

    private final IntegerProperty doctorId;
    private final StringProperty name;
    private final StringProperty username;
    private final StringProperty email;

    public DoctorRow(int doctorId, String name, String username, String email) {
        this.doctorId = new SimpleIntegerProperty(doctorId);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
    }

    public int getDoctorId() {
        return doctorId.get();
    }

    public int getId() {
        return doctorId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public IntegerProperty doctorIdProperty() {
        return doctorId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }
}