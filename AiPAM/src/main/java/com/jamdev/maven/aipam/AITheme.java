package com.jamdev.maven.aipam;

import com.pixelduke.transit.TransitTheme;

import javafx.scene.layout.Pane;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class AITheme extends AIPamApp {

	/**
	 * JMetro dark theme
	 */
	public final static int JMETRO_DARK_THEME = 0; 

	/**
	 * JMetro light theme
	 */
	public final static int JMETRO_LIGHT_THEME = 1; 
	
	/**
	 * JMetro dark theme
	 */
	public final static int TRANSIT_DARK_THEME = 2; 

	/**
	 * JMetro light theme
	 */
	public final static int TRANSIT_LIGHT_THEME = 3; 

	/**
	 * The current theme. 
	 */
	private int currentTheme = TRANSIT_DARK_THEME; 


	/**
	 * Get the current theme. 
	 * @return the current theme
	 */
	public int getCurrentTheme() {
		return currentTheme;
	}

	public AITheme() {
		//constructor for the AI theme
	}

	public void applyTheme(int type, Pane root) {
	currentTheme = type;
	root.getStylesheets().clear();
	if (root.getScene() == null) {
		// Scene is not set yet, cannot apply theme
		System.err.println("Warning: Cannot apply theme because Pane is not attached to a Scene.");
		return;
	}
	switch (type) {
	case JMETRO_DARK_THEME:
		//apply JMetro theme
		new JMetro(Style.DARK).setScene(root.getScene());
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	case JMETRO_LIGHT_THEME:
		//apply JMetro theme
		new JMetro(Style.LIGHT).setScene(root.getScene());
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		//just add extra background for dark. 
		root.getStylesheets().add(getClass().getResource("fluentdesignextra_light.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	case TRANSIT_DARK_THEME:
		//apply JMetro theme
		new TransitTheme(com.pixelduke.transit.Style.DARK).setScene(root.getScene());
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	case TRANSIT_LIGHT_THEME:
	
		//apply JMetro theme
		new TransitTheme(com.pixelduke.transit.Style.LIGHT).setScene(root.getScene());
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		//just add extra background for dark. 
		root.getStylesheets().add(getClass().getResource("fluentdesignextra_light.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	}
}

}