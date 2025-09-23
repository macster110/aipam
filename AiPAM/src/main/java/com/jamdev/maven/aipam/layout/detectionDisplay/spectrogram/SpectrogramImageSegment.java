package com.jamdev.maven.aipam.layout.detectionDisplay.spectrogram;

import javafx.scene.image.WritableImage;

/**
 * Represents a segment of the spectrogram as a WritableImage, with metadata for time and scale.
 *
 * @author Jamie Macaulay
 */
public class SpectrogramImageSegment {
    private final WritableImage image;
    private final double startTimeMillis;
    private final double durationMillis;
    private final double scaleMillisPerPixel;

    public SpectrogramImageSegment(WritableImage image, double startTimeMillis, double durationMillis, double scaleMillisPerPixel) {
        this.image = image;
        this.startTimeMillis = startTimeMillis;
        this.durationMillis = durationMillis;
        this.scaleMillisPerPixel = scaleMillisPerPixel;
    }

    public WritableImage getImage() {
        return image;
    }

    public double getStartTimeMillis() {
        return startTimeMillis;
    }

    public double getDurationMillis() {
        return durationMillis;
    }

    public double getScaleMillisPerPixel() {
        return scaleMillisPerPixel;
    }

    public boolean covers(double timeMillis, double scaleMillisPerPixel) {
        return this.scaleMillisPerPixel == scaleMillisPerPixel &&
               timeMillis >= startTimeMillis &&
               timeMillis < startTimeMillis + durationMillis;
    }
}
