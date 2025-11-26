package org.anton.nea.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import org.anton.nea.ui.ErrorWindow;
public class HomeController {
    private final Stage stage;
    private Runnable onStart;

    public HomeController(Stage stage) {
        this.stage = stage;
    }

    public void loadHome(Runnable onStart) {
        try {
            this.onStart = onStart;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anton/nea/home.fxml"));
            loader.setController(this);

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(getClass().getResource("/org/anton/nea/dark.css").toExternalForm());

            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            Platform.runLater(() -> {
                // ignore rn
                 ErrorWindow.show(e);
            });}
    }
    @FXML
    public void handleStartPressed() {
        if (onStart != null) {
            onStart.run();
        }
    }
}

