package com.jamdev.maven.aipam.layout.utilsFX;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBase;
import javafx.scene.layout.VBox;

/**
 * A menu pane which knows which button in a list of buttons is selected. Simple
 * highlights the last selected button in a list. 
 *<p>
 * This can probably be done more elgantly but meh. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class FluentMenuPane extends VBox{

	private ArrayList<ButtonBase> buttonBase = new ArrayList<ButtonBase>(); 

	public void addMenuItem(ButtonBase menuButton) {
		buttonBase.add(menuButton);

		//the button needs to know when it is selected. 
		menuButton.addEventHandler(ActionEvent.ACTION, (action)->
		{
			//return buttons to default state
			setMenuButtonsDeselected();
			//button is in slected state. 
			buttonSelected(menuButton); 
		});
		//set button to default state
		defaultButton(menuButton); 
		this.getChildren().add(menuButton); 
	}

	/**
	 * Show that a button is in selected state. 
	 * @param selectedButton - the selected button. 
	 */
	public void buttonSelected(ButtonBase selectedButton) {
		selectedButton.setStyle("-fx-border-color: transparent "
				+ "transparent transparent ACCENT_COLOR; -fx-border-width: 5px;");

	}
	
	/**
	 * Show that a button is in selected state. 
	 * @param index - index of the button to select in the list. 
	 */
	public void buttonSelected(int index) {
		buttonSelected(buttonBase.get(index)); 
	}

	
	/**
	 * Show that a button is ins selected state. 
	 * @param selectedButton - the selected button. 
	 */
	public void defaultButton(ButtonBase returnToDefault) {
		returnToDefault.setStyle("-fx-border-color: transparent; -fx-border-width: 5px;"); 
	}
	
	/**
	 * Deslect all the menu buttons. 
	 */
	public void setMenuButtonsDeselected() {
		for (int i=0; i<buttonBase.size(); i++) {
			defaultButton(buttonBase.get(i));
		}
	}

}
