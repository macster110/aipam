package com.jamdev.maven.aipam.layout;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Figures out if user prompts are needed and provides the view with appropriate user promt
 * panes. The user prompts are usually messages telling the user they need to re run some 
 * loading or analysing but can also contain some basic controls e.g. buttons to help users 
 * quickly access settings and/or re process. 
 * 
 * @author Jamie Macaulay
 *
 */
public class UserPrompts {

	/**
	 * User prompt enums. 
	 * @author Jamie Macaulay
	 *
	 */
	public enum UserPrompt {NOTHING_IMPORTED_YET, NOTHING_CLUSTERED_YET, IMPORT_AGAIN, RECREATE_IMAGES, RE_CLUSTER}

	/**
	 * Reference to the controller. 
	 */
	private AiPamController aiPamContol;

	/**
	 * Cluster image. 
	 */
	private ImageView clusterImage;

	/**
	 * Spectrogram 
	 */
	private ImageView specImage;;


	private int iconSize = 12; 


	/**
	 * Reference to the view. 
	 */
	private AIPamView aiPamView;


	public UserPrompts(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		aiPamContol=aiPamView.getAIControl();
		
		clusterImage = UtilsFX.whitenImage(new ImageView(aiPamView.getClusterIcon())); //create icon so not reloading it every time. 
		specImage = UtilsFX.whitenImage(new ImageView(aiPamView.getSpectrogramIcon())); //create icon so not reloading it every time. 
	}
	
	

	/**
	 * Figure what has changed since last audio load. 
	 * @return a list of flags indicating what needs re run. 
	 */
	public ArrayList<UserPrompt> checkLastSettings() {

		ArrayList<UserPrompt> userPrompts = new ArrayList<UserPrompt>(); 

		//lets chack all the params....
		if (aiPamContol.getLastAiParams()==null) {
			userPrompts.add(UserPrompt.NOTHING_IMPORTED_YET);
			return userPrompts; 
		}
		
//		System.out.println("Last decimator: " + aiPamContol.getLastAiParams().decimatorSR
//		+ " New channels: " + aiPamContol.getParams().decimatorSR);
//		System.out.println("Last channel: " + aiPamContol.getLastAiParams().channel
//				+ " New channels: " + aiPamContol.getParams().channel);

		boolean reimport = false; 
		if (aiPamContol.getLastAiParams().fftHop!=aiPamContol.getParams().fftHop) reimport = true;
		if (aiPamContol.getLastAiParams().fftLength!=aiPamContol.getParams().fftLength) reimport = true;

		if (aiPamContol.getLastAiParams().maximumClipLength!=aiPamContol.getParams().maximumClipLength) reimport = true;
		if (aiPamContol.getLastAiParams().decimatorSR!=aiPamContol.getParams().decimatorSR) reimport = true;
		if (aiPamContol.getLastAiParams().channel!=aiPamContol.getParams().channel) reimport = true;
		//if (!aiPamContol.getLastAiParams().audioFolder.equals(aiPamContol.getParams().audioFolder)); reimport = true;

		boolean reCalcImage = false; 
		if (!aiPamContol.getLastAiParams().spectrogramColour.equals(aiPamContol.getParams().spectrogramColour)) reCalcImage=true;
		if (aiPamContol.getLastAiParams().colourLims[0]!=aiPamContol.getParams().colourLims[0]) reCalcImage=true;
		if (aiPamContol.getLastAiParams().colourLims[1]!=aiPamContol.getParams().colourLims[1]) reCalcImage=true;

		
		//cluster messages
		boolean reCluster = false; 
		if (aiPamContol.getLastAiParams().clusterParams==null) {
			userPrompts.add(UserPrompt.NOTHING_CLUSTERED_YET);
		}
		else if (!aiPamContol.getLastAiParams().clusterParams.compare(aiPamContol.getParams().clusterParams)) reCluster=true;

		if (reimport) userPrompts.add(0,UserPrompt.IMPORT_AGAIN); //should always be first. 
		if (reCalcImage && !reimport) userPrompts.add(UserPrompt.RECREATE_IMAGES); 
		if (reCluster) userPrompts.add(UserPrompt.RE_CLUSTER); 

//		for (int i =0; i<userPrompts.size(); i++) {
//			System.out.println("UserPrompts: " + userPrompts.get(i));
//		}

		return userPrompts; 
	}

