package com.jamdev.maven.clips;

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
}
