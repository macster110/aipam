package com.jamdev.maven.aipam.layout.detectionDisplay;

import java.io.File;

import org.jamdev.jdl4pam.utils.DLUtils;
import org.jamdev.jpamutils.wavFiles.AudioData;
import org.jamdev.jpamutils.wavFiles.WavFile;

import com.jamdev.maven.aipam.clips.AudioInfo;
import com.jamdev.maven.aipam.clips.ClipWave;
import com.jamdev.maven.aipam.clips.StandardAudioImporter;
import com.jamdev.maven.aipam.layout.detectionDisplay.waveform.WaveformPlot;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
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
		
		DetectionPlot detectionPlot = new WaveformPlot();
		detectionPlot.setDataProvider(dataProvider);

		Group root = new Group(new StackPane(detectionPlot));
		Scene scene = new Scene(root, 400, 300);
		
		detectionPlot.prefWidthProperty().bind(scene.widthProperty());
		detectionPlot.prefHeightProperty().bind(scene.heightProperty());
		
		detectionPlot.maxWidthProperty().bind(scene.widthProperty());
		detectionPlot.maxHeightProperty().bind(scene.heightProperty());
		
		detectionPlot.updateAxis(new double[] {0, 1000.}, new double[] {-1, 1}, null);

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
