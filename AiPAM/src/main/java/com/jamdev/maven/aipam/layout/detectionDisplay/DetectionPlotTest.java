package com.jamdev.maven.aipam.layout.detectionDisplay;

import java.io.File;

import org.jamdev.jdl4pam.utils.DLUtils;
import org.jamdev.jpamutils.wavFiles.AudioData;
import org.jamdev.jpamutils.wavFiles.WavFile;

import com.jamdev.maven.aipam.clips.AudioInfo;
import com.jamdev.maven.aipam.clips.ClipWave;
import com.jamdev.maven.aipam.clips.StandardAudioImporter;
import com.jamdev.maven.aipam.layout.detectionDisplay.scroller.DetectionPlotScrollBar;
import com.jamdev.maven.aipam.layout.detectionDisplay.scroller.ScrollBarPane;
import com.jamdev.maven.aipam.layout.detectionDisplay.spectrogram.SpectrogramPlot;
import com.jamdev.maven.aipam.layout.detectionDisplay.waveform.WaveformPlot;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * Test a detection plot.
 *  
 * @author Jamie Macaulay
 *
 */
public class DetectionPlotTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		String filename = "/Users/au671271/Google Drive/SoundSort_dev/Right_whale_test/Clips_200Hz/20180426_001457_015.wav";
		
		StandardAudioImporter audioImporter = new StandardAudioImporter(); 
		
		ClipWave wavFile = audioImporter.initWaveWithInputStream(new File(filename), 0, 10000, 8000, true); 

		StandardDataProvider dataProvider = new StandardDataProvider(new short[][]{wavFile.getSampleAmplitudes()}, wavFile.getSampleRate(), 0); 
		
		DetectionPlot waveformPlot = new WaveformPlot();
		waveformPlot.setDataProvider(dataProvider);
		
		DetectionPlot spectrogramPlot = new SpectrogramPlot();
		spectrogramPlot.setDataProvider(dataProvider);
		
		DetectionPlotScrollBar scrollBarPane = new DetectionPlotScrollBar(); 
		scrollBarPane.setMinVal(dataProvider.getDataLimits().getMinLimit());
		scrollBarPane.setMaxVal(dataProvider.getDataLimits().getMaxLimit());
		scrollBarPane.setPrefHeight(70.0);
		
		VBox mainPane = new VBox(); 
		mainPane.setSpacing(5.0);
		
		mainPane.getChildren().add(scrollBarPane);
		mainPane.getChildren().add(waveformPlot);
		mainPane.getChildren().add(spectrogramPlot);

		Group root = new Group(new StackPane(mainPane));
		Scene scene = new Scene(root, 1000, 1000);
		
		mainPane.prefWidthProperty().bind(scene.widthProperty());
		mainPane.prefHeightProperty().bind(scene.heightProperty());
		
		waveformPlot.prefWidthProperty().bind(scene.widthProperty());
		spectrogramPlot.prefWidthProperty().bind(scene.widthProperty());
		
		//the scroll bar pane.
		scrollBarPane.addDetectionPlot(waveformPlot);
		scrollBarPane.addDetectionPlot(spectrogramPlot);

		//waveformPlot.prefHeightProperty().bind(scene.heightProperty());
		//waveformPlot.maxHeightProperty().bind(scene.heightProperty());
		
		waveformPlot.updateAxis(new double[] {0, 1000.}, new double[] {-1, 1}, null);

		//apply JMetro theme
		new JMetro(Style.LIGHT).setScene(root.getScene());
		//add extra style sheet for fluent design menu buttons and tab pane.
		//root.getStylesheets().add(getClass().getResource("/resources/fluentdesignextra.css").toExternalForm());
		//root.setStyle("-fx-background: BACKGROUND;");
		
		primaryStage.setTitle("Detection plot test");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		dataProvider.requestData(0, (long) wavFile.getLengthInSeconds()*1000);  


	}

	public static void main(String[] args) {
		
		
		
		launch(args); 
	}

}
