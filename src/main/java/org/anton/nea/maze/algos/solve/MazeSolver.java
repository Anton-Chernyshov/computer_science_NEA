package org.anton.nea.maze.algos.solve;

import org.anton.nea.maze.Cell;

import java.util.List;

public abstract class MazeSolver {
    public abstract List<Cell> solve(Cell[][] grid, int rows, int cols);
}
