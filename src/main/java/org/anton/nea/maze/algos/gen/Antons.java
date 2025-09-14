package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

import java.util.Random;
import java.util.Stack;

public class Antons extends MazeGenerator {
    @Override
    public void generateMaze(GameBoard gameBoard, long seed) {
        // Initialize all cells with all walls up (0xF)
        gameBoard.fillCellRepr(0xF);
        Cell[][] cellRepr = gameBoard.getCellRepr();
        int rows = gameBoard.getRows();
        int cols = gameBoard.getCols();
        int cellSize = gameBoard.getCellSize();


    }
}