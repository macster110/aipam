package com.jamdev.maven.clips;

import javafx.beans.property.DoubleProperty;

/**
 * Handles the playing of audio data of a clip. 
 * @author Jamie Macaulay 
 *
 */
public interface AudioPlay  {

	/**
	 * Play the clip audio. 
	 * @return the audio clip to play
	 */
	public void playClipAudio(); 
	
	/**
	 * Stop audio of the clip playing. 
	 */
	public void stopClipAudio(); 
	
	/**
	 * Get the volume property. Allows volume to be bound to the volume controls in a
	 * GUI. 
	 * @return the volume property. 
	 */
	public DoubleProperty getVolumePropery();

}
