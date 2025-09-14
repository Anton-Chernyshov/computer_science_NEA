package org.anton.nea.maze;

import org.anton.nea.util.Color2;

import javafx.scene.canvas.GraphicsContext;

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

    /**
     * draws the cell??
     * <p>also quick rant. i spent longer than i would have wanted to (about 2 hrs ) trying to figure out why i was getting faint lines drawn on my canvas when filling it with 0x0 cells.
     * TURNS OUT, javafx does antialiasing on their canvases, and i only found out about this after reading a random stackoverflow comment saying "oh just disable antialiasing", but that caused some issues for me,
     * SOOOO. i decided to just stick to pixels. funny that eh</p>
     * @param gc pass the graphics context of the canvas
     * @param colors <p>tf do u think, {@code new Color2(Color.BLACK, Color.WHITE)}makes black walls white "background"</p>
     */
    public void draw(GraphicsContext gc, Color2 colors) {
        gc.setLineWidth(2);
        double half = gc.getLineWidth() / 2.0;

// fill the inside
        gc.setFill(colors.ColorB);
        gc.fillRect(x, y, size, size);
// strokin my walls
        gc.setStroke(colors.ColorA);

        if (hasUp())    gc.strokeLine(x + half, y + half, x + size - half, y + half);
        if (hasDown())  gc.strokeLine(x + half, y + size - half, x + size - half, y + size - half);
        if (hasLeft())  gc.strokeLine(x + half, y + half, x + half, y + size - half);
        if (hasRight()) gc.strokeLine(x + size - half, y + half, x + size - half, y + size - half);
    }
    public int getX(){return this.x;}
    public int getRow(){return this.y / this.size;}
    public int getCol(){return this.x / this.size;}
    public int getY(){return this.y;}
    public int getSize(){return this.size;}

}

