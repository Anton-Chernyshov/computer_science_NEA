package org.anton.nea.helpers;
        import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
        import org.anton.nea.maze.GameBoard;

        import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class toPNG {

        public static void Canvas(Canvas canvas) throws IOException{
                WritableImage image = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, image);
                File file = new File("maze.png");

        }

}
