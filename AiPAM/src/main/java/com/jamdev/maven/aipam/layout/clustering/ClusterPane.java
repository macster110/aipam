package com.jamdev.maven.aipam.layout.clustering;

import java.io.InputStream;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.clustering.tsne.TSNEParams;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.TSNEClusterPane;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsListener;

import afester.javafx.svg.SvgLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Pane for clustering algorithms.
 * <p>
 * Currently this pane simply holds the TSNE algorithm settings. 
 * If there were another clustering algorithm then this pane 
 * would allows users to select algorithms and change the settings
 * controls appropriately.
 * 
 * @author Jamie Macaulay 
 *
 */
public class ClusterPane extends DynamicSettingsPane<AIPamParams> {
	
	/**
	 * The default TSNE cluster pane.  
	 */
	public TSNEClusterPane clusterPane;

	/**
	 * Reference to the AIPAMView. 
	 */
	private AIPamView aiPamView; 
	
	private Pane mainPane; 
	
	public ClusterPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView;
		mainPane= createPane(); 

	}
	
	private Pane createPane() {
		
		Label  titleLabel = new Label("TSNE Settings"); 
		titleLabel.getStyleClass().add("label-title1");
		
		clusterPane= new TSNEClusterPane(); 
		
		VBox holder = new VBox(); 
		holder.setSpacing(5);
		
		holder.getChildren().addAll(titleLabel, clusterPane.getPane()); 
		
		return holder; 
	}

	
	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		//for now only using TSNE...in future if using different algorithms this will need updated. 
		paramsIn.clusterParams = clusterPane.getParams((TSNEParams) paramsIn.clusterParams); 
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		clusterPane.setParams((TSNEParams) params.clusterParams);
	}

	@Override
	public Node getIcon() {
		try {
//			PamSVGIcon svgsprite =  PamSVGIcon.getInstance().create(new File(getClass().getResource("Cluster.svg").toURI()), Color.WHITE);
//			svgsprite.getSpriteNode().setStyle("-fx-text-color: white");				
//			svgsprite.getSpriteNode().setStyle("-fx-fill: white");
//			svgsprite.setFitHeight(size);
//			svgsprite.setFitWidth(size);
//			return svgsprite.getSpriteNode(); 
			
			
			// load the svg file
		    InputStream svgFile = 
		          getClass().getResourceAsStream("Cluster.svg");
		    SvgLoader loader = new SvgLoader();
		    //loader.setGradientTransformPolicy(GradientPolicy.DISCARD);
		    Group svgImage = loader.loadSvg(svgFile);
		    
//		    SVGOMDocument svgImageDoc = loader.loadSvgDocument(svgFile);

		    // Scale the image and wrap it in a Group to make the button 
		    // properly scale to the size of the image  
		    svgImage.setScaleX(1);
		    svgImage.setScaleY(1);
		    
		    setSVGCol(svgImage); 


		    return svgImage; 
		    
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
//		//get the cluster image 
//ImageView icon = new ImageView(aiPamView.getClusterIcon()); 
//		ColorAdjust colorAdjust = new ColorAdjust();
//		colorAdjust.setBrightness(1);
//		icon.setEffect(colorAdjust);
//		
//		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.GEARS); 
//		iconView.setGlyphSize(8);
//		iconView.setFill(Color.WHITE);
//		
//		StackPane stackPane = new StackPane(); 
//		
//		stackPane.getChildren().addAll(icon, iconView); 
//		StackPane.setAlignment(iconView, Pos.TOP_RIGHT);
		
//		return stackPane;
	}
	
	private void setSVGCol(Group svgImage) {
	    for (int i=0; i<svgImage.getChildren().size(); i++) {
	    	if (svgImage.getChildren().get(i) instanceof Group) {
	    		 setSVGCol((Group) svgImage.getChildren().get(i)); 
	    	}
	    	//System.out.println("Node: " + svgImage.getChildren().get(i)); 
	    	svgImage.getChildren().get(i).setStyle("-fx-fill: white;");
	    }
	}

	@Override
	public String getTitle() {
		return "Cluster Algorithm";
	}
	
	@Override
	public void addSettingsListener(SettingsListener settingsListener){
		//add a settings listener to the cluster instead as this is just a holder pane. 
		clusterPane.addSettingsListener(settingsListener);
	} 

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return "Unsupervised machine learning such as \n"
				+ "clustering algorithms e.g. TSNE";
	}

}
