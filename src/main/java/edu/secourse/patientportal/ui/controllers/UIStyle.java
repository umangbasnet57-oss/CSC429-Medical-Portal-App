package edu.secourse.patientportal.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class UIStyle {

    public static void page(VBox root) {
        root.setStyle("-fx-background-color: #f4f7fb;");
    }

    public static void title(Label label) {
        label.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #17324d;");
    }

    public static void subtitle(Label label) {
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #475569;");
    }

    public static void primary(Button button) {
        button.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;");
    }

    public static void success(Button button) {
        button.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;");
    }

    public static void danger(Button button) {
        button.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;");
    }

    public static void table(TableView<?> table) {
        table.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cbd5e1;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}