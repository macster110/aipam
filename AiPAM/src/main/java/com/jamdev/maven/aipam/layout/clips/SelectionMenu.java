package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.annotation.SimpleAnnotation;
import com.jamdev.maven.aipam.clips.PAMClip;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;

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


	/**
	 * Get the menu items for a set of selected clips. 
	 * @param selectedClips - the selected clips
	 * @return the applicable menu for the selected clips. 
	 */
	public ArrayList<MenuItem> getMenu(ArrayList<PamClipPane> selectedClips){

		ArrayList<MenuItem> menuItems; 
		if (selectedClips.size()==1) {
			menuItems = getMenu(selectedClips.get(0));  
		}
		else {
			menuItems=  getMultiMenu(selectedClips);
		}

		menuItems.add(new SeparatorMenuItem()); 
		MenuItem item= new MenuItem("Show Full Spectrogram..."); 
		item.setOnAction((action)->{
			openSpecDialog(selectedClips.get(selectedClips.size()-1).getPamClip());
		});
		menuItems.add(item);

		return menuItems; 

	}


	/**
	 * Get the menu items when multiple clips are sleected 
	 * @param selectedClips - a single clip
	 * @return the menu items for a single selected clip
	 */
	public ArrayList<MenuItem> getMultiMenu(ArrayList<PamClipPane> selectedClips){

		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(); 

		//create the annotation actions. 
		List<SimpleAnnotation> annotations = aiPamController.getAnnotationManager().getAnnotationsList(); 

		/***Menu items we need***/
		//Add clips to group xx
		//Add/Move clips to group xx
		//-------
		//remove all clips form xx1 xx2 xx3

		//how many annotations are the clips are a part of 
		List<SimpleAnnotation> clipAnnotations = new ArrayList<SimpleAnnotation>(); 
		for (PamClipPane pamClipPane: selectedClips) {
			if (pamClipPane.getPamClip().getAnnotation()!=null &&
					clipAnnotations.indexOf(pamClipPane.getPamClip().getAnnotation())==-1) {
				clipAnnotations.add((SimpleAnnotation) pamClipPane.getPamClip().getAnnotation()); 
			}
		}

		MenuItem item;
		if (annotations.size()>0) {

			String addString = "Add clips to ";
			if (clipAnnotations.size()>0) {
				addString = "Add/Move clips to "; //will be changing parent annotation 
			}

			for (int i=0; i<annotations.size(); i++) {
				annotations.get(i).getAnnotationGroupSymbol(); 
				item = new MenuItem(addString + 	annotations.get(i).getAnnotationGroupName());
				item.setGraphic(annotations.get(i).getAnnotationGroupSymbol());
				final int ii = i; 
				item.setOnAction((action)->{
					for (PamClipPane pamClipPane: selectedClips) {
						annotations.get(ii).addClip(pamClipPane.getPamClip()); 
					}
				});
				menuItems.add(item); 
			}
		}
		else menuItems.add(new MenuItem("Add some annotations in the Annotation Pane")); 

		//option to remove clip from menu item. 
		if (clipAnnotations.size()>0) {
			if (menuItems.size()>0) menuItems.add(new SeparatorMenuItem()); 
			
			item= new MenuItem("Remove all clips from groups");

			//does not work because duplicate children
//			HBox symbolGraphic = new HBox(); 
//			symbolGraphic.setSpacing(5);
//			for (SimpleAnnotation anno:clipAnnotations) {
//				symbolGraphic.getChildren().add(anno.getAnnotationGroupSymbol());
//			}

			//item.setGraphic(symbolGraphic);
			item.setOnAction((action)->{
				for (PamClipPane pamClipPane: selectedClips) {
					SimpleAnnotation anno =  (SimpleAnnotation) pamClipPane.getPamClip().getAnnotation(); 
					if (anno!=null) anno.remove(pamClipPane.getPamClip()); //can be null if annotation and non annotated groups selected. 
				}
			});
			menuItems.add(item); 
		}
		
		return menuItems; 


	}

	/**
	 * Get the menu items for a single clip. 
	 * @param selectedClips - a single clip
	 * @return the menu items for a single selected clip
	 */
	public ArrayList<MenuItem> getMenu(PamClipPane selectedClips) {

		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(); 

		//create the annotation actions. 
		List<SimpleAnnotation> annotations = aiPamController.getAnnotationManager().getAnnotationsList(); 


		MenuItem item;
		if (annotations.size()>0) {

			String addString = "Add clip to ";
			if (selectedClips.getPamClip().getAnnotation()!=null) {
				addString = "Move clip to "; //will be changing parent annotation 
			}

			for (int i=0; i<annotations.size(); i++) {
				if (annotations.get(i)!=selectedClips.getPamClip().getAnnotation()) {
					annotations.get(i).getAnnotationGroupSymbol(); 
					item = new MenuItem(addString + 	annotations.get(i).getAnnotationGroupName());
					item.setGraphic(annotations.get(i).getAnnotationGroupSymbol());
					final int ii = i; 
					item.setOnAction((action)->{
						annotations.get(ii).addClip(selectedClips.getPamClip()); 
					});
					menuItems.add(item); 
				}
			}
		}
		else menuItems.add(new MenuItem("Add some annotations in the Annotation Pane")); 

		//option to remove clip from menu item. 
		if (selectedClips.getPamClip().getAnnotation()!=null) {
			if (menuItems.size()>0) menuItems.add(new SeparatorMenuItem()); 
			item= new MenuItem("Remove from: " + selectedClips.getPamClip().
					getAnnotation().getAnnotationGroupName()); 
			item.setGraphic(selectedClips.getPamClip().getAnnotation().getAnnotationGroupSymbol());
			item.setOnAction((action)->{
				SimpleAnnotation anno =  (SimpleAnnotation) selectedClips.getPamClip().getAnnotation(); 
				anno.remove(selectedClips.getPamClip()); 
			});
			menuItems.add(item); 
		}


		return menuItems;
	}

	/**
	 * Open the spectrogram dialog. 
	 * @param pamClip - the pma clip. 
	 */
	private void openSpecDialog(PAMClip pamClip){

		ButtonType closeButtonType = new ButtonType("Close", ButtonData.OK_DONE);
		Dialog<String> dialog = new Dialog<>();
		dialog.getDialogPane().getButtonTypes().add(closeButtonType);

		
		dialog.getDialogPane().setContent(new FullClipPane(pamClip));
		//dialogPane.setContent(new Label("Hello"));

		dialog.showAndWait();

	}

}
