package com.jamdev.maven.aipam.layout.deeplearning;

import java.io.File;
import java.io.InputStream;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.PamSVGIcon;

import afester.javafx.svg.GradientPolicy;
import afester.javafx.svg.SvgLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DeepLearningPane extends DynamicSettingsPane<AIPamParams>{
	
	private BorderPane mainPane;

	public DeepLearningPane(AIPamView aiPamView) {
		this.mainPane = new BorderPane(new Label("TODO"));
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(AIPamParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getIcon() {
		
//		// load the svg file
//	    InputStream svgFile = 
//	          getClass().getResourceAsStream("noun_Deep Learning_2486374.svg");
//	    SvgLoader loader = new SvgLoader();
//	    loader.setGradientTransformPolicy(GradientPolicy.DISCARD);
//	    Group svgImage = loader.loadSvg(svgFile);
//	    
////	    SVGOMDocument svgImageDoc = loader.loadSvgDocument(svgFile);
//
//	    // Scale the image and wrap it in a Group to make the button 
//	    // properly scale to the size of the image  
//	    svgImage.setScaleX(0.3);
//	    svgImage.setScaleY(0.3);
//	   
//	    setSVGCol(svgImage); 
//	    
//		return svgImage;

		//sometimes loading the SVG is best using the method below - sometimes using the method above...meh

		try {
			int  size= 20;
			PamSVGIcon svgsprite = PamSVGIcon.getInstance().create(new File(getClass().getResource("noun_Deep Learning_2486374.svg").toURI()), Color.WHITE);
//			svgsprite.getSpriteNode().setStyle("-fx-text-color: white");				
			svgsprite.getSpriteNode().setStyle("-fx-fill: TEXT_FILL");
			svgsprite.setFitHeight(size);
			svgsprite.setFitWidth(size);
			return svgsprite.getSpriteNode(); 
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	private void setSVGCol(Group svgImage) {
//	    for (int i=0; i<svgImage.getChildren().size(); i++) {
//	    	if (svgImage.getChildren().get(i) instanceof Group) {
//	    		 setSVGCol((Group) svgImage.getChildren().get(i)); 
//	    	}
//	    	System.out.println("Node: " + svgImage.getChildren().get(i)); 
//	    	svgImage.getChildren().get(i).setStyle("-fx-fill: white;");
//	    }
//	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Deep Learning";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getDescription() {
		return "Deep learning. Allows importing a running "
				+ "of deep learning models.";
	}

}
