package org.anton.nea.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.anton.nea.helpers.Color2;
import org.anton.nea.helpers.OnStartup;
import org.anton.nea.maze.Config;
import org.anton.nea.maze.algos.gen.*;
import org.anton.nea.ui.CursorHandler;
import org.anton.nea.ui.ErrorWindow;
import org.anton.nea.maze.GameBoard;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import javafx.fxml.FXML;
import org.anton.nea.ui.Timer;

public class MainController {
    private final Stage stage;

    @FXML
    private GameBoard gameCanvas;
    @FXML
    private StackPane mazePane;
    @FXML
    private Label timerLabel;
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
            Config config = Config.getInstance();
            OnStartup.RunOnStartup(scene);
            Timer timer = new Timer(timerLabel);
            CursorHandler.cursorListener(gameCanvas, timer, colorPickerWall);

        } catch (Exception e) {
            Platform.runLater(() -> {
                ErrorWindow.show(e);
            });
        }
    }

    // Text Areas and dropdowns
    @FXML private ColorPicker colorPickerWall;
    @FXML private ColorPicker colorPickerBackground;
    @FXML private TextField seedValue;
    @FXML private ChoiceBox<String> algorithmChoiceBox;
    // BUTTONS vvvvvv
    /**
     * Mapping string to generator object type
     */
    private static final Map<String, Supplier<MazeGenerator>> MazeGenerator = Map.of(
            "Prims", Prims::new,
            "Antons", Antons::new,
            "RecursiveBacktrack", RecursiveBacktrack::new
    );
    /**
     * little helper function to convert string to algorithm type
     * @param algoString output from my choicebox
     */
    private MazeGenerator stringToGenerator(String algoString){
        try {
            Supplier<MazeGenerator> generator = MazeGenerator.get(algoString);
            if (generator == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + algoString + "\n How did this happen???");
            }
            return generator.get();
        } catch (Exception e) {
            ErrorWindow.show(e);
            return null;
        }
    }
    @FXML
    private void handleRenderButtonAction(ActionEvent event) {
        gameCanvas.drawMaze(
                Objects.requireNonNull(stringToGenerator(algorithmChoiceBox.getValue())), Long.parseUnsignedLong(seedValue.getText(), 16)
        );
        gameCanvas.updateGrid(
                new Color2(colorPickerWall.getValue(), colorPickerBackground.getValue())
        );
    }

    @FXML
    private void handleGenerateRandomSeed(ActionEvent event) {
        long seed = new Random().nextLong();
        seedValue.setText(Long.toHexString(seed));
    }
    @FXML
    private void handleGenerateNewButtonAction(ActionEvent event) {
        handleGenerateRandomSeed(event);
        handleRenderButtonAction(event);
    }

}




