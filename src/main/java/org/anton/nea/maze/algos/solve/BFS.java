package org.anton.nea.maze.algos.solve;

import org.anton.nea.maze.*;
import java.util.*;

public class BFS extends MazeSolver {

    @Override
    public List<Cell> solve(Cell[][] grid, int rows, int cols) {
        Cell start = grid[0][0];
        Cell goal = grid[rows - 1][cols - 1];

        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current == goal) {
                return reconstructPath(cameFrom, current);
            }

            for (Cell neighbor : getNeighbors(current, grid, rows, cols)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // no path found
    }

    private static List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell current) {
        List<Cell> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static List<Cell> getNeighbors(Cell cell, Cell[][] grid, int rows, int cols) {
        List<Cell> neighbors = new ArrayList<>();

        int r = cell.getRow();
        int c = cell.getCol();

        // sanity check
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new IllegalStateException("Cell coordinates out of bounds: r=" + r + ", c=" + c);
        }

        // only add valid neighbors (and if wall is open)
        if (!cell.hasUp() && r > 0) neighbors.add(grid[r - 1][c]);
        if (!cell.hasDown() && r < rows - 1) neighbors.add(grid[r + 1][c]);
        if (!cell.hasLeft() && c > 0) neighbors.add(grid[r][c - 1]);
        if (!cell.hasRight() && c < cols - 1) neighbors.add(grid[r][c + 1]);

        return neighbors;
    }
}
