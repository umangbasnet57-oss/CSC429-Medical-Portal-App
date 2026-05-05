package edu.secourse.patientportal.ui.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppointmentRow {

    private final IntegerProperty appointmentId;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty patientName;
    private final StringProperty doctorName;
    private final StringProperty status;

    public AppointmentRow(int appointmentId, String date, String time,
                          String patientName, String doctorName, String status) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.status = new SimpleStringProperty(status);
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public int getId() {
        return appointmentId.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getPatientName() {
        return patientName.get();
    }

    public String getDoctorName() {
        return doctorName.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String newStatus) {
        status.set(newStatus);
    }

    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    public StringProperty doctorNameProperty() {
        return doctorName;
    }

    public StringProperty statusProperty() {
        return status;
    }
    public void setDate(String newDate) {
        date.set(newDate);
    }

    public void setTime(String newTime) {
        time.set(newTime);
    }

    public void setPatientName(String newPatientName) {
        patientName.set(newPatientName);
    }

    public void setDoctorName(String newDoctorName) {
        doctorName.set(newDoctorName);
    }
}