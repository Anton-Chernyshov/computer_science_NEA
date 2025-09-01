package org.anton.nea.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.anton.nea.controllers.ErrorController;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;

public class ErrorWindow {

    public static void show(Exception error) {
        try {
            FXMLLoader loader = new FXMLLoader(ErrorWindow.class.getResource("/org/anton/nea/error.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Error...");
            stage.initModality(Modality.APPLICATION_MODAL); // blocks main window

            stage.setScene(new Scene(loader.load()));
            ErrorController controller = loader.getController();
            controller.setMessage(error.getMessage());
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement elem : error.getStackTrace()) {
                sb.append(elem.toString()).append("\n");
            }
            controller.setTraceStack(sb.toString());


            // write to logs/...
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HH-mm-ss");
            String timestamp = LocalDateTime.now().format(formatter);
            System.out.println("Working directory: " + System.getProperty("user.dir"));

            new java.io.File("logs/").mkdirs(); // create folder if missing
            try (PrintWriter pw = new PrintWriter(new FileWriter( "logs/" + timestamp + ".log", true))) {
                error.printStackTrace(pw);  // write stack trace to file
                pw.println();            // optional: add an empty line
            } catch (Exception io) {
                io.printStackTrace();    // fallback to console
            }
            stage.showAndWait(); // wait until user closes
        } catch (Exception e) {
            e.printStackTrace();
            // o shit my ERROR handler crashed???

        }
    }
}
