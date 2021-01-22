package com.jamdev.maven.aipam.layout.utilsFX;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Load SVG icons. These are prefferrable to images because they scale nicely between different resolution
 * displays. code adapted from https://github.com/DeskChan/DeskChan
 * @author JamieMacaulauy
 *
 */
public class PamSVGIcon {
	
	public static PamSVGIcon instance; 

    private String path = null;

	private XPathFactory xpf;

	private XPath xpath;

	private XPathExpression expression;

    public static boolean canRead(File path){
        return path.getName().endsWith(".svg");
    }
    
    public static PamSVGIcon getInstance() {
    	if (instance ==null) {
    		instance = new  PamSVGIcon(); 
    	}
		return instance;
    }

    public PamSVGIcon create(File path, Color color) throws Exception {
    	
    	System.out.println("Create icon start");
    	
    	String col = UtilsFX.toRGBCode(color);

        Document document = getDocument(path.toString());

        Insets margin = getMarginFromFile(document);

       
        NodeList svgPaths = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
        
    	System.out.println("Create icon start: " + svgPaths.getLength());

        ArrayList<SVGPath> shapes = new ArrayList<>();
        for(int i=0; i<svgPaths.getLength(); i++) {
            try {
                SVGPath shape = new SVGPath();
                NamedNodeMap map = svgPaths.item(i).getAttributes();
                shape.setContent(map.getNamedItem("d").getTextContent());
                if(map.getNamedItem("style") != null) {
                    shape.setStyle(convertStyle(map.getNamedItem("style").getTextContent()));
                } else {
                    shape.setStyle("-fx-fill: "+col+"; -fx-stroke-width: 2;");
                }
                shapes.add(shape);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        SVGPath[] shapesPaths = shapes.toArray(new SVGPath[shapes.size()]);
        
        String textStyle = getTextStyle(document);

        return new PamSVGIcon(shapesPaths, textStyle, margin, path);

    
    }
    
//	/**
//	 * Get an SVG icon.
//	 * @param resourcePath - the path from the src folder
//	 * @return a node for the SVG icon. 
//	 */
//	public Node getSVGIcon(String resourcePath, int size) {
//		try {
//			PamSVGIcon svgsprite = PamSVGIcon.create(new File(getClass().getResource(resourcePath).toURI()));
//			svgsprite.getSpriteNode().setStyle("-fx-text-color: black");				
//			svgsprite.getSpriteNode().setStyle("-fx-fill: black");
//			svgsprite.setFitHeight(size);
//			svgsprite.setFitWidth(size);
//			return svgsprite.getSpriteNode(); 
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null; 
//	}
    
    protected static Document getDocument(String path){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new File(path).toURI().toURL().toString());
        } catch (Exception e) {
            return null;
        }
    }


    private SVGPath[] svgParts;
    private double originWidth, originHeight;
	private Node sprite;
	private String contentStyke;
	private Insets margin;

    public PamSVGIcon(SVGPath[] shapes, String contentStyle, Insets margin, File path) {
        this.sprite = new Group(shapes);
        this.contentStyke = contentStyle;
        this.margin = margin;
        svgParts = shapes;
        originWidth = getFitWidth();
        originHeight = getFitHeight();
        this.path = path != null ? path.toString() : null;
    }

    public PamSVGIcon() {
        xpf = XPathFactory.newInstance();
        xpath = xpf.newXPath();
        try {
			expression = xpath.compile("//path");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double getOriginWidth(){
        return originWidth;
    }

    public double getOriginHeight(){
        return originHeight;
    }


    public void setFitWidth(double width)  {
        for (SVGPath path : svgParts)
            path.setScaleX(width / originWidth);
    }

    public void setFitHeight(double height){
        for (SVGPath path : svgParts)
            path.setScaleY(height / originHeight);
    }
    
    protected static Insets getMarginFromFile(Document document){
        Insets standard = new Insets(30, 30, 30, 30);
        if (document == null)
            return standard;

        try {
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile("//margin");

            NamedNodeMap marginTags = ((NodeList) expression.evaluate(document, XPathConstants.NODESET)).item(0).getAttributes();
            return new Insets(
                    Double.parseDouble(marginTags.getNamedItem("top").getTextContent()),
                    Double.parseDouble(marginTags.getNamedItem("right").getTextContent()),
                    Double.parseDouble(marginTags.getNamedItem("bottom").getTextContent()),
                    Double.parseDouble(marginTags.getNamedItem("left").getTextContent())
            );
        } catch (Exception e) {
            return standard;
        }
    }

    public static String getTextStyle(Document document){
        String standard = "-fx-alignment: center; -fx-text-alignment: center; -fx-content-display: center;";
        if (document == null)
            return standard;

        try {
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile("//text");

            NamedNodeMap colorTag = ((NodeList) expression.evaluate(document, XPathConstants.NODESET)).item(0).getAttributes();
            return convertStyle(colorTag.getNamedItem("style").getTextContent());
        } catch (Exception e) {
            return standard;
        }
    }

    protected static String convertStyle(String style){
        String[] styleLines = style.split(";");
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < styleLines.length; j++) {
            styleLines[j] = styleLines[j].trim();
            if (styleLines[j].length() == 0) continue;
            result.append("-fx-");
            result.append(styleLines[j].trim());
            result.append("; ");
        }
        return result.toString();
    }

    public String getSpritePath(){ return path; }
    
    public Node getSpriteNode(){ return sprite; }
    
    public double getFitWidth() {  return sprite.getLayoutBounds().getWidth();   }

    public double getFitHeight(){  return sprite.getLayoutBounds().getHeight();  }
}