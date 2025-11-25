package org.anton.nea.controllers;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.anton.nea.io.*;
import org.anton.nea.maze.Player;
import org.anton.nea.ui.MusicPlayer;
import org.anton.nea.util.Color2;
import org.anton.nea.helpers.OnStartup;
import org.anton.nea.maze.Config;
import org.anton.nea.maze.algos.gen.*;
import org.anton.nea.maze.algos.solve.MazeSolver;
import org.anton.nea.ui.ErrorWindow;
import org.anton.nea.maze.GameBoard;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private MusicPlayer musicPlayer;
    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void loadMain() {
        try { // this is for loading controller drivers
            System.out.println(OnStartup.getOS());
            if (OnStartup.getOS() == 0){System.load(new File("SDL2.dll").getAbsolutePath());} // fuckin external stuff
            else if (OnStartup.getOS() == 2){
                System.out.println("Linux users, make sure to install SDL2 if u want controller support pls :)) ");
            }
            else{
                TimeUnit.SECONDS.sleep(9999999); // punish user for using macOS
                throw new RuntimeException("mac user");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anton/nea/main.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Antons Maze Game");
            stage.centerOnScreen();
            stage.show();
            Platform.runLater(() -> stage.setMaximized(true));

            // load configs from files
            Config config = Config.getInstance();
            OnStartup.RunOnStartup(scene); // sets theme based on OS default
            Timer timer = new Timer(timerLabel);
            // Movement handlers
            handleGenerateNewButtonAction(null); // draws maze on startup

            Player player = new Player(gameCanvas, gameCanvas.getCellSize()/2, gameCanvas.getCellSize()/2);
            MovementHandler handler = new WASDHandler(gameCanvas, player, scene, hasAccelerationCheckbox);
            gameCanvas.setMovementHandler(handler);
            handler.startTimer();

//            CursorHandler.cursorListener(gameCanvas, timer, colorPickerWall);


            // make text fields limited on input
            TextFieldController.makeHexField(seedValue);
            TextFieldController.makeNumberField(animationSpeedMS);
             // TODO: redo music loading to be handled by creating a new MusicPlayer class
            List<String> songs = new ArrayList<>(); // this all does music loading.

                File folder = new File(Objects.requireNonNull(getClass().getResource("/org/anton/nea/music/")).getPath());
                if (folder.exists() && folder.isDirectory()) {
                    File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                    if (files != null) {
                        for (File file : files) {
                            songs.add(file.getAbsolutePath());
                        }
                    }
                }



            System.out.println("Loaded songs: " + songs.size());
            songs.forEach(System.out::println);



            musicPlayer = new MusicPlayer(
                    songs, playButton, pauseButton, nextButton,
                    loopButton, shuffleButton, progressSlider, timeLabel
            );
            musicPlayer.setPlaylist();


            stage.setOnCloseRequest(event -> {
                cleanup();
            });

            /*
            THESE ARE JUST FOR FUN, PROBABLY GONNA REMOVE THIS LATER
            ################################################################################################
             */
            // make custom cursor

            Image cursorImage = new Image( Objects.requireNonNull(getClass().getResourceAsStream("/org/anton/nea/cursor.png")));
            ImageCursor imageCursor = new ImageCursor(cursorImage,0,0);
            try {
                wscModeCheckbox.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                    if (isSelected) {
                    /*
                    CHANGE MODE!!!
                     */
                        scene.getStylesheets().clear();
                        scene.getStylesheets().add("/org/anton/nea/wsc.css");
                        root.setCursor(imageCursor);
                        musicPlayer.pause();
                        List<String> s = new ArrayList<>();
                        File f = new File(getClass().getResource("/org/anton/nea/wsc/").getPath());
                        gameCanvas.updateGrid(new Color2(Color.BLACK, new Color(0.2, 0, 0.2, 0)));

                        if (f.exists() && f.isDirectory()) {
                            File[] files = f.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                            if (files != null) {
                                for (File file : files) {
                                    s.add(file.getAbsolutePath());
                                }
                            }
                        }
                        musicPlayer.setPlaylist(s);
                        musicPlayer.play();
                    } else {
                        if (folder.exists() && folder.isDirectory()) {
                            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                            if (files != null) {
                                for (File file : files) {
                                    songs.add(file.getAbsolutePath());
                                }
                            }
                        }
                        scene.getStylesheets().clear();
                        OnStartup.SetCssTheme(scene);
                        root.setCursor(Cursor.DEFAULT);
                        musicPlayer.pause();
                        musicPlayer.setPlaylist(songs);
                        musicPlayer.play();
                        gameCanvas.updateGrid(new Color2(Color.BLACK, Color.WHITE));
                    }
                });
                barneyModeCheckbox.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                    if (isSelected) {
                    /*
                    CHANGE MODE!!!
                     */
                        scene.getStylesheets().clear();
                        scene.getStylesheets().add("/org/anton/nea/barney.css");
                        root.setCursor(imageCursor);
                        musicPlayer.pause();
                        List<String> s = new ArrayList<>();
                        File f = new File(getClass().getResource("/org/anton/nea/barney/").getPath());
                        gameCanvas.updateGrid(new Color2(Color.BLACK, new Color(0.2, 0, 0.2, 0)));

                        if (f.exists() && f.isDirectory()) {
                            File[] files = f.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                            if (files != null) {
                                for (File file : files) {
                                    s.add(file.getAbsolutePath());
                                }
                            }
                        }
                        musicPlayer.setPlaylist(s);
                        musicPlayer.play();
                    } else {
                        if (folder.exists() && folder.isDirectory()) {
                            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                            if (files != null) {
                                for (File file : files) {
                                    songs.add(file.getAbsolutePath());
                                }
                            }
                        }
                        scene.getStylesheets().clear();
                        OnStartup.SetCssTheme(scene);
                        root.setCursor(Cursor.DEFAULT);
                        musicPlayer.pause();
                        musicPlayer.setPlaylist(songs);
                        musicPlayer.play();
                        gameCanvas.updateGrid(new Color2(Color.BLACK, Color.WHITE));
                    }
                });
            }
            catch (NullPointerException e) {ErrorWindow.show(e);}

            /*
            ################################################################################################
            ################################################################################################
             */



        } catch (Exception e) {
            Platform.runLater(() -> {
                ErrorWindow.show(e);
            });
        }
    }
    private void cleanup(){
        if(gameCanvas.getMovementHandler() instanceof ControllerHandler handler){
            handler.shutdown();
        }
    }
    // Text Areas and dropdowns
    @FXML private ColorPicker colorPickerWall;
    @FXML private ColorPicker colorPickerBackground;
    @FXML private TextField seedValue;
    @FXML private TextField animationSpeedMS;
    @FXML private ComboBox<String> algorithmChoiceBox;
    @FXML private ComboBox<String> solveAlgoChoiceBox;
    @FXML private CheckBox hasAccelerationCheckbox;
    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private Button nextButton;
    @FXML private Slider progressSlider;
    @FXML private Label timeLabel;
    @FXML private Button shuffleButton;
    @FXML private Button loopButton;
    @FXML private CheckBox wscModeCheckbox;
    @FXML private CheckBox barneyModeCheckbox;
    @FXML private Slider widthSlider;
    @FXML private Slider heightSlider;
    @FXML private Slider boxSizeSlider;
    @FXML private TextField widthValue;
    @FXML private TextField heightValue;
    @FXML private TextField boxSizeValue;




    // BUTTONS vvvvvv
    /**
     * Mapping string to generator object type
     */
    private static final Map<String, Supplier<MazeGenerator>> MazeGenerator = Map.of(
            "Prims", Prims::new,
            "Antons", Antons::new,
            "RecursiveBacktrack", RecursiveBacktrack::new,
            "Randomizer", Randomizer::new
    );
    private static final Map<String, Supplier<MazeSolver>> MazeSolver = Map.of(
      "Astar", org.anton.nea.maze.algos.solve.Astar::new,
        "Dijkstra", org.anton.nea.maze.algos.solve.Dijkstra::new
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
    private MazeSolver stringToAlgoGenerator(String algoString){
        try{
            Supplier<MazeSolver> generator = MazeSolver.get(algoString);
            if (generator == null){
                throw new IllegalArgumentException("Unknown algorithm: " + algoString + "\n How did this happen???");
            }
            return generator.get();
        } catch (Exception e) {
            ErrorWindow.show(e);
            return null;
        }
    }
    /*
    BUTTON HANDLERS
     */
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
    @FXML
    private void handleSolveButtonAction(ActionEvent event) {
        gameCanvas.showSolvedMaze(
                Objects.requireNonNull(stringToAlgoGenerator(solveAlgoChoiceBox.getValue())), new Color2(colorPickerWall.getValue(), colorPickerBackground.getValue()), Integer.parseInt(animationSpeedMS.getText()));
    }




}