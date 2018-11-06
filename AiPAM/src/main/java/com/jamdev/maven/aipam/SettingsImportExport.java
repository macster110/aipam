package com.jamdev.maven.aipam;

import java.util.List;

import com.jamdev.maven.aipam.annotation.Annotation;
import com.jamdev.maven.aipam.clustering.ClusterParams;
import com.jamdev.maven.aipam.layout.ColourArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

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
	
	
	
	public MLStructure settingsToStruct(AIPamParams aiPamParam) {
		
		return null; 	
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
			
			//playback
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



	private void printClusterParams(ClusterParams clusterParams) {
		// TODO Auto-generated method stub
		
	}



	private void printAnnotationData(List<Annotation> annotations) {
		// TODO Auto-generated method stub
		
	}



}
