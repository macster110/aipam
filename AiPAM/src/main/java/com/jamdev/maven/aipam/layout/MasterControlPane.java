package com.jamdev.maven.aipam.layout;



import java.util.ArrayList;

import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * 
 * Pane with the some of the main controls e.g. import data and cluster data
 * 
 * @author Jamie Macaulay
 *
 */
public class MasterControlPane {
	
	private Pane mainPane;
	
	private AIPamView aiPamView;

	/**
	 * Import acoustic button
	 */
	private Button importClipsButton;

//	/**
//	 * The cluster button
//	 */
//	private Button clusterButton;

	/**
	 * Browse to clips
	 */
	private Button browseButton;

	/**
	 * Button animation showing highlighting. 
	 */
	private Animation currentAnimation; 
	
	/**
	 * List of all menu nodes. 
	 */
	private ArrayList<ControlPaneMenuItem> menuItems = new ArrayList<ControlPaneMenuItem>(); 

	
	public MasterControlPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		this.mainPane = createPane(); 
	}
	
	private Pane createPane() {
		
		VBox holder = new VBox(); 
		
		//main controls, import clips and cluster 
		
//		Label labelHome = new Label("Controls");
//		labelHome.setPadding(new Insets(5,5,5,5));
//		labelHome.getStyleClass().add("label-title1");
//		AIPamView.setButtonIcon(labelHome, new FontIcon("fa-home")); 
		
		
		browseButton = new Button("Browse..."); 
		browseButton.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(browseButton, "fa-folder-open"); 
		browseButton.prefWidthProperty().bind(holder.widthProperty());
		browseButton.setTooltip(new Tooltip(
				"Open a folder of clips"));
		
		menuItems.add(new StandardPaneMenuItem(browseButton, browseButton.getText()));

		
		browseButton.setOnAction((action)->{
			aiPamView.browseAndCheckAudio();
		});

		
		importClipsButton = new Button("Import Clips"); 
		importClipsButton.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(importClipsButton, "fa-th"); 
		importClipsButton.prefWidthProperty().bind(holder.widthProperty());
		importClipsButton.setTooltip(new Tooltip(
				"Start importing clips"));
		
		importClipsButton.setOnAction((action)->{
			aiPamView.importAcoustic(); 
		});

		menuItems.add(new StandardPaneMenuItem(importClipsButton, importClipsButton.getText()));
		
//		clusterButton = new Button("Cluster Clips"); 
//		clusterButton.getStyleClass().add("fluent-menu-button");
//		ImageView icon = UtilsFX.whitenImage(new ImageView(aiPamView.getClusterIcon())); 
//		clusterButton.setGraphic(icon);
//		clusterButton.setAlignment(Pos.CENTER_LEFT);
//		clusterButton.setGraphicTextGap(15);
//		clusterButton.prefWidthProperty().bind(holder.widthProperty());
//		clusterButton.setTooltip(new Tooltip(
//				"Start the clustering algorithm"));
//		
//		clusterButton.setOnAction((action)->{
//			aiPamView.cluster();
//		});
		
		
//		HBox controlBox = new HBox(); 
//		controlBox.prefWidthProperty().bind(holder.widthProperty());
//		controlBox.getChildren().addAll(importClipsButton); 
//		
		//holder.setSpacing(5);
		holder.getChildren().addAll(browseButton,  importClipsButton); 
		
		//holder.setAlignment(Pos.BASELINE_LEFT);

		return holder; 
	}
	
	
	public ArrayList<ControlPaneMenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * 
	 * Create a Glyph icon which can be used as graphics for various controls. 
	 * @param iconCode - the icon code to add
	 * @param size - the size of the icon in font size.
	 * @return the icon in a form to add to a control. 
	 */
	public static Text createPamGlyph(String iconCode, int size){
		FontIcon iconView = new FontIcon(iconCode);
		iconView.setIconSize(size);
		return iconView;
	}
	
  
	
	/**
	 * Highlight a button
	 * @param button - the button to highlight
	 */
	public static Animation highLightButton(Button button) {
		
		final Animation animation = new Transition() {

	        {
	            setCycleDuration(Duration.millis(3000));
	            setInterpolator(Interpolator.EASE_OUT);
	            setAutoReverse(true);
	            setCycleCount(Animation.INDEFINITE);
	        }

	        @Override
	        protected void interpolate(double frac) {
	            Color vColor = new Color(38/255., 194/255., 35/255., (1 - frac)*0.7);
	            button.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
	        }
	    };
	    
	    animation.play();
	    
	    return animation; 
		
	}
	
	/**
	 * Undo highlighting of button
	 * @param button - the button to de-highlight
	 */
	private void deHighLight(Button button) {
		if (currentAnimation!=null) currentAnimation.stop(); 
		button.setBackground(null);
	}
	
	/**
	 * Undo highlighting on all buttons
	 */
	private void deHighLightButtons() {
		deHighLight(browseButton);
		deHighLight(importClipsButton);
		//deHighLight(clusterButton);

	}

	/**
	 * Get the main pane.
	 * @return the main pane
	 */
	public Pane getPane() {
		return mainPane;
	}

	public void setUserPrompts(ArrayList<UserPrompt> userPromptsA) {
		deHighLightButtons() ;

		if (userPromptsA==null || userPromptsA.size()==0) {
			return;
		}
		
		UserPrompt userPrompt = userPromptsA.get(0); 
		switch (userPrompt) {
		case IMPORT_AGAIN:
		    this.currentAnimation = highLightButton(importClipsButton);
			break;
		case NOTHING_CLUSTERED_YET:
			//this.currentAnimation = highLightButton(clusterButton);
			break;
		case NOTHING_IMPORTED_YET:
			this.currentAnimation = highLightButton(browseButton);
			break;
		case RECREATE_IMAGES:
			break;
		case RE_CLUSTER:
			//this.currentAnimation = highLightButton(clusterButton);
			break;
		default:
			break;
		}
		
		
	}

	public void setControlButtonDisable(boolean disable) {
		browseButton.setDisable(disable);
		importClipsButton.setDisable(disable);
		//clusterButton.setDisable(disable);
	}


	
	

}
