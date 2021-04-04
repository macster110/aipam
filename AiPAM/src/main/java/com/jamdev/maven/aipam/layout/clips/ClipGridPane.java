package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;
import java.util.Collections;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.snapToGrid.ClusterSnapGrid;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
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
import javafx.scene.layout.VBox;

/**
 * Pane which shows all the clips.
 * 
 * @author Jamie Macualay 
 *
 */
public class ClipGridPane extends BorderPane {

	public static final double TILE_SPACING = 5; 
	
	/**
	 * The maximum allowed length of a clip
	 */
	public static final int MAX_CLIP_PIXEL_LEN = 1000; //4K  

	/**
	 * The default clip length
	 */
	public static final int DEFUALT_CLIP_PIXEL_LEN = 100; //4K  

	/**
	 * The tile pane which holds the tiles. 
	 */
	private VBox tilePane;

	/**
	 * The current clips. 
	 */
	public ArrayList<PamClipPane> currentPamClips;

	/**
	 * Reference to the view.
	 */
	private AIPamView aiPamView;

	/**
	 * The grid size to use. 
	 */
	private int[] gridSize;

	/** 
	 * The total time of all clips in the pane.  
	 * 
	 */
	private double totalClipTime = 0 ;

	public ClipGridPane(AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		this.setCenter(createPane());
	}

