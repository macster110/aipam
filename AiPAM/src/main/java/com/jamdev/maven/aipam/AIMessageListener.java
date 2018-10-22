package com.jamdev.maven.aipam;

/**
 * Listens for messages from the controller. 
 * @author Jamie Macaulay 
 *
 */
public interface AIMessageListener {
	
	/**
	 * Called whenever there is a new update from the controller.
	 * 
	 * @param messageFlag - the message flag.
	 * @param messageObject - the message obeject.
	 */
	public void newMessage(int messageFlag, Object messageObject); 

}
