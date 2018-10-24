package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;
import com.jamdev.maven.aipam.layout.utilsFX.ColourRangeSlider;
import com.jamdev.maven.aipam.layout.utilsFX.DefaultSliderLabel;
import com.jamdev.maven.aipam.utils.SettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FFTSettingsPane implements SettingsPane<AIPamParams> {
	
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

	/**
	 * The FFT settings pane. 
	 */
	public FFTSettingsPane() {
	}

	/**
	 * Create the 
	 */
	private Pane createPane() {
		
		Label titleLabel = new Label("Spectrogram Settings");
		titleLabel.setFont(AIPamView.defaultLabelTitle1);
		titleLabel.setTextFill(AIPamView.defaultTitleColour);

		
		Label fftLabel = new Label("FFT Length");
		fftLabel.setFont(AIPamView.defaultLabelTitle2);
		fftLabel.setTextFill(AIPamView.defaultTitleColour);

		ObservableList<Integer> fftLenList  = FXCollections.observableArrayList();
		int len=32; 
		for (int i=0; i<10; i++) {
			fftLenList.add(len);
			len*=2; 
		}
		fftComboBox = new ComboBox<Integer>(fftLenList); 
		fftComboBox.getSelectionModel().select(5);
		fftComboBox.setTooltip(new Tooltip("The FFT length is the length of each chunk used to calculate a \n"
				+ " spectrogram. The chunk lengths are in sample bins. The way the FFT calculation works means \n"
				+ " that larger chunks allow for better frequency resolution, however because they require \n"
				+ "more samples time resolution is lost. Therefore a balance is required between larger chunks \n"
				+ "giving better frequency resolution and smaller chunks giving better time resolution. In \n"
				+ "passive acoustic monitoring chunk sizes are usually 512 ->2048 "));

		Label fftHopLabel = new Label("FFT Hop");
		fftHopLabel.setFont(AIPamView.defaultLabelTitle2);
		fftHopLabel.setTextFill(AIPamView.defaultTitleColour);
		
		hopComboBox  = new ComboBox<Integer>(FXCollections.observableArrayList(fftLenList)); 
		hopComboBox.getSelectionModel().select(4);
		hopComboBox.setTooltip(new Tooltip("The FFT hop is the amount the FFT Lengbth window slides along \n"
				+ "the samples before a new FFT chunk is calculated. Often the hop is half the FFT length \n"));  
		
		Label colourScale = new Label("Colour Scales");
		colourScale.setFont(AIPamView.defaultLabelTitle2);
		colourScale.setTextFill(AIPamView.defaultTitleColour);
		
		final ColourArrayType[] colourTypes = ColourArrayType.values(); 
		ObservableList<String> colorTypeList  = FXCollections.observableArrayList();
		for (int i=0; i<colourTypes.length; i++) {
			colorTypeList.add(ColourArray.getName(colourTypes[i])); 
		}
		colorType = new ComboBox<String>(colorTypeList); 
		colorType.setOnAction((action)->{
			//change the colour of the colour slider. 
			colourRangleSlider.setColourArrayType(colourTypes[colorType.getSelectionModel().getSelectedIndex()]);
		});
		colorType.getSelectionModel().select(3);

		colourRangleSlider = new ColourRangeSlider(); 	
		//add moving labels
		colourRangleSlider.setHighLabelFormat(new DefaultSliderLabel());
		colourRangleSlider.setLowLabelFormat(new DefaultSliderLabel());
		
		

		VBox vBox = new VBox(); 
		vBox.setSpacing(5);
		vBox.getChildren().addAll(titleLabel, fftLabel, fftComboBox, fftHopLabel,
				hopComboBox, colourScale,colorType,  colourRangleSlider); 
		
		return vBox;

	}
	
	
	
	@Override
	public Pane getPane() {
		if (mainPane==null) {
			this.mainPane= createPane(); 
		}
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

}
