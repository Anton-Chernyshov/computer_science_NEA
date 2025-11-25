package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Prims extends MazeGenerator {

    @Override
    public void generateMaze(GameBoard gameBoard, long seed) {
        int rows = gameBoard.getRows();
        int cols = gameBoard.getCols();
        int cellSize = gameBoard.getCellSize();
        Cell[][] cellRepr = gameBoard.getCellRepr();

        // Initialize all walls
        gameBoard.fillCellRepr(0xF);

        Random rand = new Random(seed);
        boolean[][] visited = new boolean[rows][cols];

        // Directions: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        int[] wallBit = {0x8, 0x4, 0x2, 0x1};

        // Priority queue: [weight, row, col, parentRow, parentCol, dir]
        PriorityQueue<int[]> frontier = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Start at a random cell
        int startRow = rand.nextInt(rows);
        int startCol = rand.nextInt(cols);
        visited[startRow][startCol] = true;

        // Add neighbors to frontier with random weights
        for (int i = 0; i < 4; i++) {
            int nr = startRow + dr[i];
            int nc = startCol + dc[i];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                int weight = rand.nextInt(100); // tweak for more or less twistiness
                frontier.add(new int[]{weight, nr, nc, startRow, startCol, i});
            }
        }

        while (!frontier.isEmpty()) {
            int[] f = frontier.poll();
            int r = f[1];
            int c = f[2];
            int pr = f[3];
            int pc = f[4];
            int dir = f[5];

            if (visited[r][c]) continue;
            visited[r][c] = true;
            Cell currCell = cellRepr[r][c];
            Cell parentCell = cellRepr[pr][pc];
            int oppositeDir = switch (dir) {
                case 0 -> 1;
                case 1 -> 0;
                case 2 -> 3;
                case 3 -> 2;
                default -> -1;
            };
            currCell = new Cell(currCell.getCell() & ~wallBit[oppositeDir], c * cellSize, r * cellSize, cellSize);
            parentCell = new Cell(parentCell.getCell() & ~wallBit[dir], pc * cellSize, pr * cellSize, cellSize);
            cellRepr[r][c] = currCell;
            cellRepr[pr][pc] = parentCell;

            // Add neighbors of current cell to frontier
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    int weight = rand.nextInt(100); // random weight
                    frontier.add(new int[]{weight, nr, nc, r, c, i});
                }
            }
        }

        drawStartAndEndCells(cellRepr);
    }
}
