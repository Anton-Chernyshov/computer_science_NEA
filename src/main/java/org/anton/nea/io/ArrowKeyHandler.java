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
    private final double speed;
    private BooleanProperty hasAcceleration = new SimpleBooleanProperty(false);
    private Vector2 velocity = new Vector2(0, 0); // current movement speed
    private final double maxSpeed = 2.0;           // max pixels per frame
    private final double acceleration = 0.2;       // speed gained per frame while pressing a key
    private final double friction = 0.1;
    private AnimationTimer timer;
    public ArrowKeyHandler(GameBoard gameBoard, Player player, Scene scene, CheckBox checkbox) {

        super(gameBoard, player, scene);

        this.speed = 2;
        checkbox.selectedProperty().bindBidirectional(hasAcceleration);

        // Optional: listen to changes
        hasAcceleration.addListener((obs, oldVal, newVal) -> {
            System.out.println("Checkbox changed: " + newVal);
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            pressedKeys.add(event.getCode());
            updateMovement();
            event.consume();
        });

        // Track key releases
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            pressedKeys.remove(event.getCode());
            updateMovement();
            event.consume();
        });
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateMovement();
                player.updatePlayer();
            }
        };
        timer.start();
    }
    @Override
    public void pause(){
        this.timer.stop();
    }
    @Override
    public void resume(){
        this.timer.start();
    }
    private void updateMovement() {
        Vector2 direction = new Vector2(0, 0);

        if (pressedKeys.contains(KeyCode.UP)) direction = direction.add(Vector2.UP);
        if (pressedKeys.contains(KeyCode.DOWN)) direction = direction.add(Vector2.DOWN);
        if (pressedKeys.contains(KeyCode.LEFT)) direction = direction.add(Vector2.LEFT);
        if (pressedKeys.contains(KeyCode.RIGHT)) direction = direction.add(Vector2.RIGHT);
        if (hasAcceleration.get()) {
            if (!direction.isZero()) {
                // Accelerate in the direction of input
                Vector2 desiredVelocity = direction.normalize().multiply(acceleration);
                velocity = velocity.add(desiredVelocity);

                // Clamp velocity to max speed
                if (velocity.magnitude() > maxSpeed) {
                    velocity = velocity.normalize().multiply(maxSpeed);
                }
            } else {
                // Apply friction when no keys pressed
                velocity = velocity.multiply(1 - friction);
                if (velocity.magnitude() < 0.01) velocity = new Vector2(0, 0); // stop tiny movements
            }
            moveVector(velocity);
        } else {
            if (!direction.isZero()) {
                moveVector(direction.normalize().multiply(speed));

            }
        }
    }

}
