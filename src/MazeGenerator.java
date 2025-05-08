import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MazeGenerator extends JPanel {
    // Constants for grid and cell size
    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;

    // Maze grid and walls list
    private Cell[][] grid = new Cell[GRID_HEIGHT][GRID_WIDTH];
    private List<Wall> walls = new ArrayList<>();
    private List<Point> solutionPath = new ArrayList<>();

    // Start and end points
    private int startX = 0, startY = 0, endX = GRID_WIDTH - 1, endY = GRID_HEIGHT - 1;

    // Timer and generation flag
    private Timer timer;
    private boolean generatingMaze = true;

    // Constructor
    public MazeGenerator() {
        setPreferredSize(new Dimension(CELL_SIZE * GRID_WIDTH, CELL_SIZE * GRID_HEIGHT));
        setBackground(Color.WHITE);

        // Initialize grid with walls
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }

        // Initialize walls around the start
        grid[startY][startX].visited = true;
        addWalls(startX, startY);

        // Initialize the timer to step through maze generation
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (generatingMaze) {
                    step();
                }
            }
        }, 0, 1);  // Run step every 50ms
    }

    // Add walls around the current cell
    private void addWalls(int x, int y) {
        if (y > 0) walls.add(new Wall(x, y, "top"));
        if (y < GRID_HEIGHT - 1) walls.add(new Wall(x, y, "bottom"));
        if (x > 0) walls.add(new Wall(x, y, "left"));
        if (x < GRID_WIDTH - 1) walls.add(new Wall(x, y, "right"));
    }

    // Step function for generating the maze
    private void step() {
        if (walls.isEmpty()) {
             // Solve the maze once generation is complete
            generatingMaze = false; // Stop maze generation once complete
            repaint();
            return;
        }

        // Randomly pick a wall
        Wall wall = walls.get(new Random().nextInt(walls.size()));
        walls.remove(wall);

        int x = wall.x;
        int y = wall.y;
        String direction = wall.direction;

        int dx = 0, dy = 0;
        switch (direction) {
            case "top": dy = -1; break;
            case "bottom": dy = 1; break;
            case "left": dx = -1; break;
            case "right": dx = 1; break;
        }

        int nx = x + dx, ny = y + dy;
        if (nx >= 0 && nx < GRID_WIDTH && ny >= 0 && ny < GRID_HEIGHT) {
            if (!grid[ny][nx].visited) {
                grid[y][x].removeWall(direction);
                String oppositeDirection = getOppositeDirection(direction);
                grid[ny][nx].removeWall(oppositeDirection);

                grid[ny][nx].visited = true;
                addWalls(nx, ny);
            }
        }
        repaint();
    }

    // Get the opposite wall direction
    private String getOppositeDirection(String direction) {
        switch (direction) {
            case "top": return "bottom";
            case "bottom": return "top";
            case "left": return "right";
            case "right": return "left";
            default: return null;
        }
    }

    // Solve the maze using BFS
    public void solveMaze() {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        Set<Point> visited = new HashSet<>();

        queue.add(new Point(startX, startY));
        visited.add(new Point(startX, startY));
        cameFrom.put(new Point(startX, startY), null);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int x = current.x, y = current.y;

            if (current.equals(new Point(endX, endY))) {
                break; // Found the end point
            }

            // Check all possible neighbors
            Point[] neighbors = {
                new Point(x, y - 1), new Point(x, y + 1), new Point(x - 1, y), new Point(x + 1, y)
            };
            for (Point neighbor : neighbors) {
                int nx = neighbor.x, ny = neighbor.y;
                if (nx >= 0 && nx < GRID_WIDTH && ny >= 0 && ny < GRID_HEIGHT) {
                    if (!visited.contains(neighbor) && !hasWall(x, y, neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        cameFrom.put(neighbor, current);
                    }
                }
            }
        }
        // Reconstruct the path
        Point current = new Point(endX, endY);
        while (current != null) {
            solutionPath.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(solutionPath);
    }
    public void regenerate() {
        timer.cancel(); // Stop current timer
        solutionPath.clear();
        walls.clear();
    
        // Re-initialize the grid
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }
    
        // Re-initialize walls and start generation
        grid[startY][startX].visited = true;
        addWalls(startX, startY);
        generatingMaze = true;
    
        // Restart timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (generatingMaze) {
                    step();
                }
            }
        }, 0, 1);  // 1ms interval for speed
    }
    
    // Check if there's a wall between two cells
    private boolean hasWall(int x, int y, Point neighbor) {
        int nx = neighbor.x, ny = neighbor.y;
        if (nx == x && ny == y - 1) return grid[y][x].hasWall("top");
        if (nx == x && ny == y + 1) return grid[y][x].hasWall("bottom");
        if (nx == x - 1 && ny == y) return grid[y][x].hasWall("left");
        if (nx == x + 1 && ny == y) return grid[y][x].hasWall("right");
        return false;
    }

    // Paint the maze
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        // Draw the maze grid
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                Cell cell = grid[y][x];
                int x1 = x * CELL_SIZE, y1 = y * CELL_SIZE;
                int x2 = x1 + CELL_SIZE, y2 = y1 + CELL_SIZE;

                // Draw the walls
                if (cell.hasWall("top")) g2d.drawLine(x1, y1, x2, y1);
                if (cell.hasWall("bottom")) g2d.drawLine(x1, y2, x2, y2);
                if (cell.hasWall("left")) g2d.drawLine(x1, y1, x1, y2);
                if (cell.hasWall("right")) g2d.drawLine(x2, y1, x2, y2);
            }
        }

        // Highlight the start and end
        g2d.setColor(Color.GREEN);
        g2d.fillRect(startX * CELL_SIZE + 2, startY * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(endX * CELL_SIZE + 2, endY * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);

        // Draw the solution path in red
        g2d.setColor(Color.RED);
        for (int i = 0; i < solutionPath.size() - 1; i++) {
            Point p1 = solutionPath.get(i);
            Point p2 = solutionPath.get(i + 1);
            int x1 = p1.x * CELL_SIZE + CELL_SIZE / 2, y1 = p1.y * CELL_SIZE + CELL_SIZE / 2;
            int x2 = p2.x * CELL_SIZE + CELL_SIZE / 2, y2 = p2.y * CELL_SIZE + CELL_SIZE / 2;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prim's Maze Generator and Solver");
        MazeGenerator mazeGenerator = new MazeGenerator();
        
        JButton regenerateButton = new JButton("Regenerate Maze");
        regenerateButton.addActionListener(e -> mazeGenerator.regenerate());
    
        JButton solveButton = new JButton("Solve Maze");
        solveButton.addActionListener(e -> {
            if (!mazeGenerator.generatingMaze) {
                mazeGenerator.solveMaze();
                mazeGenerator.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Maze is still generating. Please wait.");
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(mazeGenerator, BorderLayout.CENTER);
        JPanel sidePanel = new JPanel();
        panel.add(sidePanel, BorderLayout.EAST);
        sidePanel.add(solveButton, BorderLayout.NORTH);
        sidePanel.add(regenerateButton, BorderLayout.NORTH);
        JSlider width = new JSlider();
        sidePanel.add(width, BorderLayout.CENTER);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }

    // Cell class representing each cell in the grid
    private class Cell {
        int x, y;
        boolean visited;
        Set<String> walls = new HashSet<>(Arrays.asList("top", "bottom", "left", "right"));

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
            this.visited = false;
        }

        void removeWall(String direction) {
            walls.remove(direction);
        }

        boolean hasWall(String direction) {
            return walls.contains(direction);
        }
    }

    // Wall class representing walls between cells
    private class Wall {
        int x, y;
        String direction;

        Wall(int x, int y, String direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
}
