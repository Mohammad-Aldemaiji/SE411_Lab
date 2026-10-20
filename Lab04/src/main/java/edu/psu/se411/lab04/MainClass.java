package edu.psu.se411.lab04;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Entry point of Lab 04: a minimal JavaFX application used to verify that
 * JavaFX is correctly imported through Maven and runs with {@code mvn clean javafx:run}.
 */
public class MainClass extends Application {

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Builds and shows the main window.
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Label label = new Label("Hello from SE411 Lab 04 - JavaFX via Maven!");
            Scene scene = new Scene(new StackPane(label), 400, 200);
            primaryStage.setTitle("My Project");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
