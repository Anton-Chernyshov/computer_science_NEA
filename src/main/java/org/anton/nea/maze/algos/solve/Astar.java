package org.anton.nea.maze.algos.solve;

import org.anton.nea.maze.*;
import java.util.*;

public class Astar extends MazeSolver {
        // stores cells in the order they were explored (to animate with)
        private final List<Cell> exploredOrder = new ArrayList<>();

        @Override
        public List<Cell> solve(Cell[][] grid, int rows, int cols) {

            exploredOrder.clear();
            Cell start = grid[0][0];
            Cell goal = grid[rows - 1][cols - 1];
            Map<Cell, Integer> dist = new HashMap<>();
            Map<Cell, Cell> cameFrom = new HashMap<>();


            PriorityQueue<Cell> pq = new PriorityQueue<>(
                    Comparator.comparingInt(c -> dist.get(c) + (int)(Math.pow(getHeuristicValue(c,goal), 2))
            ));

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    dist.put(grid[r][c], Integer.MAX_VALUE);
                }
            }

            dist.put(start, 0);
            pq.add(start);
            Set<Cell> visited = new HashSet<>();
            while (!pq.isEmpty()) {
                Cell current = pq.poll();
                if (!visited.add(current)) {
                    continue; // this fuckass fix took 2 hrs. 1 LINE OF CODE to ensure its the same
                }
                exploredOrder.add(current);
                if (current == goal) {return reconstructPath(cameFrom, current);}
                for (Cell neighbor : getNeighbors(current, grid, rows, cols)) {
                    int newDist = dist.get(current) + 1; // 1 cost per move
                    if (newDist < dist.get(neighbor)) {
                        dist.put(neighbor, newDist);
                        cameFrom.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }

            return Collections.emptyList();
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

            if (r < 0 || r >= rows || c < 0 || c >= cols) {
                throw new IllegalStateException("cell coordinates out of bounds: r=" + r + ", c=" + c);
            }

            if (!cell.hasUp() && r > 0) neighbors.add(grid[r - 1][c]);
            if (!cell.hasDown() && r < rows - 1) neighbors.add(grid[r + 1][c]);
            if (!cell.hasLeft() && c > 0) neighbors.add(grid[r][c - 1]);
            if (!cell.hasRight() && c < cols - 1) neighbors.add(grid[r][c + 1]);

            return neighbors;
        }

    /**
     *
     * @return the heuristic value of the current cell, which is the Manhattan distance between the current cell and the goal cell.
     */
    private int getHeuristicValue(Cell current, Cell goal){
            return Math.abs(current.getRow() - goal.getRow()) + Math.abs(current.getCol() - goal.getCol());
        }


        // expose the order of exploration for animations
        @Override
        public List<Cell> getExploredOrder() {
            return exploredOrder;
        }
    }

