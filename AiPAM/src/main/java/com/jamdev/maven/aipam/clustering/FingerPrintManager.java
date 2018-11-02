package com.jamdev.maven.aipam.clustering;

import org.nd4j.linalg.util.ArrayUtil;

import com.jamdev.maven.aipam.utils.DownSampleImpl;

/**
 * 
 * Generates a fingerprint for input into clustering algorithms. 
 * <p>
 * Fingerprint are representation of the audio data which can be 
 * used by machine learning algorithms. They could be simple spectrogram flattened
 * to be a 1D array or a more advanced peak finding method.   
 * 
 * 
 * @author Jamie Macaulay
 *
 */
public class FingerPrintManager {

	/**
	 * Creates a 1D representation a spectrogram and compresses the spectrogram
	 * so that clustering is faster. Also log scales to give quiter sounds more influence. 
	 * @param spectrogram - input spectrogram as [time][intensity]
	 * @param compression - the compression factor to use. if 50 then spectrogram images is 50 x 50. -1 means no compression
	 * @return audio fingerprint of a spectrogram. 
	 */
	public static double[] simpleSpectrogramFingerPrint(double[][] spectrogram, int compression) {

		double[][] data; 
		if (compression ==-1) {
			data=spectrogram;
		}
		else {
			data = DownSampleImpl.largestTriangleThreeBuckets(spectrogram, compression);
		}
		//		for (int i=0; i<data.length; i++) {
		//			for (int j=0; j<data[0].length; j++) {
		//				data[i][j]=20*Math.log10(data[i][j]); 
		//			}
		//		}
		return ArrayUtil.flattenDoubleArray(data);
	}

}
