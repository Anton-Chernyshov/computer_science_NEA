package org.anton.nea;

import org.anton.nea.controllers.MainController;
import org.anton.nea.controllers.SplashController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Actual entry point for the program. The program starts by calling the splash controller, then running the main window
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create controllers manually
        SplashController splashController = new SplashController(primaryStage);
        MainController mainController = new MainController(primaryStage);

        // Load splash screen, then run main screen
        splashController.loadSplash(mainController::loadMain);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
