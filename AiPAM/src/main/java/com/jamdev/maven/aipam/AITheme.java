package com.jamdev.maven.aipam;

import javafx.scene.layout.Pane;
import jfxtras.styles.jmetro8.JMetro;

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
	 * The current theme. 
	 */
	private int currentTheme = JMETRO_DARK_THEME; 


	/**
	 * Get the current theme. 
	 * @return
	 */
	public int getCurrentTheme() {
		return currentTheme;
	}

	public AITheme() {

	}

	public void applyTheme(int type, Pane root) {
	currentTheme=type;
	root.getStylesheets().clear();
	switch (type) {
	case JMETRO_DARK_THEME:
		//apply JMetro theme
		new JMetro(JMetro.Style.DARK).applyTheme(root);
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	case JMETRO_LIGHT_THEME:
		//apply JMetro theme
		new JMetro(JMetro.Style.LIGHT).applyTheme(root);
		//add extra style sheet for fluent design menu buttons and tab pane.
		root.getStylesheets().add(getClass().getResource("fluentdesignextra.css").toExternalForm());
		//just add extra background for dark. 
		root.getStylesheets().add(getClass().getResource("fluentdesignextra_light.css").toExternalForm());
		root.setStyle("-fx-background: BACKGROUND;");
		break;
	}
}

}
