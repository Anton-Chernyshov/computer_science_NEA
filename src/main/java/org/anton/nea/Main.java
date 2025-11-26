package org.anton.nea;

import javafx.application.Platform;
import org.anton.nea.controllers.MainController;
import org.anton.nea.controllers.SplashController;
import org.anton.nea.controllers.HomeController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Actual entry point for the program. The program starts by calling the splash controller, then running the main window
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Stage splashStage = new Stage();
        SplashController splashController = new SplashController(splashStage);

        splashController.loadSplash(() -> {

            Platform.runLater(() -> {
                // spash -> home
                splashStage.close();

                Stage homeStage = new Stage();
                HomeController homeController = new HomeController(homeStage);

                homeController.loadHome(() -> {
                    // home -> main
                    Platform.runLater(() -> {
                        homeStage.close();

                        Stage mainStage = new Stage();
                        MainController mainController = new MainController(mainStage);
                        mainController.loadMain();
                    });
                });
            });

        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
