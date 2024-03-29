package com.jamdev.maven.aipam;

import java.io.IOException;

import org.controlsfx.control.ToggleSwitch;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Test the toggle switch using JMetro. This a minimla example of a bug 
 * where the toggle switch thumb moves outside the toggle switch bounds. 
 * @author Jamie Macaulay 
 *
 */
public class ToggleSwitchApp extends Application {


    public static void main(String[] args) {
        launch(args);
    }

	private ToggleSwitch showPromptsSwitch;
    
    @Override
    public void start(Stage primaryStage) throws IOException {

    	
        StackPane root = new StackPane();
   
		
		primaryStage.setTitle("Toggle Switch Bug");
		
		BorderPane borderPane = new BorderPane(); 
		//user options ot display all clips as features. 
  		showPromptsSwitch = new ToggleSwitch(); 
  		showPromptsSwitch.selectedProperty().addListener((obs, oldVal, newVal)->{
  			
  		});
//  		showPromptsSwitch.setMaxWidth(100);

  		Label label = new Label("Hello my toggle switch: Resize to see toggle bug");
  		label.getStyleClass().add("label-title2");
  		HBox hBox = new HBox(); 
  		hBox.setAlignment(Pos.CENTER_LEFT);
  		hBox.setSpacing(5);
  		hBox.getChildren().addAll(showPromptsSwitch, label); 
  		
  		borderPane.setCenter(hBox);
  		

        root.getChildren().add(borderPane);
        
        
//        root.getStylesheets().add(darkStyle);
        primaryStage.setScene(new Scene(root, 200, 80));
        
    	//apply JMetro theme
//		new JMetro(Style.LIGHT).setScene(root.getScene());
        
        primaryStage.show();
    }

}
