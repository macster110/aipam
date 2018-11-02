package com.jamdev.maven.aipam;

import java.io.IOException;

import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.utilsFX.AISVGDimensionProvider;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
/**
 * The main FX application for AiPam.
 * 
 * @author Jamie Macaulay 
 *
 */
public class AIPamApp extends Application {


    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	//create the controller. 
        AiPamController aiPamController = new AiPamController(); 

        //enable loading of svg images. 
        SvgImageLoaderFactory.install(new AISVGDimensionProvider());
    	
        StackPane root = new StackPane();
        root.setStyle("-fx-background: #1d1d1d; -fx-font: 12px Segoe; -fx-text-fill: white;");
        
        //apply JMetro theme
        new JMetro(JMetro.Style.DARK).applyTheme(root);
        //add extra stylesheet for fluent design menu buttons and tab pane. . 
        root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());

        //create the view
        AIPamView sensorView = new AIPamView(aiPamController, primaryStage); 
                
   
        root.getChildren().add(sensorView);
        
//        root.getStylesheets().add(darkStyle);
        primaryStage.setScene(new Scene(root, 1000, 750));
        primaryStage.show();
    }

}
