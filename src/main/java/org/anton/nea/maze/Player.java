package org.anton.nea.maze;
import javafx.scene.paint.Color;
import org.anton.nea.util.Point2;
import org.anton.nea.util.Vector2;

public class Player {
    private final GameBoard gameboard;
    private int x;
    private int y;
    private int rotation; // in degrees, 0 is up, 90 is right, etc.
    private Color[][] characterDefinition = {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, Color.BLACK, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null, null},
            {null, null, null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null},
            {null, null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null},
            {null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null},
            {null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null},
            {null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null},
            {null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null},
            {null, null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null},
            {null, null, null, null, null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
    };


    public Player(GameBoard gameboard, int x, int y){
        this.gameboard = gameboard;
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }
    public void setGraphicObject(Color[][] characterDefinition){
        this.characterDefinition = characterDefinition;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getRotation() {return rotation;}
    /**
     * sets the X of the player, also clamps it to the board dimensions
     * @param x the x coord ??? what did u think it meant silly 😛
     */
    private void setX(int x) { // barney says he wants to record me havin sex. what a weirdo
        // and justin leung
        double maxWidth = gameboard.getWidth();
        if (x < 0) x = 0;
        if (x > maxWidth) x = (int)maxWidth;
        this.x = x;
    }
    private void setY(int y) {
        double maxHeight = gameboard.getHeight();
        if (y < 0) y = 0;
        if (y > maxHeight) y = (int)maxHeight;
        this.y = y;
    }

    private void setRotation(int rotation) {
        if (rotation < 0) rotation = 0;
        if (rotation > 360) rotation = 360;
        this.rotation = rotation;
    }
    /**
     * player is represented as an arrow on the board
     * this just draws him at his X, Y
     */
    public void updatePlayer(){
        // draw player on the board
        int cellSize = this.gameboard.getCellSize();
        for (int i = 0; i < characterDefinition.length; i++) {
            for (int j = 0; j < characterDefinition[i].length; j++) {
                gameboard.drawPixel(x+j, y+i, characterDefinition[i][j]);

            }
        }
    }
    public void moveToPoint(Point2 point){
        setX(point.x);
        setY(point.y);
        updatePlayer();
    }
    public void moveVector(Vector2 vector){
        setX(x + vector.x);
        setY(y + vector.y);
        updatePlayer();
    }

    public void rotate(int degrees){
        setRotation(rotation + degrees);
        updatePlayer();
    }
}
// 15x15 arrow