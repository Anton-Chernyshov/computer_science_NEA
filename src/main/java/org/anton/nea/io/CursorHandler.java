package org.anton.nea.io;

import javafx.scene.control.ColorPicker;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.anton.nea.maze.GameBoard;
import org.anton.nea.ui.Timer;

public class CursorHandler {
    private static Timer timer;
    public static void cursorListener(GameBoard gameCanvas, Timer t, ColorPicker colorPickerWall) {
        CursorHandler.timer = t;
        gameCanvas.setOnMouseMoved((MouseEvent e) -> {

            WritableImage snapshot = new WritableImage((int)gameCanvas.getWidth(), (int)gameCanvas.getHeight());
            gameCanvas.snapshot(null, snapshot);

            PixelReader reader = snapshot.getPixelReader();
            if (reader != null) {
                int x = (int)e.getX();
                int y = (int)e.getY();

                if (x >= 0 && x < snapshot.getWidth() && y >= 0 && y < snapshot.getHeight()) {
                    Color color = reader.getColor(x, y);
                    doStuffBasedOnColor(color, colorPickerWall);
                }
            }
        });
    }

    /**
     * This is what handles "winning" and losing, and controls the timer
     * @param color
     */
    private static void doStuffBasedOnColor(Color color, ColorPicker colorPickerWall) {
        //System.out.println(color);
        // colors are:
        // RED - STOP
        // GREEN - RESET -> START
        // BLACK - STOP
        switch (color.toString()) {
            case "0xff0000ff" -> { // RED (win)
                timer.stop();
                timer.HAPPYFACE();

            }

            case "0x008000ff" -> { // green ( start )
                timer.stop();
                timer.reset();
                timer.start();
            }
            default -> {
                if (colorPickerWall.getValue().toString().equals(color.toString()))  { // wall
                    // for context ^^^^^ i need to do this goofy thing because java switch statements need their values to be known at compile time, and i , well, dont.
                    timer.stop();
                }
            }
        }

    }
}
