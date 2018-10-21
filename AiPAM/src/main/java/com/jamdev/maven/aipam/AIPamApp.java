package com.jamdev.maven.aipam;

import java.io.IOException;

import com.jamdev.maven.aipam.layout.AIPamView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

/**
 * The main FX app for AiPam.
 * @author Jamie Macaulay 
 *
 */
public class AIPamApp extends Application {
	
	/**
	 * Dark style theme for jmetro. 
	 */
	public String darkStyle="resources//jmetroDarkTheme.css";
	private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1d1d1d");
        
        AiPamController aiPamController = new AiPamController(); 
        AIPamView sensorView = new AIPamView(aiPamController); 
        
		//apply the JMetro them 
        new JMetro(JMetro.Style.DARK).applyTheme(root);

        root.getChildren().add(sensorView);
//        root.getStylesheets().add(darkStyle);
        primaryStage.setScene(new Scene(root, 750, 550));
        primaryStage.show();
    }

}
