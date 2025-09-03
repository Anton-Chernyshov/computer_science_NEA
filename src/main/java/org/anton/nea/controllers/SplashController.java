package org.anton.nea.controllers;

import org.anton.nea.ui.ErrorWindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.IOException;


public class SplashController {
    private final Stage stage;
    private static final int splashShowLength = 1; // how long the splash screen is displayed for
    public SplashController(Stage stage) {
        this.stage = stage;
    }

    public void loadSplash(Runnable onFinished) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anton/nea/splash.fxml"));
            Parent root = loader.load();
            loader.setController(this); // required when manually injecting

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Loading Screen");
            stage.setMaximized(true);
            stage.setAlwaysOnTop(true); // launch on top
            stage.centerOnScreen();
            stage.show();
            stage.setAlwaysOnTop(false); // make it so the window can be properly tiled
            // Delay then call callback to move on
            PauseTransition delay = new PauseTransition(Duration.seconds(splashShowLength));
            delay.setOnFinished(e -> onFinished.run());
            delay.play();

        } catch (IOException e) {
            Platform.runLater(() -> {
                ErrorWindow.show(e);
            });
        }
    }


}
