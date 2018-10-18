package com.jamdev.maven.aipam;

import java.io.IOException;

import com.jamdev.maven.aipam.layout.PamSneView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PamSneApplication extends Application {
	
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
     
        StackPane root = new StackPane();
        
        PamSneController pamSneControl = new PamSneController(); 
        PamSneView sensorView = new PamSneView(pamSneControl); 

        root.getChildren().add(sensorView);
//        root.getStylesheets().add(darkStyle);

        primaryStage.setScene(new Scene(root, 750, 550));
        primaryStage.show();
        
    }

}