	/**
	 * Create the clip pane.
	 * @return the clip pane. 
	 */
	private Node createPane() {

		tilePane = new VBox();
		tilePane.setPadding(new Insets(5));
		tilePane.setSpacing(TILE_SPACING);
		//		tilePane.setVgap(2);
		//		tilePane.setHgap(2);
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


	public int[] getClipsSize(PAMClip pamClip, int nClips) {
		if (aiPamView.getAIParams().maximumClipLength>0) {
			//clips have been trimmed so they are all the same length
			return new int[] {DEFUALT_CLIP_PIXEL_LEN, DEFUALT_CLIP_PIXEL_LEN};
		}
		///clips have no trimming so the width needs to be reflect the clip length
		double medianLen = aiPamView.getAIControl().getAudioInfo().medianFilelength;
		double maxLen = aiPamView.getAIControl().getAudioInfo().maxFileLength; 
		
		//what if we have some absolutely massive canvas and some wee ones too - that is going to cause an issues. So 
		//have to make a decision as to what is going to be the maximum. 
		
//		System.out.println("maxLen: " + maxLen +  " medianLen " + medianLen 
//				+ "  PAMClip len: " + pamClip.getClipLength()
//				+ " r1: " + (medianLen/maxLen)
//				+ " r2: " + ((double) DEFUALT_CLIP_PIXEL_LEN/MAX_CLIP_PIXEL_LEN)); 

		
		int width; 
		if ((medianLen/maxLen) > ((double) DEFUALT_CLIP_PIXEL_LEN/MAX_CLIP_PIXEL_LEN)) {
			//we have a problem - need to use max length. 
			//todo BROKEM
			width = (int) (MAX_CLIP_PIXEL_LEN*pamClip.getClipLength()/maxLen);
		}
		else {
			 width = (int) (DEFUALT_CLIP_PIXEL_LEN*pamClip.getClipLength()/medianLen);
		}
		
		System.out.println("Width: " + width); 
		
		return new int[] {Math.min(MAX_CLIP_PIXEL_LEN, width), 100};
	}
	
	


	/**
	 * Task for spectrogram data. 
	 * @param pamClips - the pamClips to generate images for. 
	 * @return the 
	 */
	public Task<Integer> generateSpecImagesTask(ArrayList<PAMClip> pamClips) {

		if (pamClips==null) return null; 

		//work out the number of clips that have a spectrogram - these are hte only ones that will be displayed in the page. 
		int nClips =0 ; 
		totalClipTime = 0;
		for (PAMClip pamClip: pamClips) {
			if (pamClip.getSpectrogram(aiPamView.getAIParams().spectrogramParams.fftLength, 
					aiPamView.getAIParams().spectrogramParams.fftLength)!=null) {
				
				nClips++; 
				totalClipTime += pamClip.getClipLength();
			}
		}

		//		System.out.println("NCLIPS TO LOAD: " + nClips); 

		gridSize = ClusterSnapGrid.calcGridSize(nClips);
		//
		currentPamClips= new ArrayList<PamClipPane>(); 

		Task<Integer> task = new Task<Integer>() {

			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					this.updateTitle("Generating Clip Images");

					double memoryMB ;
					for (int i=0; i<pamClips.size(); i++) {
						final int ii=i; 

						//clips which have no pane are not shown. 
						if (pamClips.get(i).getSpectrogram(aiPamView.getAIParams().spectrogramParams.fftLength, 
								aiPamView.getAIParams().spectrogramParams.fftLength)==null) continue;

						int[] clipSize = getClipsSize(pamClips.get(i), pamClips.size()); 

						UtilsFX.runAndWait(() -> {
							//must run on FX thread as generates an image. 
							FeatureExtraction featureExtraction=null; 
							if (aiPamView.getAIParams().showFeatures) {
								featureExtraction=aiPamView.getAIControl().getFeatureExtractionManager().getCurrentFeatureExtractor(); 
							}

							final PamClipPane pamClipPane = new PamClipPane(pamClips.get(ii), clipSize[0], clipSize[1], 
									aiPamView.getAIParams().spectrogramParams,  aiPamView.getCurrentColourArray(), featureExtraction);
							pamClipPane.setSelectionManager(aiPamView.getClipSelectionManager()); 
							//add child on the fx pane
							//tilePane.getChildren().add(pamClipPane); 
							currentPamClips.add(pamClipPane);
							
							pamClips.get(ii).clearSpectrogram(); 

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
	 * @param - the size of the grid in width and height
	 */
	@SuppressWarnings("unchecked")
	public void layoutClips(){
		System.out.println("ClipGridPane: Layout clips: "); 
		//make sure that all the clips are cleared
		tilePane.getChildren().clear();
		//tilePane.setTileAlignment(Pos.CENTER_LEFT);


		//we need to find all the clips in order of their clip ID. Sort the list so 
		//grid ID's are in ascending order.
		//Collections.sort(currentPamClips); //FIXME - need to sort this out.

		//now simply add to the pane. 
		HBox hBox = null; 
		double rowTime = 0; 
		
		for (int i=0; i<currentPamClips.size(); i++) {
			
			if (isNewRow(i, rowTime)) {
				if (hBox!=null) {
					tilePane.getChildren().add(hBox);
				}
				hBox = new HBox(); 
				hBox.setSpacing(5);
				rowTime=0;
			}
			rowTime=rowTime+currentPamClips.get(i).getPamClip().getClipLength();
			hBox.getChildren().add(currentPamClips.get(i)); 
			//TilePane.setAlignment(currentPamClips.get(i), Pos.CENTER_LEFT);
		}
		tilePane.getChildren().add(hBox);
	}

	/**
	 * Check whether there is a new clip row. 
	 * @param i - the current clip index. 
	 * @param rowTime - the total time of clips in the current row in seconds. 
	 * @return true to start a new row. 
	 */
	private boolean isNewRow(int i, double rowTime) {
		if ( this.aiPamView.getAIParams().maximumClipLength==-1) {
			//check the total time of the row. 
			return rowTime>(totalClipTime/gridSize[1]) || i==0;
		}
		else {
			//the same number of clips per row so just use grid size
			return i%gridSize[0] == 0;
		}
	}

	/**
	 * The tile pane. 
	 * @return the pane which shows tiles. 
	 */
	public Pane getTilePane() {
		return tilePane;
	}

	/**
	 * Get the current clips. 
	 * @return the current clips.
	 */
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



}
