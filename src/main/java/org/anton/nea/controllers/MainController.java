package org.anton.nea.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import org.anton.nea.maze.Config;
import org.anton.nea.ui.ErrorWindow;
import org.anton.nea.maze.GameBoard;
import java.util.Objects;
import org.anton.nea.maze.Cell;
import javafx.fxml.FXML;

public class MainController {
    private Stage stage;

    @FXML
    private GameBoard gameCanvas;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void loadMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anton/nea/main.fxml"));
            loader.setController(this); // required when manually injecting
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Antons Maze Game");
            stage.centerOnScreen();

            // Show first
            stage.show();

            // Then maximize after layout pass
            Platform.runLater(() -> stage.setMaximized(true));

            // load configs from files
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/anton/nea/globalStyle.css")).toExternalForm());
            Config config = Config.getInstance();
            gameCanvas.createGrid(config.getColor("maze.mainColor"), config.getColor("maze.altColor"));




        } catch (Exception e) {
            Platform.runLater(() -> {
                ErrorWindow.show(e);
            });
        }
    }
}

