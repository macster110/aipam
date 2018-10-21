package com.jamdev.maven.clips;

import org.datavec.audio.Wave;
import org.datavec.audio.extension.Spectrogram;

//import org.datavec.audio.Wave;
//import org.datavec.audio.dsp.FastFourierTransform;
//import org.datavec.audio.dsp.WindowFunction;

/**
 * Creates a spectrogram of a clip. 
 * 
 * Some code taken from
 * https://knowm.org/exploring-bird-song-with-a-spectrogram-in-java/
 *  
 * @author Jamie Macaulay
 *
 */
public class ClipSpectrogram {

    
	private Wave wave;
	
	private Spectrogram spectrogram; 

    
    /**
     * Constructor
     *
     * @param wave
     */
    public ClipSpectrogram(Wave wave, int fftLength, int fftHop) {
        this.wave = wave;
        //generate the spectrogram for the clip. This is used as a clusterring fingerprint. 
        spectrogram = new Spectrogram(wave, fftLength, fftHop); 
        spectrogram.getNormalizedSpectrogramData(); 
    }


}