package edu.secourse.patientportal.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;

public class UIStyle {

    public static void page(VBox root) {
        root.setStyle("""
        -fx-background-color: #1e1e1e;
    """);
    }

    public static void darkTable(TableView<?> table) {

        table.setStyle("""
        -fx-background-color: #2b2b2b;
        -fx-control-inner-background: #2b2b2b;
        -fx-table-cell-border-color: #3c3f41;
        -fx-table-header-border-color: #3c3f41;
        -fx-text-background-color: white;
        -fx-selection-bar: #4a90e2;
        -fx-selection-bar-text: white;
    """);
    }

    public static void darkTextField(Control control) {

        control.setStyle("""
        -fx-background-color: #2b2b2b;
        -fx-text-fill: white;
        -fx-prompt-text-fill: #999999;
        -fx-border-color: #555555;
    """);
    }

    public static void darkLabel(Labeled label) {

        label.setStyle("""
        -fx-text-fill: white;
    """);
    }
    public static void title(Label label) {
        label.setStyle("""
        -fx-font-size: 28px;
        -fx-font-weight: bold;
        -fx-text-fill: white;
    """);
    }

    public static void subtitle(Label label) {
        label.setStyle("""
        -fx-font-size: 16px;
        -fx-text-fill: #d1d5db;
    """);
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
    public static void darkTableFull(TableView<?> table) {
        table.setStyle("""
        -fx-background-color: #1e1e1e;
        -fx-control-inner-background: #1e1e1e;
        -fx-table-cell-border-color: #444444;
        -fx-table-header-border-color: #444444;
        -fx-text-background-color: white;
        -fx-selection-bar: #374151;
        -fx-selection-bar-text: white;
    """);
    }
}