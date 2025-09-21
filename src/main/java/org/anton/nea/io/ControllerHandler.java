package org.anton.nea.io;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import org.anton.nea.maze.AnimationRenderer;
import org.anton.nea.maze.GameBoard;
import org.anton.nea.maze.Player;
import javafx.scene.control.CheckBox;
import org.anton.nea.maze.algos.gen.MazeGenerator;
import org.anton.nea.util.Vector2;

import java.util.Objects;

/**
 * IS it easier to wait for ICT
 * to do their job,
 * OR
 * is it easier to write a whole
 * fucking controller driver
 * BY MYSELF
 *
 */

public class ControllerHandler extends MovementHandler {
    private ControllerManager controllers = new ControllerManager();
    private Vector2 direction = new Vector2(0, 0);
    private  ControllerState state;
    public ControllerHandler(GameBoard gameBoard, Player player, Scene scene, CheckBox checkbox){
        super(gameBoard, player, scene, checkbox);

        controllers.initSDLGamepad();
        state = controllers.getState(0); // first controller, only doing 1 rn, however i *might* add support for many players ( LOCALLY IM NOT FUCKING DOING MULTIPLAYER )
        if (state.isConnected) {
            float x = state.leftStickX;
            float y = state.leftStickY;
        }

        // right now to create a loop bcs i cant do the event trigger thingy for controller

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameBoard.getMovementHandler().move();
            }
        };
        timer.start();

    }

    /**
     * "gracefully" shutdown
     */
    public void shutdown(){
        controllers.quitSDLGamepad();
    }
    @Override
    protected void move(){
        state = controllers.getState(0);
        if (controllers.getState(0) != null){

            float deadzone = 0.5f;
            float x = Math.abs(state.leftStickX) > deadzone ? state.leftStickX : 0;
            float y = Math.abs(state.leftStickY) > deadzone ? state.leftStickY : 0;

            Vector2 stickVector = new Vector2(x, -y); // invert Y if needed

            // Scale by MovementHandler speed
            Vector2 movement = stickVector.multiply(1.5);

            moveVector(movement);

        }
    }
}

