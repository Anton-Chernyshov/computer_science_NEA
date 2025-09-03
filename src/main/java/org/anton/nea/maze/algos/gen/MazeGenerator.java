package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

public abstract class MazeGenerator {
    public abstract void generateMaze(GameBoard gameBoard, long seed);
}
