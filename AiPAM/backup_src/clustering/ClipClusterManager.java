package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.snapToGrid.ClusterSnapGrid;
import com.jamdev.maven.aipam.clustering.snapToGrid.SnapToGridListener;
import com.jamdev.maven.aipam.clustering.tsne.TSNEClipClustererYK;
import com.jmatio.types.MLStructure;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 * Manages clustering on different threads. 
 * 
 * 
 * @author Jamie Macaulay
 *
 */
public class ClipClusterManager {

	/**
	 * The current clustering algorithm
	 */
	private ClusteringAlgorithm clusterAlgorithm; 
	
	/**
	 * The snap to grid algorithm. 
	 */
	private ClusterSnapGrid clusterSnapGrid = new ClusterSnapGrid(); 
	
	public ClipClusterManager(AiPamController aiPamControl) {
		 clusterAlgorithm = new TSNEClipClustererYK(aiPamControl); 
	}

	/**
	 * Construct the cluster data task. 
	 * @param pamClips  - the pamClips
	 * @param params 	- the aiPamParams
	 * @return the clustering task.
	 */
	public Task<Integer> clusterDataTask(ArrayList<PAMClip> pamClips, AIPamParams params) {
		return new ClusterTask(pamClips, params); 
	}


	public class ClusterTask extends Task<Integer> {

		private ArrayList<PAMClip> pamClips;
		
		private AIPamParams params;

		public ClusterTask(ArrayList<PAMClip> pamClips, AIPamParams params) {
			this.params=params; 
			this.pamClips=pamClips; 		
		}

		@Override
		protected Integer call() throws Exception {
			//progress is in intermediate mode. 
			//System.out.println("Hello: Start clustering");
			try {

				updateTitle("Clustering");
				updateMessage("Initialising..."); 
				updateProgress(0, 1);
				updateProgress(ProgressBar.INDETERMINATE_PROGRESS, 1);

				if (clusterAlgorithm.getTrainingListener()!=null) {
					clusterAlgorithm.getTrainingListener().progressProperty().addListener((obsval, oldval, newval)->{
						updateProgress(newval.doubleValue(), 1);
						updateMessage("Processing..."); 
					});
				}

				//start the algorithm 
				updateMessage("Using TSNE to cluster clips...This could take some time"); 

				if (pamClips==null) return -1; 
				//cluster data is stored in the clips. 
				clusterAlgorithm.cluster(pamClips,  params.clusterParams);
				
				
				clusterSnapGrid.setListener(new SnapToGridListener()); 
				
				//start snapping to gird.
				updateMessage("Snapping the cluster points to a grid...this can also take some time"); 
				if (clusterSnapGrid.getListener()!=null) {
					clusterSnapGrid.getListener().assigmentProgressProperty().addListener((obsval, oldval, newval)->{
						updateProgress(newval.doubleValue(), 1);
						if (clusterSnapGrid.getListener()!=null && this.isCancelled()) {
							clusterSnapGrid.getListener().setCancelled(true);
							//should now exit gracefully. 
						}
					});
					
					clusterSnapGrid.getListener().assignmentMessageProperty().addListener((obsval, oldval, newval)->{
						updateMessage("Snapping to grid: " + newval);
						if (clusterSnapGrid.getListener()!=null && this.isCancelled()) {
							clusterSnapGrid.getListener().setCancelled(true);
							//should now exit gracefully. 
						}
					});
				}

				
				clusterSnapGrid.snapToGrid(pamClips); 

				//System.out.println("Hello: Finished!!!!");
				return 1; 
			}
			catch (Exception e) {
				e.printStackTrace();
				return -1; 
			}
		}
		
        @Override 
        protected void cancelled() {
            super.cancelled();
			if (clusterSnapGrid.getListener()!=null) {
				clusterSnapGrid.getListener().setCancelled(true);
				//should now exit gracefully. 
			}
            updateMessage("Cancelled!");
        }
	}


	public Params struct2ClusterParams(MLStructure clusterParams) {
		clusterParams.getField("type"); 
		
		// TODO Auto-generated method stub -switch statement here
		return null;
	}



}
