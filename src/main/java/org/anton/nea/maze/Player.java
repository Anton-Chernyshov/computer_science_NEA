package org.anton.nea.maze;
import javafx.scene.paint.Color;
import org.anton.nea.util.HelperFuncs;
import org.anton.nea.util.Point2;
import org.anton.nea.util.Vector2;
import java.lang.Math;
public class Player {
    private final GameBoard gameboard;
    private double x;
    private double y;
    private double rotation; // in degrees, 0 is up, 90 is right, etc.

    private Color[][] characterDefinition = {
            {null, null, null, null, null, null, null},
            {null, null, null, Color.BLACK, null, null, null},
            {null, null, Color.BLACK, Color.BLACK, Color.BLACK, null, null},
            {null, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK , null},
            {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK},
            {null, null, Color.BLACK, Color.BLACK, Color.BLACK, null, null},
            {null, null, null, null, null, null, null},
    };
    private int[][] characterLocations = {
            {1, 3},
            {2, 2}, {2, 3}, {2, 4},
            {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5},
            {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6},
            {5, 2}, {5, 3}, {5, 4}, {5, 5}
    };

    /**
     * generates a player
     * @param gameboard
     * @param x the RAW X COORD ON THE CANVAS
     * @param y the Y coord on the canvas
     */
    public Player(GameBoard gameboard, int x, int y){
        this.gameboard = gameboard;
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }
    public void setGraphicObject(Color[][] characterDefinition){
        this.characterDefinition = characterDefinition;
    }
    public double getX() {return x;}
    public double getY() {return y;}
    public double getRotation() {return rotation;}


    /**
     * sets the X of the player, also clamps it to the board dimensions
     * @param x the x coord ??? what did u think it meant silly 😛
     */
    private void setX(int x) { // barney says he wants to record me havin sex. what a weirdo
        // and justin leung does too
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

    /**
     *
     * @param rotation in degrees clockwise from the vertical
     */
    private void setRotation(double rotation) {
        /* now because i did this a *bit* weirdly, i need to convert the rotation
        from clockwise from vert to anticlock from horizontal */

        double phi = 90-rotation;
        if (phi < 0) {phi = 360 + phi;}
        double theta = Math.toRadians(phi);
        double[][] rotmat = HelperFuncs.getRotationalMatrix(theta);



        /* do the actual rotation thingy */

        if (rotation < 0) rotation = 0;
        if (rotation > 360) rotation = 360;
        this.rotation = rotation;


    }
    /**
     * player is represented as an arrow on the board
     * this just draws him at his X, Y
     */
    public void updatePlayer(){
        gameboard.updateGrid(gameboard.getColor());
    // draw player on the board
        int cellSize = this.gameboard.getCellSize();
        for (int i = 0; i < characterDefinition.length; i++) {
            for (int j = 0; j < characterDefinition[i].length; j++) {
                if (characterDefinition[i][j] != null) {
                    int drawX = (int)x+j;
                    int drawY = (int)y+i;
                    gameboard.drawPixel(drawX, drawY, characterDefinition[i][j]);
                    // TO DO  FIX THIS SHIT SINCE RIGHT NOW ITS O(55MILLION) OPERATIONS PER FRAME
                    // PER       FRAME. @60FPS
                    // WHAT THE FUCK WAS I COOKING
                    /*
                    if (!checkCollisionAt(drawX, drawY)) {
                        gameboard.drawPixel(drawX, drawY, characterDefinition[i][j]);

                    } else {
                        // handle collision (stop movement, reset, etc)
                        handleCollision(drawX, drawY);
                    }
                    */
                }

            }
        }
    }
    public void moveToPoint(Point2 point){

        setX(point.x);
        setY(point.y);
        updatePlayer();
    }
    public void moveVector(Vector2 vector){
        x += vector.x;
        y += vector.y;
        setX( (int) (x) );
        setY((int)(y));
        updatePlayer();
    }

    public void rotate(int degrees){
        setRotation(rotation + degrees);
        updatePlayer();
    }
    private boolean checkCollisionAt(int drawX, int drawY) {
        Color boardColor = gameboard.getColorAt(drawX, drawY);
        /*
                 No switches?
        ⠀⣞⢽⢪⢣⢣⢣⢫⡺⡵⣝⡮⣗⢷⢽⢽⢽⣮⡷⡽⣜⣜⢮⢺⣜⢷⢽⢝⡽⣝
        ⠸⡸⠜⠕⠕⠁⢁⢇⢏⢽⢺⣪⡳⡝⣎⣏⢯⢞⡿⣟⣷⣳⢯⡷⣽⢽⢯⣳⣫⠇
        ⠀⠀⢀⢀⢄⢬⢪⡪⡎⣆⡈⠚⠜⠕⠇⠗⠝⢕⢯⢫⣞⣯⣿⣻⡽⣏⢗⣗⠏⠀
        ⠀⠪⡪⡪⣪⢪⢺⢸⢢⢓⢆⢤⢀⠀⠀⠀⠀⠈⢊⢞⡾⣿⡯⣏⢮⠷⠁⠀⠀
        ⠀⠀⠀⠈⠊⠆⡃⠕⢕⢇⢇⢇⢇⢇⢏⢎⢎⢆⢄⠀⢑⣽⣿⢝⠲⠉⠀⠀⠀⠀
        ⠀⠀⠀⠀⠀⡿⠂⠠⠀⡇⢇⠕⢈⣀⠀⠁⠡⠣⡣⡫⣂⣿⠯⢪⠰⠂⠀⠀⠀⠀
        ⠀⠀⠀⠀⡦⡙⡂⢀⢤⢣⠣⡈⣾⡃⠠⠄⠀⡄⢱⣌⣶⢏⢊⠂⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⠀⢝⡲⣜⡮⡏⢎⢌⢂⠙⠢⠐⢀⢘⢵⣽⣿⡿⠁⠁⠀⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⠀⠨⣺⡺⡕⡕⡱⡑⡆⡕⡅⡕⡜⡼⢽⡻⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⠀⣼⣳⣫⣾⣵⣗⡵⡱⡡⢣⢑⢕⢜⢕⡝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⣴⣿⣾⣿⣿⣿⡿⡽⡑⢌⠪⡢⡣⣣⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⡟⡾⣿⢿⢿⢵⣽⣾⣼⣘⢸⢸⣞⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        ⠀⠀⠀⠀⠁⠇⠡⠩⡫⢿⣝⡻⡮⣒⢽⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
        */

        if (boardColor.equals(Color.RED)) {
            // win
            return true;
        }
        if (boardColor.equals(Color.BLACK)) {
            // wall
            return true;
        }
        return false;
    }
    private void handleCollision(int drawX, int drawY) {
        //
    }
}
