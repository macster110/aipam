package com.jamdev.maven.aipam.layout;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.AudioInfo;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;
import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.clips.FullClipPane;
import com.jamdev.maven.aipam.layout.clips.SpectrogramImage;
import com.jamdev.maven.aipam.layout.utilsFX.ColourRangeSlider;
import com.jamdev.maven.aipam.layout.utilsFX.DefaultSliderLabel;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

	private ImageView specImage;

	/**
	 * The current preview clip. Null if no clip selected. Aloows users to play with control settings. 
	 */
	private PAMClip currentPreviewClip;

	private BorderPane clipPreviewHolder;

	/**
	 * Pane which shows some user prompts. 
	 */
	private BorderPane userPromptPane; 

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
		int len=4; 
		for (int i=0; i<10; i++) {
			fftLenList.add(len);
			len*=2; 
		}
		fftComboBox = new ComboBox<Integer>(fftLenList); 
		fftComboBox.getSelectionModel().select(8); // 512 samples default
		fftComboBox.setOnAction((action)->{
			notifySettingsListeners();
		});

		fftComboBox.setTooltip(new Tooltip(
							"The FFT length is the length of each chunk used to calculate a \n"
						+ " spectrogram. The chunk lengths are in sample bins. The way the FFT calculation works means \n"
						+ " that larger chunks allow for better frequency resolution, however because they require \n"
						+ "more samples time resolution is lost. Therefore a balance is required between larger chunks \n"
						+ "giving better frequency resolution and smaller chunks giving better time resolution. In \n"
						+ "passive acoustic monitoring chunk sizes are usually 512 ->2048 "));

		Label fftHopLabel = new Label("FFT Hop");
		fftHopLabel.getStyleClass().add("label-title2");
		
		hopComboBox  = new ComboBox<Integer>(FXCollections.observableArrayList(fftLenList)); 
		hopComboBox.getSelectionModel().select(7); //256 samples hop default
		hopComboBox.setTooltip(new Tooltip(
				"The FFT hop is the amount the FFT Lengbth window slides along \n"
				+ "the samples before a new FFT chunk is calculated. Often the hop is half the FFT length \n"));  
		hopComboBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		Label colourScale = new Label("Colour Scales");
		colourScale.getStyleClass().add("label-title2");
		
		
		Button defaultHop = new Button("Default"); 
		defaultHop.setTooltip(new Tooltip("The default FFT hop is 50% of the the FFT length")); 
		defaultHop.setOnAction((action)->{
			int index =fftComboBox.getSelectionModel().getSelectedIndex()-1;
			if (index<0 || index> hopComboBox.getItems().size()-1) return;
			hopComboBox.getSelectionModel().select(index);
		});
		
		HBox hopBox = new HBox(); 
		hopBox.setSpacing(5);
		hopBox.getChildren().addAll(hopComboBox, defaultHop);
		
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
			updateSpecImage(this.currentPreviewClip);  //update the preview clips
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
			updateSpecImage(this.currentPreviewClip);  //update the preview clips
		});
		colourRangleSlider.highValueProperty().addListener((obsval, oldval, newval)->{
			notifySettingsListeners();
			updateSpecImage(this.currentPreviewClip);  //update the preview clips
		});
		
		VBox vBox = new VBox(); 
		vBox.setSpacing(5);
		vBox.getChildren().addAll(titleLabel, fftLabel, fftComboBox, fftHopLabel,
				hopBox, colourScale,colorType,  colourRangleSlider, createClipPane(),  createUserPromptPane()); 
		
		return vBox;

	}
	
	private Pane createUserPromptPane() {
		this.userPromptPane = new BorderPane(); 
		return userPromptPane; 
	}
	
	/**
	 * Create pane to allow user to recalculate 
	 * @return
	 */
	private Pane createRecalcPane() {
		HBox recalcPane = new HBox(); 
		recalcPane.setAlignment(Pos.CENTER_LEFT);
		recalcPane.setSpacing(5); 

		Button recalc = new Button("Recalculate"); 
		recalc.setOnAction((action)->{
			aiPamView.reCalcImages();
		}); 
		
		recalcPane.getChildren().addAll(new Label("Spectrogram images need recalculated "), recalc); 
		
		MasterControlPane.highLightButton(recalc); 
		
		return recalcPane; 
	}
	
	/**
	 * Called whenever there is a new user prompt. Convenience for the user to recalc spectrograms. 
	 * @param list - the userprompt list.  
	 */
	private void newUserPrompt(ArrayList<UserPrompt> list) {
		this.userPromptPane.setCenter(null); 
		for (UserPrompt prompt : list) {
			if (prompt == UserPrompt.RECREATE_IMAGES) {
				this.userPromptPane.setCenter(createRecalcPane() );
			}
		}
	}
	
	/**
	 * Shows a preview of the clip
	 */
	private Pane createClipPane() {
		
		specImage = new ImageView(); 
		
		clipPreviewHolder = new BorderPane(); 
	
		clipPreviewHolder.setCenter(specImage);
		
		Label colourScale = new Label("Clip Preview");
		colourScale.getStyleClass().add("label-title2");
		
		clipPreviewHolder.setTop(colourScale);
		clipPreviewHolder.setCenter(new Label("Select an imported clip to see a preview\nThis can be used to test colour scales"));


		return clipPreviewHolder; 
	}
	
	/**
	 * Update the image in the spectrogram display
	 * @param pamClip - the clips. 
	 */
	private void updateSpecImage(PAMClip pamClip) {
		if (pamClip==null) return; 
		SpectrogramImage image = new SpectrogramImage(pamClip.getSpectrogram().getAbsoluteSpectrogram(), 
				this.aiPamView.getCurrentColourArray(), new double[] {colourRangleSlider.getLowValue(), 
						colourRangleSlider.getHighValue()}); 
		
		specImage.setImage(image.getSpecImage(200, 200));	
		
		clipPreviewHolder.setLeft(FullClipPane.createFreqAxis(Side.LEFT,  pamClip)); 
		clipPreviewHolder.setCenter(specImage); 
	}
		
	
	/**
	 * Called when a new audio info is set. This will set the default FFT length and FFT hop
	 * @param audioInfo - the audio info object. 
	 * @param params - the current parameters. 
	 */
	private void setAudioInfo(AudioInfo audioInfo, AIPamParams params) {
	
		double samples = audioInfo.medianFilelength*audioInfo.sampleRate;
		
		//System.out.println("Set new audio info in spectrogram: " + samples);
		
		for (int i=0; i<fftComboBox.getItems().size(); i++) {
			//System.out.println("Set new audio info in spectrogram: " + samples/(double) fftComboBox.getItems().get(i));
			if (samples/(double) fftComboBox.getItems().get(i)<25) {
				params.fftLength = fftComboBox.getItems().get(i); 
				break;
			}; 
		}
	
		//System.out.println("Set new audio info in spectrogram: " + params.fftLength );
		
		params.fftHop = params.fftLength/2; 
		
		setParams(params);
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
		//System.out.println("Set Params: " + params.fftLength);
		
		fftComboBox.getSelectionModel().select(Integer.valueOf(params.fftLength));
		hopComboBox.getSelectionModel().select(Integer.valueOf(params.fftHop));
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
		switch (flag) {
		case AiPamController.NEW_CLIP_SELECTED:
			this.currentPreviewClip = (PAMClip) stuff; 
			updateSpecImage(currentPreviewClip);
			break;
		case AiPamController.USER_PROMPT:
			ArrayList<UserPrompt> list = (ArrayList<UserPrompt> ) stuff; 
			newUserPrompt(list);
			break; 
		case AiPamController.END_FILE_HEADER_LOAD:
			setAudioInfo(this.aiPamView.getAIControl().getAudioInfo(),
					aiPamView.getAIControl().getParams());
			break; 

		}
	}
	
	@Override
	public String getDescription() {
		return "Spectrogram settings including FFT length, \n"
				+ "FFT hop and colour scales";
	}




}
