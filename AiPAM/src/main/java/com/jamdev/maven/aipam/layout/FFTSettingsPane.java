package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;
import com.jamdev.maven.aipam.layout.utilsFX.ColourRangeSlider;
import com.jamdev.maven.aipam.layout.utilsFX.DefaultSliderLabel;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Pane with controls for changing the FFT settings
 * 
 * @author Jamie Macaulay 
 *
 */
public class FFTSettingsPane extends DynamicSettingsPane<AIPamParams> {
	
	/**
	 * Combo box for FFT Lengths
	 */
	private ComboBox<Integer> fftComboBox;
	
	/**
	 * Combo box for FFT Hop sizes. 
	 */
	private ComboBox<Integer> hopComboBox;
	
	/**
	 * The main pane. 
	 */
	private Pane mainPane;
	
	/**
	 * Combo box whiohc shows possible colour type.s 
	 */
	private ComboBox<String> colorType;
	
	/**
	 * The colour range sliders allowing contrast of spectrogram to be changed.
	 */
	private ColourRangeSlider colourRangleSlider;

	private AIPamView aiPamView; 

	/**
	 * The FFT settings pane. 
	 */
	public FFTSettingsPane(AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		this.mainPane= createPane(); 
	}

	/**
	 * Create the 
	 */
	private Pane createPane() {
		
		Label titleLabel = new Label("Spectrogram Settings");
		titleLabel.getStyleClass().add("label-title1");

		Label fftLabel = new Label("FFT Length");
		fftLabel.getStyleClass().add("label-title2");

		ObservableList<Integer> fftLenList  = FXCollections.observableArrayList();
		int len=32; 
		for (int i=0; i<10; i++) {
			fftLenList.add(len);
			len*=2; 
		}
		fftComboBox = new ComboBox<Integer>(fftLenList); 
		fftComboBox.getSelectionModel().select(5);
		fftComboBox.setOnAction((action)->{
			notifySettingsListeners();
		});

		fftComboBox.setTooltip(new Tooltip("The FFT length is the length of each chunk used to calculate a \n"
				+ " spectrogram. The chunk lengths are in sample bins. The way the FFT calculation works means \n"
				+ " that larger chunks allow for better frequency resolution, however because they require \n"
				+ "more samples time resolution is lost. Therefore a balance is required between larger chunks \n"
				+ "giving better frequency resolution and smaller chunks giving better time resolution. In \n"
				+ "passive acoustic monitoring chunk sizes are usually 512 ->2048 "));

		Label fftHopLabel = new Label("FFT Hop");
		fftHopLabel.getStyleClass().add("label-title2");
		


		
		hopComboBox  = new ComboBox<Integer>(FXCollections.observableArrayList(fftLenList)); 
		hopComboBox.getSelectionModel().select(4);
		hopComboBox.setTooltip(new Tooltip("The FFT hop is the amount the FFT Lengbth window slides along \n"
				+ "the samples before a new FFT chunk is calculated. Often the hop is half the FFT length \n"));  
		hopComboBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		Label colourScale = new Label("Colour Scales");
		colourScale.getStyleClass().add("label-title2");

		
		final ColourArrayType[] colourTypes = ColourArrayType.values(); 
		ObservableList<String> colorTypeList  = FXCollections.observableArrayList();
		for (int i=0; i<colourTypes.length; i++) {
			colorTypeList.add(ColourArray.getName(colourTypes[i])); 
		}
		colorType = new ComboBox<String>(colorTypeList); 
		colorType.setOnAction((action)->{
			//change the colour of the colour slider. 
			colourRangleSlider.setColourArrayType(colourTypes[colorType.getSelectionModel().getSelectedIndex()]);
			notifySettingsListeners();
		});
		colorType.getSelectionModel().select(3);

		colourRangleSlider = new ColourRangeSlider(); 	
		colourRangleSlider.setMin(0);
		colourRangleSlider.setMax(150);

		//add moving labels
		colourRangleSlider.setHighLabelFormat(new DefaultSliderLabel());
		colourRangleSlider.setLowLabelFormat(new DefaultSliderLabel());
		colourRangleSlider.lowValueProperty().addListener((obsval, oldval, newval)->{
			notifySettingsListeners();
		});
		colourRangleSlider.highValueProperty().addListener((obsval, oldval, newval)->{
			notifySettingsListeners();
		});
		
		VBox vBox = new VBox(); 
		vBox.setSpacing(5);
		vBox.getChildren().addAll(titleLabel, fftLabel, fftComboBox, fftHopLabel,
				hopComboBox, colourScale,colorType,  colourRangleSlider); 
		
		return vBox;

	}
	
	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {	
		paramsIn.fftLength	=fftComboBox.getValue();
		paramsIn.fftHop		=hopComboBox.getValue();
		paramsIn.colourLims = new double[] {colourRangleSlider.getLowValue(), 
				colourRangleSlider.getHighValue()}; 
		//System.out.println("ColourLimits: " + paramsIn.colourLims[0] + "   " + paramsIn.colourLims[1]); 
		paramsIn.spectrogramColour = ColourArray.getColorArrayType(colorType.getValue()); 
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		fftComboBox.getSelectionModel().select(params.fftLength);
		hopComboBox.getSelectionModel().select(params.fftHop);
		colourRangleSlider.setLowValue(params.colourLims[0]);
		colourRangleSlider.setHighValue(params.colourLims[1]);
		
//		System.out.println("Set colour lims: " + params.colourLims[0] + "   " + 
//		params.colourLims[1] + "  " + colourRangleSlider.getMax()); 		
		
		colorType.getSelectionModel().select(ColourArray.getName(params.spectrogramColour));
		colourRangleSlider.setColourArrayType(ColourArray.getColorArrayType(colorType.getValue())); 
	}
	

	@Override
	public Node getIcon() {
		ImageView icon = new ImageView(aiPamView.getSpectrogramIcon()); 
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(1);
		icon.setEffect(colorAdjust);
		return icon;
	}

	@Override
	public String getTitle() {
		return "Spectrogram";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

}
