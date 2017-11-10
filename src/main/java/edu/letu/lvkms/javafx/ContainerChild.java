package edu.letu.lvkms.javafx;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ContainerChild extends GridPane {
	public ContainerChild(Region parent) {
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefHeightProperty().bind(parent.heightProperty());
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefWidthProperty().bind(parent.widthProperty());
		this.setBackground(Background.EMPTY);
	}
}
 