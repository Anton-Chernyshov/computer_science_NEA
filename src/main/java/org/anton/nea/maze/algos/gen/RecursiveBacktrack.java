package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrack extends MazeGenerator {
    @Override
    public void generateMaze(GameBoard gameBoard, long seed) {
        // Initialize all cells with all walls up (0xF)
        gameBoard.fillCellRepr(0xF);
        Cell[][] cellRepr = gameBoard.getCellRepr();
        int rows = gameBoard.getRows();
        int cols = gameBoard.getCols();
        int cellSize = gameBoard.getCellSize();


        boolean[][] visited = new boolean[rows][cols];
        Random rand = new Random(seed);

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        int[] wallBit = {0x8, 0x4, 0x2, 0x1};

        Stack<int[]> stack = new Stack<>();
        int startRow = 0;
        int startCol = 0;
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int r = current[0];
            int c = current[1];

            java.util.List<Integer> neighbors = new java.util.ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    neighbors.add(i);
                }
            }

            if (!neighbors.isEmpty()) {
                int dir = neighbors.get(rand.nextInt(neighbors.size()));
                int nr = r + dr[dir];
                int nc = c + dc[dir];

                Cell currentCell = cellRepr[r][c];
                Cell nextCell = cellRepr[nr][nc];

                currentCell = new Cell(currentCell.getCell() & ~wallBit[dir], c * cellSize, r * cellSize, cellSize);
                cellRepr[r][c] = currentCell;

                int oppositeDir = switch (dir) {
                    case 0 -> 1;
                    case 1 -> 0;
                    case 2 -> 3;
                    case 3 -> 2;
                    default -> -1;
                };
                nextCell = new Cell(nextCell.getCell() & ~wallBit[oppositeDir], nc * cellSize, nr * cellSize, cellSize);
                cellRepr[nr][nc] = nextCell;

                visited[nr][nc] = true;
                stack.push(new int[]{nr, nc});
            } else {
                stack.pop();
            }
        }
        drawStartAndEndCells(cellRepr);
    }
}