	/**
	 * Get the user prompt node for a particular user prompt. 
	 * @param userPrompt - the user prompt
	 * @return pane with user prompt message.
	 */
	public Node getUserPromptNode(UserPrompt userPrompt) {
		Pane pane = null; 
		switch (userPrompt) {
		case IMPORT_AGAIN:
			pane = importAgainMessage(); 
			break;
		case RECREATE_IMAGES:
			pane = reCalcImageMessage(); 
			break;
		case RE_CLUSTER:
			pane = reClusterMessage(); 
			break;
		case NOTHING_IMPORTED_YET:
			pane = nothingImportedYet(); 
			break;
		case NOTHING_CLUSTERED_YET:
			pane = nothingClusterredYet(); 

			break;
		default:
			break;
		}
		return pane;
	}

	private Pane nothingClusterredYet() {
		return iconLabelPane(clusterImage, 
				 "Nothing clustered yet: Press ", "Cluster to start algorithm" ); 
	}


	private Pane reClusterMessage() {
		 return iconLabelPane(clusterImage, 
				 "Need to re cluster clips: Press ", "Cluster to start algorithm" ); 
	}
	
	/**
	 * Create a pane with icon
	 * @param icon
	 * @param labelBfr
	 * @param labelAftr
	 * @return
	 */
	private Pane iconLabelPane(Node icon, String labelBfr, String labelAftr ) {
		HBox hBox = new HBox(); 
		hBox.setSpacing(5);
		hBox.setAlignment(Pos.CENTER_LEFT);
		
		Label label = new Label(labelBfr); 
		label.setStyle("-fx-text-fill: TEXT_USER_PROMPT"); 

		
		Label label2 = new Label(labelAftr); 
		label2.setStyle("-fx-text-fill: TEXT_USER_PROMPT"); 
		
		hBox.getChildren().addAll(label, icon, label2);
		
		return hBox;
	}

	/**
	 * Import again message. 
	 * @return import again message. 
	 */
	public Pane nothingImportedYet() {
		FontAwesomeIconView iconViewSettings = new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN_ALT); 
		iconViewSettings.setGlyphSize(iconSize);
		iconViewSettings.setGlyphStyle("-fx-fill: TEXT_USER_PROMPT"); 
		
		FontAwesomeIconView iconViewClips = new FontAwesomeIconView(FontAwesomeIcon.TH); 
		iconViewClips.setGlyphSize(iconSize);
		iconViewClips.setGlyphStyle("-fx-fill: TEXT_USER_PROMPT"); 
		
		FontAwesomeIconView audioFile = new FontAwesomeIconView(FontAwesomeIcon.FILE_AUDIO_ALT); 
		audioFile.setGlyphSize(iconSize);
		audioFile.setGlyphStyle("-fx-fill: TEXT_USER_PROMPT"); 
		
		Pane pane1=iconLabelPane(iconViewSettings, "No clips imported yet: select", "");
		Pane pane2=iconLabelPane(iconViewClips, "and then ", " to import."); 
		Pane pane3=iconLabelPane(audioFile, " Use ", "Audio for settings"); 

		HBox vBox = new HBox(); 
		vBox.getChildren().addAll(pane1, pane2, pane3); 

		return vBox;

	}


	private Pane reCalcImageMessage() {
		Button button = new Button();
	
		button.setGraphic(specImage);	
		
		Label label = new Label("Need to recalculate spectrogram images: Press");
		label.setStyle("-fx-text-fill: TEXT_USER_PROMPT"); 
		
		button.setOnAction((action)->{
			aiPamView.reCalcImages();
		});

		return 	iconLabelPane(button, "Need to recalculate spectrogram images: Press", "to recalculate now" )
;
	}

	/**
	 * Import again message. 
	 * @return import again message. 
	 */
	public Pane importAgainMessage() {
		HBox hBox = new HBox(); 
		hBox.setSpacing(5);
		hBox.setAlignment(Pos.CENTER_LEFT);

		FontAwesomeIconView iconViewSettings = new FontAwesomeIconView(FontAwesomeIcon.TH); 
		iconViewSettings.setGlyphSize(iconSize);
		iconViewSettings.setGlyphStyle("-fx-fill: TEXT_USER_PROMPT"); 
		
		return iconLabelPane(iconViewSettings, 
				 "Need to reimport clips: Press ", "Import Clips to start import" ); 
	}

	/**
	 * Get the pane with user prompts and notification. 
	 * @return the 
	 */
	public Pane getUserPromptPane(ArrayList<UserPrompt> userPrompts) {
		VBox vBox = new VBox(); 
		vBox.setSpacing(5);
		for (int i=0; i<userPrompts.size(); i++) {
			vBox.getChildren().add(getUserPromptNode(userPrompts.get(i))); 
		}
		return vBox;
	}



}
