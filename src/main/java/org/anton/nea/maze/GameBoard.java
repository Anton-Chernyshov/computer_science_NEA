package org.anton.nea.maze;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import org.anton.nea.maze.Cell;
public class GameBoard extends Canvas {
    public GameBoard() {
        // constructor

        Cell[][] cellRepr = new Cell[40][60];

    }

    public void createGrid(Color mainColor, Color altColor) {
            // checkerboard bcs why not
            GameBoard gameCanvas = this;
            GraphicsContext gc = getGraphicsContext2D();
            int rows = 40;
            int cols = 70;

            double cellWidth = gameCanvas.getWidth() / cols;
            double cellHeight = gameCanvas.getHeight() / rows;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if ((row + col) % 2 == 0) gc.setFill(mainColor);
                    else {
                        gc.setFill(altColor);
                    }
                    gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                }
            }
    }

    public static Color getMousePixel(double mouseX, double mouseY) {
        // will store an array of pixel positions on the canvas, and do an O(1) lookup to check. ooh what a great idea anton you are so fucking clever
        // haven't implemented the actual "array" yet so im not doing anything yet
        throw new UnsupportedOperationException();
    }

    public void testGrid(){
        /*
        Since the grid is a 1400*900px grid, im splitting it into a 70*40 grid of 20x20 squares
         */

    }

    public void drawMaze(double seed){
        throw new UnsupportedOperationException();
    }


}