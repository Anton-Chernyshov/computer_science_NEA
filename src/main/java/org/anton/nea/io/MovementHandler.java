package org.anton.nea.io;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import org.anton.nea.maze.GameBoard;
import org.anton.nea.maze.Player;
import org.anton.nea.util.Vector2;
import javafx.scene.Scene;

public abstract class MovementHandler {
    private GameBoard gameBoard;
    private Player player;
    private final double speed;
    private BooleanProperty hasAcceleration = new SimpleBooleanProperty(false);
    private Vector2 velocity = new Vector2(0, 0); // current movement speed
    private final double maxSpeed = 2.0;           // max pixels per frame
    private final double acceleration = 0.2;       // speed gained per frame while pressing a key
    private final double friction = 0.1;
    private AnimationTimer timer;
    public MovementHandler(GameBoard gameBoard, Player player, Scene scene, CheckBox checkbox) {
        this.gameBoard = gameBoard;
        this.player = player;
        // this is just a constructor,
        this.speed = 2;
        checkbox.selectedProperty().bindBidirectional(hasAcceleration);

        hasAcceleration.addListener((obs, oldVal, newVal) -> {
            System.out.println("Checkbox changed: " + newVal);
        });
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
                player.updatePlayer();
            }
        };
        timer.start();
    }
    protected abstract void move();
    public Player getPlayer(){return this.player;}
    public void setPlayer(Player player){this.player = player;}
    public void resume(){
        this.timer.start();
    }
    public void pause(){
        this.timer.stop();
    }

    protected void updateMovement(Vector2 direction) {

        /*
        Alr ill be honest i need a bit more work on acceleration since its a bit iffy. the current system means that the movement is calculated in the background as doubles
        but the drawring is done on an integer based grid, so if any of the v
         */
        if (hasAcceleration.get()) {
            if (direction.isNotZero()) {
                // accelerate
                Vector2 desiredVelocity = direction.normalize().multiply(acceleration);
                velocity = velocity.add(desiredVelocity);

                // clamp veloctiy
                if (velocity.magnitude() > maxSpeed) {
                    velocity = velocity.normalize().multiply(maxSpeed);
                }
            } else {
                //  friction
                velocity = velocity.multiply(1 - friction);
                if (velocity.magnitude() < 0.01) velocity = new Vector2(0, 0); // stop tiny movements ( had to add this bcs it was weird
            }
            moveVector(velocity);
        } else {
            if (direction.isNotZero()) {
                moveVector(direction.normalize().multiply(speed));

            }
        }
    }
    public void moveVector(Vector2 vector){
        player.moveVector(vector);
    }
    public abstract void startTimer();
}
