package com.jamdev.maven.aipam.layout.utilsFX;


import org.w3c.dom.Document;

import com.jamdev.maven.aipam.layout.AIPamView;

import de.codecentric.centerdevice.javafxsvg.dimension.DefaultDimensionProvider;
import de.codecentric.centerdevice.javafxsvg.dimension.Dimension;

/**
 * The dimension provider for SVG icons
 */
public class AISVGDimensionProvider extends DefaultDimensionProvider  {
	
	@Override
	public Dimension getDimension(Document document){
		return new Dimension(AIPamView.iconSize, AIPamView.iconSize);
	}

}
