package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.annotation.SimpleAnnotation;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Selection menu. Supplies the menu items and actions for a selected PAMClip
 * 
 * @author Jamie Macaulay 
 *
 */
public class SelectionMenu {

	/**
	 * Reference to the controller. 
	 */
	private AiPamController aiPamController;


	public SelectionMenu(AiPamController aiPamController) {
		this.aiPamController=aiPamController; 
	}


	public ArrayList<MenuItem> getMenu(PamClipPane pamClipPane) {

		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(); 

		//create the annotation actions. 
		List<SimpleAnnotation> annotations = aiPamController.getAnnotationManager().getAnnotationsList(); 
		
		MenuItem item;
		if (annotations.size()>1) {
			String addString = "Add clip to ";
			if (pamClipPane.getPamClip().getAnnotation()!=null) {
				addString = "Move clip to "; //will be changing parent annotation 
			}

			for (int i=0; i<annotations.size(); i++) {
				annotations.get(i).getAnnotationGroupSymbol(); 
				item = new MenuItem(addString + 	annotations.get(i).getAnnotationGroupName());
				item.setGraphic(annotations.get(i).getAnnotationGroupSymbol());
				final int ii = i; 
				item.setOnAction((action)->{
					annotations.get(ii).addClip(pamClipPane.getPamClip()); 
				});
				menuItems.add(item); 
			}

		}
		else menuItems.add(new MenuItem("Add some annotations in the Annotation Pane")); 
		
		//option to remove clip from menu item. 
		if (pamClipPane.getPamClip().getAnnotation()!=null) {
			menuItems.add(new SeparatorMenuItem()); 
			item= new MenuItem("Remove from: " + pamClipPane.getPamClip().getAnnotation().getAnnotationGroupName()); 
			item.setGraphic(pamClipPane.getPamClip().getAnnotation().getAnnotationGroupSymbol());
			item.setOnAction((action)->{
				pamClipPane.getPamClip().setAnnotation(null);;  
			});
		}
		
		menuItems.add(new SeparatorMenuItem()); 
		
		item= new MenuItem("Show Full Spectrogram..."); 
		item.setOnAction((action)->{
			//TODO - show full spectrogram of clip; 
		});
		menuItems.add(item); 

	
		return menuItems;
	}

}
