package org.anton.nea.maze.algos.gen;

import org.anton.nea.maze.Cell;
import org.anton.nea.maze.GameBoard;

import java.util.Comparator;
import java.util.PriorityQueue;
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


        // right now for some crazy stuff.
        /*
        For "this" algorithm i decided to try a weird approach.
        i assign a random weight to each edge, and from this i then run dijktras on the graph to get a maze.

         */


                Random rand = new Random(seed);
                boolean[][] visited = new boolean[rows][cols];

                int[] dr = {-1, 1, 0, 0};
                int[] dc = {0, 0, -1, 1};
                int[] wallBit = {0x8, 0x4, 0x2, 0x1};

                PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
                int startRow = 0, startCol = 0;
                pq.add(new int[]{0, startRow, startCol, -1, -1, -1});

                while (!pq.isEmpty()) {
                    int[] current = pq.poll();
                    int r = current[1];
                    int c = current[2];
                    int pr = current[3];
                    int pc = current[4];
                    int dir = current[5];

                    if (visited[r][c]) continue;
                    visited[r][c] = true;
                    if (pr != -1) {
                        Cell currCell = cellRepr[r][c];
                        Cell parentCell = cellRepr[pr][pc];

                        // Current cell: remove opposite wall
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
                    }

                    for (int i = 0; i < 4; i++) {
                        int nr = r + dr[i];
                        int nc = c + dc[i];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                            int weight = 1 + rand.nextInt(69420); // fuckaah heuristic
                            pq.add(new int[]{weight, nr, nc, r, c, i});
                        }
                    }
                }

                drawStartAndEndCells(cellRepr);



    }
}