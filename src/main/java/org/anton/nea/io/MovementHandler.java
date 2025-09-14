package org.anton.nea.io;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;
import org.anton.nea.maze.Player;
import org.anton.nea.util.Vector2;

public class MovementHandler {
    private GameBoard gameBoard;
    private Player player;

    public MovementHandler(GameBoard gameBoard, Player player){
        this.gameBoard = gameBoard;
        this.player = player
        // this is just a constructor,
    }

    public Player getPlayer(){return this.player;}
    public void setPlayer(Player player){this.player = player;}

    public void moveVector(Vector2 vector){
        /*
        Right now what.
         */
        Cell[][] rizz = this.gameBoard.getCellRepr();
        int cellSize = this.gameBoard.getCellSize();
        int rows = this.gameBoard.getRows();
        int cols = this.gameBoard.getCols();
        this.player.
    }

}
