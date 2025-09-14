package org.anton.nea.maze;


import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import org.anton.nea.maze.algos.solve.MazeSolver;
import java.util.List;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import org.anton.nea.util.Color2;
import javafx.scene.paint.Color;
/**
 * This class draws the maze gen and solve algorithm step by step, so people can see how it works. this is for "coolness"
 */
public class AnimationRenderer {
    /**
     * This shows the maze solving algorithm step by step, based on the one parsed.
     * this should be called after the maze is generated.
     * @param gameBoard the borrd
     * @param solver the algorithm to use to solve a maze
     */
    public static void showSolvingAnimation(GameBoard gameBoard, MazeSolver solver) {

        if (!(solver instanceof org.anton.nea.maze.algos.solve.Dijkstra dijkstra)){
            // ive only done this for dijkstra (ez)
            throw new IllegalArgumentException(("must be djikjstasara"));

        }
        List<Cell> explored = dijkstra.getExploredOrder();
        List<Cell> path = dijkstra.solve(gameBoard.cellRepr, gameBoard.getRows(), gameBoard.getCols());

        GraphicsContext gc = gameBoard.getGraphicsContext2D();

        Timeline timeline = new Timeline();
        int delayMs = 25;

        for (int i = 0; i < explored.size(); i++) {
            Cell cell = explored.get(i);
            KeyFrame kf = new KeyFrame(
                    Duration.millis(i * delayMs),
                    e -> cell.draw(gc, new Color2(Color.BLACK, Color.BLUE))
            );
            timeline.getKeyFrames().add(kf);
        }

        int offset = explored.size() * delayMs;
        for (int i = 0; i < path.size(); i++) {
            Cell cell = path.get(i);
            KeyFrame kf = new KeyFrame(
                    Duration.millis(offset + i * delayMs),
                    e -> cell.draw(gc, new Color2(Color.YELLOW, Color.ORANGE))
            );
            timeline.getKeyFrames().add(kf);
        }
        timeline.play();

    }
}
