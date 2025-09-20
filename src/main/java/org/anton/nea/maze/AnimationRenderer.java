package org.anton.nea.maze;


import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import org.anton.nea.maze.algos.solve.Dijkstra;
import org.anton.nea.maze.algos.solve.MazeSolver;


import java.util.Collections;
import java.util.List;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import org.anton.nea.util.Color2;
import javafx.scene.paint.Color;
import javafx.util.Duration;
/**
 * This class draws the maze gen and solve algorithm step by step, so people can see how it works. this is for "coolness"
 */
public class AnimationRenderer {
    /**
     * This shows the maze solving algorithm step by step, based on the one parsed.
     * this should be called after the maze is generated.
     * @param gameBoard the borrd
     * @param solver the algorithm to use to solve a maze
     *
     *
     *
     *Right this bug took me about 2 hours to figure out. and I SWEAR TO GOD im so happy its done.
     *the delay between drawing explored and path was weirdly long, so after some investigation and basically rereading line by line my code, i realised, what it actually does is:
     *adds ALL VISITED NODES, IN ORDER, and this is the important part : EVEN THE BACKTRACKS. so i was redrawing over backtracked cells, and i didnt realise because i was drawring
     * in the same FUCKING COLOR.
     *so . have a look inside {@link org.anton.nea.maze.algos.solve.Dijkstra#solve }
     */
    public static void showSolvingAnimation(GameBoard gameBoard, MazeSolver solver, int animationSpeed) {

        gameBoard.getMovementHandler().pause();

        if (!(solver instanceof org.anton.nea.maze.algos.solve.Dijkstra dijkstra)) {
            // ive only done this for dijkstra (ez)
            throw new IllegalArgumentException(("must be djikjstasara"));

        }
        new Thread(() -> {
            List<Cell> path = solver.solve(gameBoard.cellRepr, gameBoard.getRows(), gameBoard.getCols());
            List<Cell> explored = solver.getExploredOrder();


            GraphicsContext gc = gameBoard.getGraphicsContext2D();
            Timeline timeline = new Timeline();
            for (int i = 0; i < explored.size(); i++) {
                Cell cell = explored.get(i);
                KeyFrame kf = new KeyFrame(
                        Duration.millis(i * animationSpeed),
                        e -> cell.draw(gc, new Color2(Color.BLACK, Color.BLUE))
                );
                timeline.getKeyFrames().add(kf);
            }
            int offset = explored.size() * animationSpeed;
            System.out.println(explored);
            System.out.println(explored.size());
            for (int i = 0; i < path.size(); i++) {
                Cell cell = path.get(i);
                KeyFrame kf = new KeyFrame(
                        Duration.millis(offset + i * animationSpeed),
                        e -> cell.draw(gc, new Color2(Color.YELLOW, Color.ORANGE))
                );
                timeline.getKeyFrames().add(kf);
            }

            timeline.play();
            PauseTransition pause = new PauseTransition(Duration.millis(offset + path.size() * animationSpeed + animationSpeed*20));
            pause.setOnFinished(e -> {        gameBoard.getMovementHandler().resume();
            });
            pause.play();

        }).start();

    }

}
