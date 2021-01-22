package com.jamdev.maven.aipam.layout;

import java.io.Serializable;

import com.jamdev.maven.aipam.layout.utilsFX.InfernoColorMap;

import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * A colour map contains information to create a colour map and the correct CSS to colour components. 
 * @author Doug Gillespie, modified by Jamie Macaulay to use JavaFX classes
 *
 */
public class ColourArray implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;

	private Color[] colours;
	
	private Color contrastingColor;

	/**
	 * The current colour array type for the color list. 
	 */
	private ColourArrayType type; 
	
	private ColourArray() {
		
	}

	public static enum ColourArrayType{GREY, REVERSEGREY, BLUE,  GREEN, RED, HOT, FIRE, PARULA, PATRIOTIC, MATLAB,INFERNO}
	
	public static String getName(ColourArrayType type) {
		switch (type){
		case GREY:
			return "Grey (black to white)";
		case REVERSEGREY:
			return "Grey (white to black)";
		case BLUE:
			return "Blue";
		case GREEN:
			return "Green";
		case RED:
			return "Red";
		case HOT:
			return "Hot (multicoloured)";
		case INFERNO:
			return "Inferno (multicoloured)";
		case FIRE:
			return "Fire (multicoloured)";
		case PATRIOTIC:
			return "Red-White-Blue";
		case PARULA:
			return "Parula (multicoloured)";
		case MATLAB:
			return "Pastel (multicoloured)";
		default:
			return "Unknown";
		}
	}
	
	
	/**
	 * Get the color array type from it's string name. 
	 * @param name - string name of color array
	 * @return
	 */
	public static ColourArrayType getColorArrayType(String name){
		switch (name){
		case "Grey (black to white)":
			return ColourArrayType.GREY;
		case "Grey (white to black)":
			return ColourArrayType.REVERSEGREY;
		case "Blue":
			return ColourArrayType.BLUE;
		case "Green":
			return ColourArrayType.GREEN;
		case "Red":
			return ColourArrayType.RED;
		case "Hot (multicoloured)":
			return ColourArrayType.HOT;
		case "Inferno (multicoloured)":
			return  ColourArrayType.INFERNO;
		case "Fire (multicoloured)":
			return ColourArrayType.FIRE;
		case "Red-White-Blue":
			return ColourArrayType.PATRIOTIC;
		case "Parula (multicoloured)":
			return ColourArrayType.PARULA;
		case "Pastel (multicoloured)":
			return ColourArrayType.MATLAB;
		default:
			return ColourArrayType.GREY;
		}
	}
	
	/**
	 * Get the ColourArrayType from it's string name
	 * @param name - the string name of the colour array type
	 * @return the ColourArrayType for the string name. Null if no ColourArrayType found. 
	 */
	public static ColourArrayType getColourArrayType(String name){
		
		for (int i=0; i<ColourArrayType.values().length; i++){
			if (name==getName(ColourArrayType.values()[i])){
				return ColourArrayType.values()[i]; 
			}
		}
		return null;
	}
	
	public static ColourArray createStandardColourArray(int nPoints, ColourArrayType type) {
		if (type == null) {
			type = ColourArrayType.GREY;
		}
		ColourArray array;
		switch (type){
		case GREY:
			array = createMergedArray(nPoints, Color.WHITE, Color.BLACK);
			break;
		case REVERSEGREY:
			array = createMergedArray(nPoints, Color.BLACK, Color.WHITE);
			break;
		case BLUE:
			array = createMergedArray(nPoints, Color.BLACK, Color.BLUE);
			break;
		case GREEN:
			array = createMergedArray(nPoints, Color.BLACK, Color.GREEN);
			break;
		case RED:
			array = createMergedArray(nPoints, Color.BLACK, Color.RED);
			break;
		case HOT:
			array = createHotArray(nPoints);
			break;
		case FIRE:
			array = createFireArray(nPoints);
			break;
		case PATRIOTIC:
			array = createPatrioticArray(nPoints);
			break;
		case PARULA:
			array = createParulaArray(nPoints);
			break;
		case MATLAB:
			array = createMATLABArray(nPoints);
		case INFERNO:
			array =createInfronArray(nPoints); 
			break;
		default:
			array = createMergedArray(nPoints, Color.GREEN, Color.RED);
			break;
		}
		
		array.setColorArrayType(type); 
		
		return array; 
	}
	

	private void setColorArrayType(ColourArrayType type) {
		this.type=type; 
	}
	
	/**
	 * Get the colour array type for the current colours. 
	 * @return the colours array type. 
	 */
	public ColourArrayType getColorArrayType() {
		return this.type;
	}


	/**
	 * Get the CSS linear gradient for colouring Nodes by the selected colourmap/. 
	 * @param type
	 * @return CSS string for a linear gradient representing a colour map. 
	 */
	public static String getCSSColourArray(ColourArrayType type){
		if (type == null) {
			type = ColourArrayType.GREY;
		}
		switch (type){
		case GREY:
			return "  linear-gradient(black 0%, white 100%)";
		case REVERSEGREY:
			return "  linear-gradient(white 0%, black 100%)";
		case BLUE:
			return "  linear-gradient(blue 0%, black 100%)";
		case GREEN:
			return "  linear-gradient(green 0%, black 100%)";
		case RED:
			return "  linear-gradient(red 0%, black 100%)";
		case HOT:
			return "  linear-gradient(red 0%, orange 20%, green 40%, cyan 60%, blue 80%, black 100%)";
		case FIRE:
			return "  linear-gradient(black 0%, red 25%, orange 50%, yellow 75%, white 100%)";
		case PATRIOTIC:
			return "  linear-gradient(red 0%, white 50%, blue 100%)";
		case PARULA:
			return "  linear-gradient(#3e26a8 0%, #4367fd 20%, #1caadf 40%, #48cb86 %, #eaba30 80%, #f9fb15 100%)";
		case MATLAB:
			return "  linear-gradient(#0072bd 0%, #da5319 17%, #edb120 33%,  #7e2f8e 50%, #77ac30 67%,  #4dbeee 83%, #a2142f 100% )";
		default:
			return "  linear-gradient(black 0%, blue 20%, cyan 40%, green 60%, orange 80%, red 100%)";
		}
	}
	
	
	/**
	 * Get a list of colours which make up a colour array
	 * @return the linear gradient 
	 */
	public static Color[] getColorList(ColourArrayType type){
		Color[] colors; 
		if (type == null) {
			type = ColourArrayType.GREY;
		}
		switch (type){
		case GREY:
			colors=new Color[2];
			colors[0]=Color.WHITE;
			colors[1]=Color.BLACK;
			return colors;
		case REVERSEGREY:
			colors=new Color[2];
			colors[0]=Color.BLACK;
			colors[1]=Color.WHITE;
			return colors;
		case BLUE:
			colors=new Color[2];
			colors[0]=Color.BLACK;
			colors[1]=Color.BLUE;
			return colors;
		case GREEN:
			colors=new Color[2];
			colors[0]=Color.BLACK;
			colors[1]=Color.GREEN;
			return colors;
		case RED:
			colors=new Color[2];
			colors[0]=Color.BLACK;
			colors[1]=Color.RED;
			return colors;
		case HOT:
			colors=new Color[6];
			colors[0]=Color.BLACK;
			colors[1]= Color.BLUE; 
			colors[2]= Color.CYAN;
			colors[3]=Color.GREENYELLOW; 
			colors[4]= Color.ORANGE; 
			colors[5]= Color.RED;
			return colors;
		case FIRE:
			colors=new Color[5];
			colors[0]=Color.BLACK;
			colors[1]= Color.RED; 
			colors[2]= Color.ORANGE;
			colors[3]=Color.YELLOW; 
			colors[4]= Color.WHITE; 
			return colors; 
		case PATRIOTIC:
			colors=new Color[3];
			colors[0]=Color.RED;
			colors[1]= Color.WHITE; 
			colors[2]= Color.BLUE;
			return colors; 
		case MATLAB:
			colors=new Color[7];
			colors[0]= Color.color( 0,    0.4470,    0.7410);
			colors[1]= Color.color(0.8500 ,   0.3250   , 0.0980);
			colors[2]=  Color.color(0.9290 ,   0.6940 ,   0.1250);
			colors[3]= Color.color( 0.4940 ,   0.1840 ,   0.5560);
			colors[4]= Color.color(0.4660 ,   0.6740 ,   0.1880);
			colors[5]= Color.color( 0.3010 ,   0.7450 ,   0.9330);
			colors[6]= Color.color(   0.6350  ,  0.0780 ,   0.1840);
			return colors; 
		case PARULA:
			colors=new Color[6];
			colors[0]= Color.color(  0.24220,  0.150400,   0.66030);
			colors[1]= Color.color(  0.264700 ,0.403000  , 0.993500);
			colors[2]= Color.color(  0.108500,  0.66690 ,  0.873400);
			colors[3]= Color.color(  0.280900 , 0.796400,   0.526600);
			colors[4]= Color.color(  0.918400,  0.73080  , 0.18900);
			colors[5]= Color.color(  0.9769 , 0.98390   ,0.0805000);
				return colors; 
		case INFERNO:
			return InfernoColorMap.getInfernoColourMap();
		default :
			colors=new Color[2];
			colors[0]=Color.WHITE;
			colors[1]=Color.BLACK;
			return colors;
		}
	}
	
	/**
	 * Create a colour array which is the defualt in MATLAB r2014b+
	 * @param nPoints - the size of the colour map
	 * @return the colourmap.
	 */
	private static ColourArray createMATLABArray(int nPoints) {
		Color[] matlab=getColorList(ColourArrayType.MATLAB); 
		return createMultiColouredArray(nPoints, matlab[0], matlab[1], matlab[2], matlab[3], matlab[4], matlab[5],matlab[6]);
	}
	
	private static ColourArray createParulaArray(int nPoints) {
		// TODO Auto-generated method stub
		Color[] parula=getColorList(ColourArrayType.PARULA); 
		return createMultiColouredArray(nPoints, parula[0], parula[1], parula[2], parula[3], parula[4], parula[5]);
	}
	
	private static ColourArray createInfronArray(int nPoints) {
		Color[] inferno=getColorList(ColourArrayType.INFERNO); 
		ColourArray ca = new ColourArray();
		ca.colours = inferno; 
		return ca; 
	}
	
	public static ColourArray createWhiteToBlackArray(int nPoints) {
		return createMergedArray(nPoints, Color.WHITE, Color.BLACK);
	}
	
	public static ColourArray createBlackToWhiteArray(int nPoints) {
		return createMergedArray(nPoints, Color.BLACK, Color.WHITE);
	}
	
	public static ColourArray createHotArray(int nPoints) {
		// go from black to blue to cyan to green to orange to red
		// that's five stages in total. 		
		return createMultiColouredArray(nPoints, Color.BLACK, Color.BLUE, Color.CYAN,
				Color.GREENYELLOW, Color.ORANGE, Color.RED);
	}
	
	
	public static ColourArray createFireArray(int nPoints) {
		// go from black to red to orange to yellow to white
		// that's five stages in total. 		
		return createMultiColouredArray(nPoints, Color.BLACK, Color.RED, Color.ORANGE,
				Color.YELLOW, Color.WHITE);
	}
	
	public static ColourArray createPatrioticArray(int nPoints) {
		// go from black to red to white to blue
		// that's three stages in total.  		
		return createMultiColouredArray(nPoints, Color.RED, Color.WHITE, Color.BLUE);
	}

	
	/**
	 * Create a multicoloured array of colours that merges in turn between each of
	 * the colours given in the list. 
	 * @param nPoints total number of colour points
	 * @param colourList variable number of colours. 
	 * @return a new ColourArray object. 
	 */
	public static ColourArray createMultiColouredArray(int nPoints, Color ... colourList) {

		if (colourList.length == 1) {
			return createMergedArray(nPoints, colourList[0], colourList[0]);
		}
		else if (colourList.length == 2) {
			return createMergedArray(nPoints, colourList[0], colourList[1]);
		}
		
		ColourArray ca = new ColourArray();
		ca.colours = new Color[nPoints];
		
		int nSegments = (colourList.length - 1);
		int segPoints = nPoints / nSegments-1;
		int lastPoints = nPoints - segPoints * (nSegments-1);
		//int thisSegPoints;
		//int pointsToCreate;
		int iPoint = 0;
		ColourArray subArray;
		for (int i = 0; i < nSegments-1; i++) {
			subArray = createMergedArray(segPoints+1, colourList[i], colourList[i+1]);
			for (int j = 0; j < segPoints; j++) {
				ca.colours[iPoint++] = subArray.colours[j];
			}
		}
		// now the last one
		subArray = createMergedArray(lastPoints, colourList[nSegments-1], colourList[nSegments]);
		for (int j = 0; j < lastPoints; j++) {
			ca.colours[iPoint++] = subArray.colours[j];
		}
		
		return ca;
	}
	
