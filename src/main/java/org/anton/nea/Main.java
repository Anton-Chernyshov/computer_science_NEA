package org.anton.nea;

import javafx.application.Application;
import javafx.stage.Stage;
import org.anton.nea.controllers.MainController;
import org.anton.nea.controllers.SplashController;

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
