package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;

import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;
import com.jamdev.maven.aipam.layout.utilsFX.ZoomableScrollPane;
import com.jamdev.maven.clips.PAMClip;

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
		tilePane.setVgap(4);
		tilePane.setHgap(4);
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

		int gridSize = (int) Math.sqrt(pamClips.size()); 
		tilePane.setPrefColumns(gridSize);

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
	 * Layout all the clips. 
	 */
	public void layoutClips(){
		for (PamClipPane clip: currentPamClips) {
			this.tilePane.getChildren().add(clip); 
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
