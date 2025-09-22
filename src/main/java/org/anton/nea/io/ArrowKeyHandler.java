package org.anton.nea.io;
import javafx.animation.AnimationTimer;
import org.anton.nea.maze.GameBoard;
import org.anton.nea.maze.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.anton.nea.util.Vector2;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.CheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
public class ArrowKeyHandler extends MovementHandler {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final Scene scene;
    public ArrowKeyHandler(GameBoard gameBoard, Player player, Scene scene, CheckBox checkbox) {

        super(gameBoard, player, scene, checkbox);

        this.scene = scene;




    }
    @Override
    protected void move() {
        Vector2 direction = new Vector2(0, 0);

        if (pressedKeys.contains(KeyCode.UP)) direction = direction.add(Vector2.UP);
        if (pressedKeys.contains(KeyCode.DOWN)) direction = direction.add(Vector2.DOWN);
        if (pressedKeys.contains(KeyCode.LEFT)) direction = direction.add(Vector2.LEFT);
        if (pressedKeys.contains(KeyCode.RIGHT)) direction = direction.add(Vector2.RIGHT);

        updateMovement(direction);

    }

    /**
     * starts listeners for the game input, in this case, it handles arrow key events.
     */
    public void startTimer(){

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            pressedKeys.add(event.getCode());
            move();
            event.consume();
        });

        // Track key releases
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            pressedKeys.remove(event.getCode());
            move();
            event.consume();
        });
    }
}
