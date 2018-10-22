package com.jamdev.maven.aipam.layout;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * General progress bar pane. Shows a progress bar during threaded tasks. Also 
 * contains a cancel button to cancel tasks.  
 * @author Jamie Macaulay 
 *
 */
public class ProgressBarPane extends BorderPane {
	
	/**
	 * The progress bar. 
	 */
	private ProgressBar progressBar; 
	
	/**
	 * The cancel button. 
	 */
	private Button cancelButton; 
	
	/**
	 * The cancel button. 
	 */
	private Label progressLabel; 
	
	/**
	 * The title of the progress bar. 
	 */
	private Label progressTitle; 
	
	/**
	 * Constructor for progress bar pane. 
	 */
	public ProgressBarPane(AIPamView aiPamView) {
		this.setCenter(createProgressPane()); 
	}

	/**
	 * Create the progress bar pane.
	 * @return the progress bar pane/ 
	 */
	private Pane createProgressPane() {
		
		progressTitle = new Label(); 
		progressTitle.setFont(AIPamView.defaultLabelTitle2);
		progressTitle.setTextFill(AIPamView.defaultTitleColour);

		
		progressBar = new ProgressBar(); 
		cancelButton = new Button("Cancel");
		
		HBox progressHolder = new HBox(progressBar, cancelButton);
		HBox.setHgrow(progressBar, Priority.ALWAYS);
		progressHolder.setAlignment(Pos.CENTER);
		
		progressLabel = new Label();  

		VBox holder = new VBox(); 
		holder.setSpacing(5);
		
		holder.getChildren().addAll(progressTitle, progressHolder, progressLabel); 
		
		return holder;
	}
	
	/**
	 * Set the task for the progress pane. 
	 */
	public synchronized void setTask(Task task) {
		//unbind the progress bar from previous task property
		progressBar.progressProperty().unbind();
		progressLabel.textProperty().unbind();
		
		//set the title of the task 
		progressTitle.setText(task.getTitle());
		
		//bind the progress bar to the task progress. 
		progressBar.progressProperty().bind(task.progressProperty());
		//bind the cnacle button to the task cancel. 
		cancelButton.setOnAction((action)->{
			task.cancel(); 
		});
		//bind the label to the task update message. 
		progressLabel.textProperty().bind(task.messageProperty());
		
	}
	
	
	
	

}
