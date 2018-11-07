package com.jamdev.maven.aipam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.annotation.Annotation;
import com.jamdev.maven.aipam.clustering.ClusterParams;
import com.jamdev.maven.aipam.layout.ColourArray;
import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Handles importing and exporting settings files 
 * <p>
 * Settings files are saved as .mat files to make it easy for 
 * users to create setting sin MATLAB or Octave.  This also allows makesit
 * more straightforward to call the Java code from MATLAB, allowing for bespoke
 * analysis tasks whilst keeping the speed of Java. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class SettingsImportExport {
	
	/**
	 * The AIPamController 
	 */
	private AiPamController aiPamControl;


	public SettingsImportExport(AiPamController aiPamControl) {
		this.aiPamControl = aiPamControl; 
	}
	
	
	/**
	 * Save the MT file. 
	 * @param file - the file
	 * @param mlArray - the mlArray to save. 
	 */
	@SuppressWarnings("unused")
	public void saveMTFile(File file, ArrayList<MLArray> mlArray) {
		try {
			//this autiomatically writes the file 
			MatFileWriter filewrite=new MatFileWriter(file.getAbsolutePath(), mlArray);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("File save failed");
			alert.setHeaderText("Failure saving the file");
			alert.setContentText("There was a proible trying to save the last prob_det results as a .mat file");

			alert.showAndWait();
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Convert settings to a MATLAB struct for export
	 * @param aiPamParam - the params to convert to a struct
	 * @return a MATLAB structure containing program settings, annotations etc. 
	 */
	public MLStructure settingsToStruct(AIPamParams aiPamParam) {
		
		MLStructure mlStruct = new MLStructure("settings", new int[] {1,1});

		//the audio settings
		MLDouble maxClipLength= mlDouble(aiPamParam.maximumClipLength);
		MLInt32 channel=mlInt(aiPamParam.channel); 
		MLInt32 decimatorSR=mlInt(aiPamParam.decimatorSR); 
		

		//the FFT settings
		MLInt32 fftLength=mlInt(aiPamParam.fftLength);
		MLInt32 fftHop=mlInt(aiPamParam.fftHop);
		MLChar colourArrayType=new MLChar(null, ColourArray.getName(aiPamParam.spectrogramColour)); 
		MLDouble colourLimits=new MLDouble(null, aiPamParam.colourLims , 2); 
		
		//playback
		MLDouble volume=mlDouble(aiPamParam.volume);

		MLChar outputFolder=new MLChar(null, aiPamParam.outPutAnnotationFolder); 
		
		//the annotation data
		MLStructure annotationStruct = aiPamControl.getAnnotationManager().annotation2Struct(); 
		
		// the cluster params
		MLStructure clusterParams = aiPamParam.clusterParams.clusterParams2Struct();

		mlStruct.setField("maxcliplength", maxClipLength,0);
		mlStruct.setField("channel", channel,0);
		mlStruct.setField("decimatorfs",decimatorSR ,0);
		mlStruct.setField("fftlength",fftLength ,0);
		mlStruct.setField("ffthop",fftHop ,0);
		mlStruct.setField("colourarraytype", colourArrayType,0);
		mlStruct.setField("colourlimits", colourLimits,0);
		mlStruct.setField("playbackvolume", volume ,0);
		mlStruct.setField("outputfolder",outputFolder ,0);
		mlStruct.setField("annotations", annotationStruct ,0);
		mlStruct.setField("clustersettings", clusterParams,0);

		
		return mlStruct; 	
	}
	
	/**
	 * Import a settings file from a file.
	 * @param file - the file to load. 
	 */
	public AIPamParams loadSettingsFile(File file) {
		MatFileReader mfr = null; 
		try {
			if (file ==null) {
				System.err.println("The imported file is null");
				return null;
			}
			
			mfr = new MatFileReader(file);
			
			//get array of a name "my_array" from file
			MLStructure mlArrayRetrived = (MLStructure) mfr.getMLArray( "settings" );
			AIPamParams aiPamParams = structToSettings(mlArrayRetrived);
			return aiPamParams; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}

	
	/**
	 * Convert a MATLAB structure to a AIPamParams class. 
	 * @param settings - the MATLAB structure with simulation settings
	 * @return the probability settings class. 
	 */
	public AIPamParams structToSettings(MLStructure mlArrayRetrived) {

//		MatFileReader mfr = null; 
		try {
//			mfr = new MatFileReader( "C:\\Users\\macst\\Desktop\\testImportSettings.mat" );
//
//			//get array of a name "my_array" from file
//			MLStructure mlArrayRetrived = (MLStructure) mfr.getMLArray( "settings" );

//			MLChar speciesML = (MLChar) mlArrayRetrived.getField("species", 0); 
//			String species = speciesML.getString(0); 
			
			//the audio settings
			MLDouble maxClipLength=(MLDouble) mlArrayRetrived.getField("maxcliplength", 0);
			MLInt32 channel=(MLInt32) mlArrayRetrived.getField("channel", 0);
			MLInt32 decimatorSR=(MLInt32) mlArrayRetrived.getField("decimatorfs", 0);

			//the FFT settings
			MLInt32 fftLength=(MLInt32) mlArrayRetrived.getField("fftlength", 0);
			MLInt32 fftHop=(MLInt32) mlArrayRetrived.getField("ffthop", 0);
			MLChar colourArrayType=(MLChar) mlArrayRetrived.getField("colourarraytype", 0);
			MLDouble colourLimits=(MLDouble) mlArrayRetrived.getField("colourlimits", 0);

			
			//playbackvolume
			MLDouble volume=(MLDouble) mlArrayRetrived.getField("playbackvolume", 0);
			
			//output annotation - this is a list of annotation strcutures. 
			MLChar outputFolder = (MLChar) mlArrayRetrived.getField("outputfolder");
			MLStructure annotations = (MLStructure) mlArrayRetrived.getField("annotations");

			//cluster parameters
			MLStructure clusterParams = (MLStructure) mlArrayRetrived.getField("clustersettings");

			//now set all the stuff in the new params class.
			AIPamParams aiPamParams = new AIPamParams(); 
			//audio settings
			aiPamParams.maximumClipLength=maxClipLength.get(0).doubleValue();
			aiPamParams.channel=channel.get(0).intValue();
			aiPamParams.decimatorSR=decimatorSR.get(0).intValue();
			
			//spectrogram settings. 
			aiPamParams.fftHop=fftHop.get(0).intValue();
			aiPamParams.fftLength=fftLength.get(0).intValue();
			aiPamParams.colourLims = new double[]{colourLimits.get(0), colourLimits.get(1)}; 
			aiPamParams.spectrogramColour= ColourArray.getColorArrayType(colourArrayType.getString(0)) ;
			
			//play back
			aiPamParams.volume = volume.get(0);

			//annotations
			aiPamParams.outPutAnnotationFolder = outputFolder.getString(0);
			
			//import all the annotations
			aiPamParams.annotations = aiPamControl.getAnnotationManager().struct2Annotations(annotations); 
			
			//cluster parameters
			aiPamParams.clusterParams = aiPamControl.getClusterManager().struct2ClusterParams(clusterParams); 
			
			/***Print this stuff out***/
			System.out.println("maxcliplength: " + maxClipLength.get(0)); 
			System.out.println("channel: " + channel.get(0)); 
			System.out.println("decimatorfs: " + decimatorSR.get(0)); 
			System.out.println("--------------------------"); 
			System.out.println("fftlength: " + fftLength.get(0)); 
			System.out.println("fftHop: " + fftHop.get(0)); 
			System.out.println("colourArrayType: " + colourArrayType.getString(0)); 
			System.out.println("colorlims: " + colourLimits.get(0) + "  " + colourLimits.get(1)); 
			System.out.println("--------------------------"); 
			System.out.println("volume: " + volume.get(0)); 
			System.out.println("--------------------------"); 
			System.out.println("outPutAnnotationFolder: " + outputFolder.getString(0)); 
			printAnnotationData(aiPamParams.annotations); 
			System.out.println("--------------------------"); 
			printClusterParams(aiPamParams.clusterParams); 
		
			return aiPamParams;

		} catch (Exception e) {
			e.printStackTrace();
		}


		return null; 
	}
	
	
	/**
	 * Single double value in MATLAB
	 * @param value - the value  
	 * @return MLDouble value
	 */
	public static MLDouble mlDouble(double value) {
		return new MLDouble(null, new double[]{value}, 1);
	}
	
	/**
	 * Single int value in MATLAB
	 * @param value - the int value  
	 * @return MLInt value
	 */
	public static MLInt32 mlInt(int value) {
		return new MLInt32(null, new int[]{value}, 1);
	}



	private void printClusterParams(ClusterParams clusterParams) {
		// TODO Auto-generated method stub
		
	}



	private void printAnnotationData(List<Annotation> annotations) {
		// TODO Auto-generated method stub
		
	}



}
