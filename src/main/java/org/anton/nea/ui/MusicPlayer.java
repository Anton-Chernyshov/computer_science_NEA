package org.anton.nea.ui;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private List<String> playlist;
    private int currentIndex = 0;
    private Button playButton = new Button("▶");
    private Button pauseButton = new Button("⏸");
    private Button nextButton = new Button("⏭");
    private Slider progressSlider = new Slider();
    private Label timeLabel = new Label("0:00 / 0:00");

    public MusicPlayer() {

    }



}
