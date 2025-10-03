package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

public abstract class MazeGenerator {
    public abstract void generateMaze(GameBoard gameBoard, long seed);

    public void drawStartAndEndCells(Cell[][] cellRepr){
        int rows = cellRepr.length;
        int cols = cellRepr[0].length;
        Cell startCell = cellRepr[0][0]; // top-left
        startCell = new Cell(startCell.getCell() & ~0x8, startCell.getX(), startCell.getY(), startCell.getSize()); // remove top wall
        cellRepr[0][0] = startCell;

        Cell endCell = cellRepr[rows-1][cols-1]; // bottom-right
        endCell = new Cell(endCell.getCell() & ~0x4, endCell.getX(), endCell.getY(), endCell.getSize()); // remove bottom wall
        cellRepr[rows-1][cols-1] = endCell;
    }
}
