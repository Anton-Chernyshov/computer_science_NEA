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

public class WASDHandler extends MovementHandler{
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    public WASDHandler(GameBoard gameBoard, Player player, Scene scene, CheckBox checkbox) {

        super(gameBoard, player, scene, checkbox);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            pressedKeys.add(event.getCode());
            move();
            event.consume();
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            pressedKeys.remove(event.getCode());
            move();
            event.consume();
        });


    }
    @Override
    protected void move() {
        Vector2 direction = new Vector2(0, 0);

        if (pressedKeys.contains(KeyCode.W)) direction = direction.add(Vector2.UP);
        if (pressedKeys.contains(KeyCode.S)) direction = direction.add(Vector2.DOWN);
        if (pressedKeys.contains(KeyCode.A)) direction = direction.add(Vector2.LEFT);
        if (pressedKeys.contains(KeyCode.D)) direction = direction.add(Vector2.RIGHT);

        updateMovement(direction);

    }

}