//	static ColourArray createHotArray(int nPoints) {
//		
//	}
	
	public static ColourArray createMergedArray(int nPoints, Color c1, Color c2) {
		ColourArray ca = new ColourArray();
		ca.colours = new Color[nPoints];
		double col1[] = {c1.getRed(), c1.getGreen(), c1.getBlue()};
		double col2[] = {c2.getRed(), c2.getGreen(), c2.getBlue()};
		double step[] = new double[3];
		if (nPoints == 1) {
			ca.colours[0] = new Color(col1[0], col1[1], col1[2],1);
			return ca;
		}
		for (int i = 0; i < 3; i++) {
			step[i] = (col2[i]-col1[i]) / (nPoints-1);
		}
		for (int c = 0; c < nPoints; c++) {
			try {
		  ca.colours[c] = new Color(col1[0], col1[1], col1[2],1);
			}
			catch (IllegalArgumentException ex) {
				System.out.println(String.format("Illegal colour arguments red %3.3f green %3.3f blue %3.3f",
						col1[0], col1[1], col1[2]));
			}
		  for (int i = 0; i < 3; i++) {
			  col1[i] += step[i];
			  col1[i] = Math.max(0, Math.min(1, col1[i]));
		  }
		}		
		return ca;
	}
	
	public Color[] getColours() {
		return colours;
	}
	

	/**
	 * Get a colour from the colour array
	 * @param iCol - the colour index between 0 and 1 on the colour array. 
	 * @return the colour at at percentage iCol within the array
	 */
	public Color getColour(double iCol) {
		int iColInd=(int) Math.round(getNumbColours()*iCol);
		if (iColInd< 0 ) iColInd=0; 
		if (iColInd>=getNumbColours()) iColInd=getNumbColours()-1; 
		return colours[iColInd];
	}
	
	/**
	 * Get a colour from the colour array
	 * @param iCol - the colour index. i.e. 0 to N colours
	 * @return the colour at index iCol; 
	 */
	public Color getColour(int iCol) {
		return colours[iCol];
	}
	
	public int[] getIntColourArray(int iCol) {
		int[] rgb = new int[3];
		rgb[0] = (int) (colours[iCol].getRed()*255);
		rgb[1] = (int) (colours[iCol].getGreen()*255);
		rgb[2] = (int) (colours[iCol].getBlue()*255);
		return rgb;
	}
	
	public int getNumbColours() {
		if (colours == null) {
			return 0;
		}
		else {
			return colours.length;
		}
	}

	public void reverseArray() {
		Color[] newColours = new Color[colours.length];
		for (int i = 0; i < colours.length; i++) {
			newColours[i] = colours[colours.length-1-i];
		}
		colours = newColours;
	}

	@Override
	protected ColourArray clone() {
		try {
			ColourArray newArray = (ColourArray) super.clone();
			newArray.colours = this.colours.clone();
			return newArray;
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Return a contrasting colour which is different to ALL of the 
	 * colours in the array. This can be used to draw additional lines 
	 * over 3D plots made with the array colours. 
	 * @return the contrastingColor
	 */
	public Color getContrastingColour() {
		if (contrastingColor == null) {
			contrastingColor = createContrastingColour();
		}
		return contrastingColor;
	}

	/**
	 * Find a colour which is as distant as possible from all the other colours in 
	 * a colour array. 
	 * @return a contrasting colour. 
	 */
	private Color createContrastingColour() {
		Color[] tryCols = {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.BLACK};
		if (colours == null) {
			return tryCols[0];
		}
		int furthest = 0;
		int furthestIndex = 0;
		for (int i = 0; i < tryCols.length; i++) {
			int closeness = getCloseness(colours, tryCols[i]);
			if (closeness > furthest) {
				furthest = closeness;
				furthestIndex = i;
			}
		}
		return tryCols[furthestIndex];
		
	}

	/**
	 * Work out the closest distance between all the colours in 
	 * colourArray and colour
	 * @param colourArray
	 * @param aColour
	 * @return closest distance found
	 */
	private int getCloseness(Color[] colourArray, Color aColour) {
		double r = aColour.getRed();
		double b = aColour.getBlue();
		double g = aColour.getGreen();
		int closeness = Integer.MAX_VALUE;
		double cl;
		for (int i = 0; i < colourArray.length; i++) {
			cl = Math.abs(r-colourArray[i].getRed()) +  Math.abs(b-colourArray[i].getBlue()) +  Math.abs(g-colourArray[i].getGreen());
			closeness = (int) Math.min(cl, closeness);
		}
		return closeness;
	}

	/**	 
	 * Set a contrasting colour which is different to ALL of the 
	 * colours in the array. This can be used to draw additional lines 
	 * over 3D plots made with the array colours.
	 * @param contrastingColor the contrastingColor to set
	 */
	public void setContrastingColour(Color contrastingColor) {
		this.contrastingColor = contrastingColor;
	}
	
	/**
	 * Get the linear gradient to display on a node node for a colour array. 
	 * @param orientation- orientation of the colour gradinet, vertical or horizontal. 
	 * @param size- size in pixels of the gradient. 
	 * @param colourArrayType- colour array
	 * @return the linear graident for the a ColourArrayType. 
	 */
	public static LinearGradient getLinerGradient(Orientation orientation, double size, ColourArrayType colourArrayType){
		double sizeX = 0, sizeY=0;
		Color[] colorList=ColourArray.getColorList(colourArrayType);
		Stop[] stops =new Stop[colorList.length];
		for (int j=0; j<colorList.length; j++){
			stops[j]=new Stop((double) j/(colorList.length-1),colorList[j]);
		};
		if (orientation==Orientation.HORIZONTAL) sizeX=size; else sizeY=size; 
		LinearGradient gradient=new LinearGradient(0, 0, sizeX, sizeY, false, CycleMethod.NO_CYCLE, stops);
		return gradient;
	}
	

}
