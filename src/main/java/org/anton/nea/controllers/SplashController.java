package org.anton.nea.controllers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static final int splashShowLength = 5; // how long the splash screen is displayed for
    public SplashController(Stage stage) {
        this.stage = stage;
    }
    @FXML
    Pane bg;
    public void loadSplash(Runnable onFinished) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anton/nea/splash.fxml"));
            loader.setController(this); // required when manually injecting

            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Loading Screen");
            stage.setMaximized(true);
            stage.setAlwaysOnTop(true); // launch on top
            stage.centerOnScreen();
            stage.show();
            stage.setAlwaysOnTop(false); // make it so the window can be properly tiled
            Canvas canvas = new Canvas(1900, 1200);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.LIGHTGRAY);
            gc.setFont(Font.font(36));

            for (int y = 0; y < 1200; y += 50) {
                for (int x = 0; x < 1900; x += 200) {
                    gc.fillText("I HATE NEA", x, y);
                }
            }

            bg.getChildren().add(0, canvas);
            // add at the back
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
