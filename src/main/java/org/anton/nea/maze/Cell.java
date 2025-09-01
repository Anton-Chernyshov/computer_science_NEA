package org.anton.nea.maze;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Cell {
    private int cellValue = 0x0;
    private int x;
    private int y;
    private int size;
    public Cell(int cellValue, int x, int y, int size) {
       this.cellValue = cellValue;
       this.x = x;
       this.y = y;
       this.size = size;
    }

    public int getCell(){
        return cellValue;
    }

    // cell value is a number from 0x0 to 0xF , representing a 4 bit number UDLR ( showing which wall is active )
    // SO:
    // up = 0x8 down = 0x4 left 0x2 right 0x1
    // helper functions because I WILL forget this ( even if its so simple, for future readability and my sanity )
    // hope this makes sense for future anton
    public boolean hasUp(){return (cellValue & 0x8) != 0;}
    public boolean hasDown(){return (cellValue & 0x4) != 0;}
    public boolean hasLeft(){return (cellValue & 0x2) != 0;}
    public boolean hasRight(){return (cellValue & 0x1) != 0;}


    public void draw(GraphicsContext gc) {
        double w = gc.getLineWidth();       // line thickness (2px in your case)
        double half = w / 2.0;

        gc.setStroke(Color.RED);

        // Top wall (inside the square)
        if (hasUp()) {
            gc.strokeLine(x + half, y + half, x + size - half, y + half);
        }

        // Bottom wall
        if (hasDown()) {
            gc.strokeLine(x + half, y + size - half, x + size - half, y + size - half);
        }

        // Left wall
        if (hasLeft()) {
            gc.strokeLine(x + half, y + half, x + half, y + size - half);
        }

        // Right wall
        if (hasRight()) {
            gc.strokeLine(x + size - half, y + half, x + size - half, y + size - half);
        }
    }

}
