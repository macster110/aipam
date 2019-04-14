package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;
import java.util.Collections;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.snapToGrid.ClusterSnapGrid;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;
import com.jamdev.maven.aipam.layout.utilsFX.ZoomableScrollPane;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;

/**
 * Pane which shows all the clips.
 * 
 * @author Jamie Macualay 
 *
 */
public class ClipGridPane extends BorderPane {

	/**
	 * The tile pane which holds the tiles. 
	 */
	private TilePane tilePane;

	/**
	 * The current clips. 
	 */
	public ArrayList<PamClipPane> currentPamClips;

	public ArrayList<PamClipPane> getCurrentPamClips() {
		return currentPamClips;
	}

	/**
	 * Get current clips. 
	 * @return the current clips. 
	 */
	public ArrayList<PamClipPane> getCurrentClipPanes() {
		return currentPamClips;
	}

	/**
	 * Reference to the view.
	 */
	private AIPamView aiPamView;

	public ClipGridPane(AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		this.setCenter(createPane());
	}

	/**
	 * Create the clip pane.
	 * @return the clip pane. 
	 */
	private Node createPane() {
		tilePane = new TilePane();
		tilePane.setPadding(new Insets(5));
		tilePane.setVgap(2);
		tilePane.setHgap(2);
		//tilePane.setPrefColumns(4);

		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(tilePane); 

		//        StackPane stack = new StackPane();
		//        stack.getChildren().add(tilePane);
		//        stack.setStyle("-fx-background-color: blue;");

		ZoomableScrollPane sp = new ZoomableScrollPane(hbox);
		//        sp.setFitToHeight(true);
		//        sp.setFitToWidth(true);

		// dont grow more than the preferred number of columns:
		tilePane.setMaxWidth(Region.USE_PREF_SIZE);
		//        ZoomableScrollPane sp = new ZoomableScrollPane(tilePane);
		//        tilePane.setStyle("-fx-background-color: #1d1d1d; -fx-font: 12px Segoe; -fx-text-fill: white;");
		//        sp.setFitToHeight(true);
		//        sp.setFitToWidth(true);
		//        sp.setContent(tilePane);

		return sp;
	}

	/**
	 * Clear all spectrogram images. 
	 */
	public void clearSpecImages() {
		tilePane.getChildren().clear(); 
	}


	public int[] getClipsSize(int nClips) {
		return new int[] {100,100};
	}

	/**
	 * Task for spectrogram data. 
	 * @param pamClips - the pamClips to generate images for. 
	 * @return the 
	 */
	public Task<Integer> generateSpecImagesTask(ArrayList<PAMClip> pamClips) {
		
		if (pamClips==null) return null; 

		int[] gridSize = ClusterSnapGrid.calcGridSize(pamClips.size());
		
		tilePane.setPrefColumns(gridSize[0]);
//
		currentPamClips= new ArrayList<PamClipPane>(); 

		Task<Integer> task = new Task<Integer>() {

			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					this.updateTitle("Generating Clip Images");

					int[] clipSize = getClipsSize(pamClips.size()); 

					double memoryMB ;
					for (int i=0; i<pamClips.size(); i++) {
						final int ii=i; 
						
						UtilsFX.runAndWait(() -> {
							//must run on FX thread as generates an image. 
							final PamClipPane pamClipPane= new PamClipPane(pamClips.get(ii), clipSize[0], clipSize[1], aiPamView.getCurrentColourArray(), aiPamView.getClims());
							pamClipPane.setSelectionManager(aiPamView.getClipSelectionManager()); 
							//add child on the fx pane
							//tilePane.getChildren().add(pamClipPane); 
							currentPamClips.add(pamClipPane);
						});


						this.updateProgress(i, pamClips.size());

						memoryMB = Runtime.getRuntime().totalMemory()/1000./1000.; 
						this.updateMessage(String.format("Generating image %d of %d | Memory usage: %.2f MB",i,  pamClips.size(), memoryMB));
					}
					
					updateProgress(-1, pamClips.size());
					memoryMB = Runtime.getRuntime().totalMemory()/1000./1000.; 
					updateMessage(String.format("Drawing %d  images | Memory usage: %.2f MB",  pamClips.size(), memoryMB));
					//need to do this all in a oner or get this classic NGCnvas null pointer. 
					Platform.runLater(()->layoutClips()); 

					//Thread.sleep(3000); // give the FX thread some time to load the images with scroll bar still showing 
					return pamClips.size();

				}
				catch (Exception e) {
					e.printStackTrace();
					return -1; 
				}
			}
		};
		return task; 
	}

	/**
	 * Layout all the clips in order of their gridID flag. 
	 */
	@SuppressWarnings("unchecked")
	public void layoutClips(){
		System.out.println("ClipGridPane: Layout clips: "); 
		//make sure that all the clips are cleared
		tilePane.getChildren().clear();
		
		//we need to find all the clips in order of their clip ID. Sort the list so 
		//grid ID's are in ascending order.
		Collections.sort(currentPamClips);
		
		//now simply add to the pane. 
		for (int i=0; i<currentPamClips.size(); i++) {
			this.tilePane.getChildren().add(currentPamClips.get(i)); 
		}
	}

	/**
	 * The tile pane. 
	 * @return
	 */
	public Pane getTilePane() {
		return tilePane;
	}
	

}
