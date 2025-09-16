package com.jamdev.maven.aipam.layout;

import java.util.Random;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedSpectrogramIcon extends Pane {

	  private final int cellSize = 4;         // size of each pixel block
	    private final int rows;                 // number of vertical cells
	    private final int cols;                 // number of horizontal cells
	    private final Rectangle[][] pixels;     // grid of rectangles
	    private Timeline animation;             // scrolling animation
	    private final Random rng = new Random();

	    public AnimatedSpectrogramIcon(int width, int height, boolean animate) {
	        setPrefSize(width, height);

	        cols = width / cellSize;
	        rows = height / cellSize;
	        pixels = new Rectangle[rows][cols];

	        // background box
	        setStyle("-fx-border-color: black; -fx-background-color: white;");

	        // build pixel grid
	        for (int r = 0; r < rows; r++) {
	            for (int c = 0; c < cols; c++) {
	                Rectangle rect = new Rectangle(cellSize, cellSize);
	                rect.setX(c * cellSize);
	                rect.setY(r * cellSize);
	                rect.setFill(randomShade());
	                pixels[r][c] = rect;
	                getChildren().add(rect);
	            }
	        }

	        if (animate) {
	            startAnimation();
	        }
	    }

	    /** Creates a random color shade to simulate spectrogram intensity. */
	    private Color randomShade() {
	        // generate color in a "contour-like" gradient
	        double v = rng.nextDouble();
	        if (v < 0.2) return Color.rgb(15, 50, 100);
	        if (v < 0.4) return Color.rgb(30, 90, 150);
	        if (v < 0.6) return Color.rgb(80, 160, 200);
	        if (v < 0.8) return Color.rgb(180, 210, 240);
	        return Color.rgb(255, 180, 90);
	    }

	    /** Start the leftward scrolling animation. */
	    private void startAnimation() {
	        animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> scrollOneStep()));
	        animation.setCycleCount(Animation.INDEFINITE);
	        animation.play();
	    }

	    /** Shift all columns left by one and add a new column on the right. */
	    private void scrollOneStep() {
	        for (int r = 0; r < rows; r++) {
	            for (int c = 0; c < cols - 1; c++) {
	                pixels[r][c].setFill(pixels[r][c + 1].getFill());
	            }
	            pixels[r][cols - 1].setFill(randomShade());
	        }
	    }

	    /** Allow turning animation on or off after creation. */
	    public void setAnimated(boolean animate) {
	        if (animate) {
	            if (animation == null) startAnimation();
	        } else {
	            if (animation != null) {
	                animation.stop();
	                animation = null;
	            }
	        }
	    }

		public static Node getIcon() {
			// TODO Auto-generated method stub
			return new AnimatedSpectrogramIcon(AIPamView.iconSize, AIPamView.iconSize, true);
		}
}
