package com.jamdev.maven.aipam.layout.utilsFX;

import java.util.concurrent.CountDownLatch;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;



import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

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
		colorAdjust.setBrightness(0.8);
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

	/**
	 * Animates a node with a flash
	 * @param node - the node for the effect
	 * @param col - the colour of the flash
	 * @param radius - the raioud of the flash
	 * @param duration - the duration of the flash in seconds
	 */
	public static void nodeFlashEffect(Node node, Color col, double radius, double duration ){
		//			ColorInput effect = new ColorInput(0, 0, textBox.getWidth(), textBox.getHeight(), Paint.valueOf("#FFDDDD"));
		//			Timeline flash = new Timeline(
		//					  new KeyFrame(Duration.seconds(0.4), new KeyValue(effect.paintProperty(), Color.RED)),
		//					  new KeyFrame(Duration.seconds(0.8), new KeyValue(effect.paintProperty(), Paint.valueOf("#E0DDDD"))),
		//					  new KeyFrame(Duration.seconds(1.0), new KeyValue(effect.paintProperty(), Paint.valueOf("#DDDDDD"))));

		DropShadow shadow = new DropShadow();
		shadow.setColor(col);
		shadow.setSpread(0.5);

		Timeline shadowAnimation = getFlashTimeLine( shadow,  radius, duration);

		node.setEffect(shadow);
		shadowAnimation.setOnFinished(e -> node.setEffect(null));
		shadowAnimation.play();
	}
	
	
	
	/**
	 * Get node flash timeline. This can be used to make  a node flash. 
	 * @param node - the node for the effect
	 * @param col - the colour of the flash
	 * @param radius - the raioud of the flash
	 * @return flash timeline
	 */
	public static Timeline getFlashTimeLine(DropShadow shadow, double radius, double duration) {


		Timeline shadowAnimation = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 0d)),
				new KeyFrame(Duration.seconds(duration), new KeyValue(shadow.radiusProperty(), radius)));
		shadowAnimation.setAutoReverse(true);
		
		return shadowAnimation;
	}
}
