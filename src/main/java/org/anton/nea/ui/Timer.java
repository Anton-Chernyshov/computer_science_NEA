package org.anton.nea.ui;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class Timer {
    private long startTime = 0;
    private long elapsedTime = 0;
    private boolean running = false;

    private final Label timerLabel;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            elapsedTime = System.currentTimeMillis() - startTime;
            timerLabel.setText(getFormattedTime());
        }
    };
    public Timer (Label label){
        this.timerLabel = label;
        this.timerLabel.setText("00:00:000");
    }
    // Start counting
    public void start() {
        if (!running) {
            startTime = System.currentTimeMillis() - elapsedTime;
            timer.start();
            running = true;
        }
    }

    // Stop counting
    public void stop() {
        if (running) {
            timer.stop();
            running = false;
        }
    }

    // Reset timer
    public void reset() {
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
    }

    // Get elapsed time in milliseconds
    public long getElapsedMillis() {
        return elapsedTime;
    }

    // Formatted as mm:ss:SSS
    public String getFormattedTime() {
        long millis = elapsedTime;
        long minutes = millis / 60000;
        millis %= 60000;
        long seconds = millis / 1000;
        millis %= 1000;
        return String.format("%02d:%02d:%03d", minutes, seconds, millis);
    }
}
