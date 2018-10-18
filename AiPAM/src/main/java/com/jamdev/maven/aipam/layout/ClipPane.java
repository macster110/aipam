package com.jamdev.maven.aipam.layout;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;


public class ClipPane extends BorderPane {

	public ClipPane(PamSneView pamSneView) {
		this.setCenter(new Button("Hello!!!!"));
	}

}
