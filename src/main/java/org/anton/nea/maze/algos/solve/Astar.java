package org.anton.nea.maze.algos.solve;

import org.anton.nea.maze.*;

import java.util.*;

public class Astar extends MazeSolver {

    @Override
    public List<Cell> solve(Cell[][] grid, int rows, int cols) {
        Cell start = grid[0][0];
        Cell goal = grid[rows - 1][cols - 1]; // always max valid indices

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<Cell> openSetCells = new HashSet<>(); // track what's already in the queue
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Map<Cell, Integer> gScore = new HashMap<>();

        gScore.put(start, 0);
        Node startNode = new Node(start, heuristic(start, goal));
        openSet.add(startNode);
        openSetCells.add(start);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            Cell current = currentNode.cell;
            openSetCells.remove(current);

            if (current == goal) {
                return reconstructPath(cameFrom, current);
            }

            for (Cell neighbor : getNeighbors(current, grid, rows, cols)) {
                int tentativeG = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    int f = tentativeG + heuristic(neighbor, goal);
                    Node neighborNode = new Node(neighbor, f);

                    if (!openSetCells.contains(neighbor)) {
                        openSet.add(neighborNode);
                        openSetCells.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // no path found
    }

    private static int heuristic(Cell a, Cell b) {
        // Manhattan distance on row/col grid
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
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

        // Only add neighbors that are within bounds and not blocked by walls
        if (!cell.hasUp() && r > 0) neighbors.add(grid[r - 1][c]);
        if (!cell.hasDown() && r < rows - 1) neighbors.add(grid[r + 1][c]);
        if (!cell.hasLeft() && c > 0) neighbors.add(grid[r][c - 1]);
        if (!cell.hasRight() && c < cols - 1) neighbors.add(grid[r][c + 1]);

        return neighbors;
    }

    // Node class for priority queue
    private static class Node {
        Cell cell;
        int f; // f = g + h

        Node(Cell cell, int f) {
            this.cell = cell;
            this.f = f;
        }
    }
}
