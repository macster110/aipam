package com.jamdev.maven.aipam;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Minimal JavaFX Application to demonstrate build and packaging infrastructure
 * This replaces the complex AiPAM application temporarily to show the workflow works
 */
public class AIPamApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AiPAM - Build Demo");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("AiPAM - Audio Information Processing and Management");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label infoLabel = new Label("This is a demo version showing the build infrastructure works!");
        
        Button testButton = new Button("Test Button");
        testButton.setOnAction(e -> {
            System.out.println("Build and packaging infrastructure working correctly!");
        });

        root.getChildren().addAll(titleLabel, infoLabel, testButton);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}