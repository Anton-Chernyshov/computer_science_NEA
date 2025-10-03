package org.anton.nea.maze;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.anton.nea.io.MovementHandler;
import org.anton.nea.util.Color2;
import org.anton.nea.util.Point2;
import org.anton.nea.maze.algos.gen.MazeGenerator;
import org.anton.nea.maze.algos.solve.Dijkstra;
import org.anton.nea.maze.algos.solve.MazeSolver;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class GameBoard extends Canvas {
    /** will always be 40 */
    private final int rows;
    /** always gonna be 70 */
    private final int cols;
    /**The actual maze cells, represented as a 2d array*/
    Cell[][] cellRepr;
    /** Cellsize is 20*/
    private final int cellSize;
    /** The color of the gameboard going {@link Color2#ColorA is wall}*/
    private Color2 color;




    /**
     * fuckass class that is the bane of my life. JKJK lol
     * <p>This class is where the game occurs, its a custom canvas subclass that does my special stuff, because i decided to do this
     * using a class that extends the {@link Canvas} INSTEAD of having some kind of helper or manager class that just works along side it. idk
     * i thought this might look cleaner, idk if it does</p>
     */

    public GameBoard() {
        // constructor

        /*
        the reason i have it all set out  AS IF I was gonna use this constructor with parameters is because i was,
         but then remembered that i was actually loading this using FXML, so i kinda dont need them.. ALSO this makes it easy asf to change stuff so im keeping it yay!!
        */
        // width is 1400, height is 800
        this.rows =80; // cells //20
        this.cols = 140;  //35
        this.cellSize = (int) (1400/this.cols); // px //40
        this.cellRepr = new Cell[rows][cols];

    }

    /**
     * generates a checkerboard according to the rows and cols passed upon {@link GameBoard} creation
     * @param mainColor Primary color for checkerboard
     * @param altColor  Secondary color for checkerboard
     */
    public void createTestGrid(Color mainColor, Color altColor) {
            // checkerboard bcs why not
            GameBoard gameCanvas = this;
            GraphicsContext gc = getGraphicsContext2D();
            int rows = this.rows;
            int cols = this.cols;

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
    /**
     * will store an array of pixel positions on the canvas, and do an O(1) lookup to check. ooh what a great idea anton you are so fucking clever
     * haven't implemented the actual "array" yet so im not doing anything yet
     */
    public static Color getMousePixel(double mouseX, double mouseY) {

        throw new UnsupportedOperationException();
    }

    public Cell[][] getCellRepr() {return cellRepr;}
    public int getRows() {return rows;}
    public int getCols() {return cols;}
    public int getCellSize() {return cellSize;}
    public Color2 getColor() {return color;}
    public Color getColorAt(int x, int y){
        // color at the FUCKING X Y PIXEL NOT ROW
        // stoile this beautiful bit of code from my mouse handler
        WritableImage snapshot = new WritableImage((int)this.getWidth(), (int)this.getHeight());
        this.snapshot(null, snapshot);

        PixelReader reader = snapshot.getPixelReader();
        if (reader != null) {

            if (x >= 0 && x < snapshot.getWidth() && y >= 0 && y < snapshot.getHeight()) {
                return reader.getColor(x, y);

            }
        }
        return null; // out of range
    }
    private MovementHandler handler;
    public void setMovementHandler(MovementHandler handler) {this.handler = handler;}
    public MovementHandler getMovementHandler() {return handler;}
    /**
     * Fills the whole cellRepr with the cellValue
     * @param cellValue a value from 0x0 -> 0xF ( representing 4 bits UDLR )
     */
    public void fillCellRepr(int cellValue){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // and remeber that col is x and row is y
                this.cellRepr[row][col] = new Cell(cellValue, col*20, row*20, this.cellSize);
            }
        }
    }

    /**
     * Since the grid is a 1400*800px grid, im splitting it into a 70*40 grid of 20x20 squares.
     * this fills the grid with 'Max' Cells (0xF)
     */

    public void fillGrid(int cellValue, Color2 cellColors) {
        // Fill cellRepr with 0xF cells
        fillCellRepr(cellValue);
        updateGrid(cellColors);
    }

    /**
     * updates grid based of the array cellRepr
     * @param cellColors Color2 color for the cell
     */
    public void updateGrid(Color2 cellColors){
        this.color = cellColors;
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                this.cellRepr[row][col].draw(getGraphicsContext2D(), cellColors);
            }
        }
        drawStartAndEndPoints(new Color2(cellColors.ColorA, Color.GREEN), new Color2(cellColors.ColorA, Color.RED));

    }



    public void drawPixel(int x, int y, Color color){
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x, y, 1, 1);
    }

    public void drawMaze(MazeGenerator generator, long seed) {
        generator.generateMaze(this, seed);

        updateGrid(new Color2(Color.BLACK, Color.WHITE));
    }

    /**
     * Take a guess at what this function does? wild guess. It takes the background color and wall color and picks a "contrasting" color
     */
    public void drawStartAndEndPoints(Color2 startColor, Color2 endColor) {
        // Im gonna *try* to make this scale, but idk
        this.cellRepr[0][0].draw(getGraphicsContext2D(), startColor);
        // fuckass statement to get bottom right
        this.cellRepr[this.cellRepr.length - 1][this.cellRepr[this.cellRepr.length - 1].length -1].draw(getGraphicsContext2D(), endColor);
    }

    public void showSolvedMaze(MazeSolver solver, Color2 colors, int animationSpeed){
        List<Cell> solution = solver.solve(this.cellRepr, this.rows, this.cols);
        AnimationRenderer.showSolvingAnimation(this, solver, animationSpeed);


    }




}