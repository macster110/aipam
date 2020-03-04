package com.jamdev.maven.aipam.clips;

import java.io.File;
import javafx.application.Application;  
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.stage.Stage;  

/**
 * Test the javafx media player with a wav file. 
 * @author macst
 *
 */
public class MediaPlayApplicationTest extends Application  


{  
	/**
	 * this flag indicates whether the playback completes or not.
	 */
	boolean playCompleted;
  
    @Override  
    public void start (Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
        //Initialising path of the media file, replace this with your file path   
        String audioFilePath = "C:\\Users\\macst\\Desktop\\retrieval_2\\20180816\\Ish20180816_100541_627.wav" ;
        
        //path = "E:\\Google Drive\\Example_PAM\\Sperm_Whale_IFAW\\MF20070419_101650.wav";
          
        //Instantiating Media class  
        Media media = new Media(new File(audioFilePath).toURI().toString());  
          
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  
          
        //by setting this property to true, the audio will be played   
        mediaPlayer.setAutoPlay(true);  
        primaryStage.setTitle("Playing Audio");  
        primaryStage.show();  
        
        

         
    }  
    public static void main(String[] args) {  
        launch(args);  
    }  
      
}  