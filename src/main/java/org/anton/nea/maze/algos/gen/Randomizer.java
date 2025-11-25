package org.anton.nea.maze.algos.gen;
import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

public class Randomizer extends MazeGenerator{
    @Override
    public void generateMaze(GameBoard gameBoard, long seed){

        retryMaze(gameBoard, seed);

    }

    private void retryMaze(GameBoard gameBoard, long seed){
        gameBoard.fillCellRepr(0xF);
        Cell[][] cellRepr = gameBoard.getCellRepr();
        int rows = gameBoard.getRows();
        int cols = gameBoard.getCols();
        int cellSize = gameBoard.getCellSize();
        Random rand = new Random(seed);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellRepr[i][j] = new Cell(rand.nextInt(10), j * cellSize, i * cellSize, cellSize);
                }
            }
        drawStartAndEndCells(cellRepr);
    }

    /**
     * Doubles up walls so players cant phase through them
     * @param gameboard
     */
    private void doubleUpWalls(GameBoard gameboard){
        Cell[][] cellRepr = gameboard.getCellRepr();
    }
}

