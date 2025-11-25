package org.anton.nea.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.control.*;
import java.io.File;
import java.util.List;
import java.util.Random;

public class MusicPlayer {
    private final String defaultMusicFolder = "resources/org/anton/nea/music";
    private MediaPlayer mediaPlayer;
    private List<String> playlist;
    private int currentIndex = 0;
    private boolean loop = false;
    private boolean shuffle = false;
    private final Random random = new Random();

    private final Button playButton;
    private final Button pauseButton;
    private final Button nextButton;
    private final Button loopButton;
    private final Button shuffleButton;
    private final Slider progressSlider;
    private final Label timeLabel;

    public MusicPlayer(List<String> playlist,
                       Button playButton, Button pauseButton, Button nextButton,
                       Button loopButton, Button shuffleButton,
                       Slider progressSlider, Label timeLabel) {
        this.playlist = playlist;
        this.playButton = playButton;
        this.pauseButton = pauseButton;
        this.nextButton = nextButton;
        this.loopButton = loopButton;
        this.shuffleButton = shuffleButton;
        this.progressSlider = progressSlider;
        this.timeLabel = timeLabel;

        if (!playlist.isEmpty()) {
            loadMedia(currentIndex);
        }
        setupControls();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mediaPlayer != null) {
                    Duration current = mediaPlayer.getCurrentTime();
                    Duration total = mediaPlayer.getTotalDuration();
                    if (total != null && !progressSlider.isValueChanging()) {
                        progressSlider.setValue(current.toSeconds());
                    }
                    updateTimeLabel(current, total);
                }
            }
        };
        timer.start();
    }
    private void setupControls() {
        playButton.setOnAction(e -> play());
        pauseButton.setOnAction(e -> pause());
        nextButton.setOnAction(e -> next());

        loopButton.setOnAction(e -> {
            loop = !loop;
            loopButton.setStyle(loop ? "-fx-background-color: lightgreen" : "");
        });

        shuffleButton.setOnAction(e -> {
            shuffle = !shuffle;
            shuffleButton.setStyle(shuffle ? "-fx-background-color: lightgreen" : "");
        });

        progressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (progressSlider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(newVal.doubleValue()));
            }
        });
    }
    private void loadMedia(int index ) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(new File(playlist.get(index)).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void next() {
        if (playlist.isEmpty()) return;

        if (shuffle) {
            currentIndex = random.nextInt(playlist.size());
        } else {
            currentIndex = (currentIndex + 1) % playlist.size();
        }
        loadMedia(currentIndex);
        play();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    private void updateTimeLabel(Duration current, Duration total) {
        if (current == null || total == null) return;

        int curMin = (int) current.toMinutes();
        int curSec = (int) current.toSeconds() % 60;
        int totMin = (int) total.toMinutes();
        int totSec = (int) total.toSeconds() % 60;

        timeLabel.setText(String.format("%02d:%02d / %02d:%02d", curMin, curSec, totMin, totSec));
    }

    /**
     * right so this is the NEW playlist function, for loading from a folder directly
     */
    public void setPlaylist(String folderPath) {
        if (folderPath == null || folderPath.isEmpty()) return;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        playlist.clear();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                playlist.add(file.getAbsolutePath());
            }
        }

        currentIndex = 0;
        progressSlider.setValue(0);
        timeLabel.setText("00:00 / 00:00");
        loadMedia(currentIndex);
    }

    /**
     * Call setPlaylist with defaultParams.
     */
    public void setPlaylist(){
        setPlaylist(defaultMusicFolder);
    }

    /**
    Loads playlist (a list of file paths)
     */
    @Deprecated
    public void setPlaylist(List<String> newPlaylist) {

        if (newPlaylist == null || newPlaylist.isEmpty()) return;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        playlist.clear();
        playlist.addAll(newPlaylist);
        currentIndex = 0;
        progressSlider.setValue(0);
        timeLabel.setText("00:00 / 00:00");
        loadMedia(currentIndex);
    }

}
