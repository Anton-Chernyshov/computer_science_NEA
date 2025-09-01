package org.anton.nea.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

public class ErrorController {

    @FXML
    private TextArea errorMessage;
    @FXML
    private TextArea errorTrace;
    public void setMessage(String message) {
        errorMessage.setText(message);
    }
    public void setTraceStack(String trace) {
        errorTrace.setText(trace);
    }
    @FXML
    public void close() {
        Stage stage = (Stage) errorMessage.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void copy(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(errorTrace.getText());
        clipboard.setContent(content);
    }
    @FXML
    private void terminate(){
        Platform.exit();
    }
}