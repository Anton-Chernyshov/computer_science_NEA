package org.anton.nea.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldController {
    public static void makeNumberField(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
    }
    public static void makeStringField(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(c -> c));
    }
    public static void makeHexField(TextField tf) {
        tf.setTextFormatter(new TextFormatter<>(c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[a-fA-f0-9]*")) { // letters and spaces only
                return c;
            }
            return null; // reject invalid input
        }));
    }
}
