package edu.secourse.patientportal.ui.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientRow {

    private final IntegerProperty patientId;
    private final StringProperty name;
    private final StringProperty username;
    private final StringProperty email;

    public PatientRow(int patientId, String name, String username, String email) {
        this.patientId = new SimpleIntegerProperty(patientId);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
    }

    public int getPatientId() {
        return patientId.get();
    }

    public int getId() {
        return patientId.get();
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

    public IntegerProperty patientIdProperty() {
        return patientId;
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
    public void setName(String newName) {
        name.set(newName);
    }

    public void setUsername(String newUsername) {
        username.set(newUsername);
    }

    public void setEmail(String newEmail) {
        email.set(newEmail);
    }

}
