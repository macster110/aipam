package com.jamdev.maven.aipam;

import java.io.IOException;

import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.utilsFX.AISVGDimensionProvider;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.DefaultDimensionProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
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

//        //enable loading of svg images. 
//        SvgImageLoaderFactory.install(new AISVGDimensionProvider());
    	
        StackPane root = new StackPane();
        
//		primaryStage.getIcons().add(new Image("file:Cluster_sml.png"));
//		primaryStage.getIcons().getClass().getResource("Cluster_sml.png");
		
        //need to use this method for images packaged in jar. 
//		primaryStage.getIcons().add( new Image(AIPamApp.class.getResourceAsStream("Cluster_sml.png"))); 
        
//        System.out.println("Image: 1" +  getClass().getResource(".") + "  ");
//        System.out.println("Image: 2" + AIPamApp.class.getResourceAsStream("."));

        
		primaryStage.getIcons().add( new Image(AIPamApp.class.getResourceAsStream("Cluster_med.png"))); 
		primaryStage.getIcons().add( new Image(AIPamApp.class.getResourceAsStream("Cluster.png"))); 
		
		primaryStage.setTitle("Sound Sort");
        
//        //apply JMetro theme
//        new JMetro(JMetro.Style.DARK).applyTheme(root);
//        //add extra stylesheet for fluent design menu buttons and tab pane. . 
//        root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
//       // root.setStyle("-fx-background: #1d1d1d; -fx-font: 12px Segoe; -fx-text-fill: white;");
//        root.setStyle("-fx-background: BACKGROUND;");

        //create the view
        AIPamView sensorView = new AIPamView(aiPamController, primaryStage, root); 
    
        root.getChildren().add(sensorView);
        
        Scene scene = new Scene(root, 1000, 750);
        scene.setRoot(root);
        

        
        //need to apply theme after stage to get it to work with new version of JMetro
        primaryStage.setScene(scene);
        primaryStage.show();

        // Apply theme after the stage is shown to avoid Prism texture errors
        javafx.application.Platform.runLater(() -> {
            sensorView.getTheme().applyTheme(AITheme.TRANSIT_DARK_THEME, root);
        });
    }

}