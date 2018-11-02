package com.jamdev.maven.aipam.layout.utilsFX;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class UtilsFX {
	
	/**
	 * Runs the specified {@link Runnable} on the
	 * JavaFX application thread and waits for completion.
	 *
	 * @param action the {@link Runnable} to run
	 * @throws NullPointerException if {@code action} is {@code null}
	 */
	public static void runAndWait(Runnable action) {
	    if (action == null)
	        throw new NullPointerException("action");

	    // run synchronously on JavaFX thread
	    if (Platform.isFxApplicationThread()) {
	        action.run();
	        return;
	    }

	    // queue on JavaFX thread and wait for completion
	    final CountDownLatch doneLatch = new CountDownLatch(1);
	    Platform.runLater(() -> {
	        try {
	            action.run();
	        } finally {
	            doneLatch.countDown();
	        }
	    });

	    try {
	        doneLatch.await();
	    } catch (InterruptedException e) {
	        // ignore exception
	    }
	}
	
	/**
	 * Scale an image. 
	 * @param source - the source image
	 * @param targetWidth - the target image width.
	 * @param targetHeight - the target image height.
	 * @param preserveRatio - true to preserve ratio. 
	 * @return the scaled image. 
	 */
	public static Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(preserveRatio);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(null, null);
	}
	
	/**
	 * Whitens an image
	 * @param imageView - the image to whiten
	 * @return the whitened image. 
	 */
	public static ImageView whitenImage(ImageView imageView) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(1);
		imageView.setEffect(colorAdjust);
		return imageView;
	}
	
	/**
	 * Get the hex code form a color
	 * @param color - the color. 
	 * @return the color. 
	 */
	 public static String toRGBCode( Color color )
	    {
	        return String.format( "#%02X%02X%02X",
	            (int)( color.getRed() * 255 ),
	            (int)( color.getGreen() * 255 ),
	            (int)( color.getBlue() * 255 ) );
	    }

}
