package com.jamdev.maven.aipam.layout.utilsFX;


import com.sun.javafx.scene.control.skin.ButtonSkin;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
//import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Fluent design button skin with animation when the button is clicked.
 * <p>
 * Ripple effect inspired by 
 * 
 * @author Jamie Macaulay
 *
 */
public class FluentButtonSkin extends ButtonSkin {
	
	private Circle circleRipple;
	private Rectangle rippleClip = new Rectangle();
	private Duration rippleDuration =  Duration.millis(500);
	private double lastRippleHeight = 0;
	private double lastRippleWidth = 0;
	//	    private Color rippleColor = new Color(0, 0, 0, 0.11);
	private Color rippleColor = new Color(1,1,1,0.1); 
	
	private Color borderColour = new Color(1,1,1,0.13); 

	//private Color rippleColor =  Color.WHITE; 
	
	
	public FluentButtonSkin(Button button) {
		super(button);
		createRippleEffect( button);
		this.getChildren().add(0,circleRipple);
		// TODO Auto-generated constructor stub
	}
	
	private void createRippleEffect(Button button) {		
		
//        Region colour = (Region) this.lookup("button:hover");
//        Background background=colour.getBackground();
		
		
		circleRipple = new Circle(0.1, rippleColor);
		
		circleRipple.setOpacity(0.0);
		
		//FIXME - for some reason adding the stroke caused weird behaviour in the mnouse hover
		//over buttons. 
		//circleRipple.setStrokeWidth(40);
		//circleRipple.setStroke(borderColour);
		
		// Optional box blur on ripple - smoother ripple effect
		circleRipple.setEffect(new GaussianBlur(10));
		// Fade effect bit longer to show edges on the end of animation
		final FadeTransition fadeTransition = new FadeTransition(rippleDuration, circleRipple);
		fadeTransition.setInterpolator(Interpolator.EASE_OUT);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		final Timeline scaleRippleTimeline = new Timeline();
		final SequentialTransition parallelTransition = new SequentialTransition();
		parallelTransition.getChildren().addAll(
				scaleRippleTimeline,
				fadeTransition
				);
		// When ripple transition is finished then reset circleRipple to starting point  
		parallelTransition.setOnFinished(event -> {
			circleRipple.setOpacity(0.0);
			circleRipple.setRadius(0.1);
			//circleRipple.setEffect(new GaussianBlur()); //add blur effect for Jmetro
		});
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			parallelTransition.stop();
			// Manually fire finish event
			parallelTransition.getOnFinished().handle(null);
			circleRipple.setCenterX(event.getX());
			circleRipple.setCenterY(event.getY());
			// Recalculate ripple size if size of button from last time was changed
			if (button.getWidth() != lastRippleWidth || button.getHeight() != lastRippleHeight)
			{
				lastRippleWidth = button.getWidth();
				lastRippleHeight = button.getHeight();
				rippleClip.setWidth(lastRippleWidth);
				rippleClip.setHeight(lastRippleHeight);
				// try block because of possible null of Background, fills ...
				try {
					rippleClip.setArcHeight(button.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
					rippleClip.setArcWidth(button.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
					circleRipple.setClip(rippleClip);
				} catch (Exception e) {
				}
				// Getting 45% of longest button's length, because we want edge of ripple effect always visible
				double circleRippleRadius = Math.max(button.getHeight(), button.getWidth()) * 0.65;
				final KeyValue keyValue = new KeyValue(circleRipple.radiusProperty(), circleRippleRadius, Interpolator.EASE_OUT);
				final KeyFrame keyFrame = new KeyFrame(rippleDuration, keyValue);
				scaleRippleTimeline.getKeyFrames().clear();
				scaleRippleTimeline.getKeyFrames().add(keyFrame);
			}
			parallelTransition.playFromStart();
		});
	}

}
