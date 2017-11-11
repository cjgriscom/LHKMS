package edu.letu.lvkms.javafx;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ContainerChild extends GridPane {
	
	private final String breadcrumbName;
	private final StringProperty subtitle;
	
	public ContainerChild(String breadcrumbName, StringProperty subtitle, Region parent) {
		
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefHeightProperty().bind(parent.heightProperty());
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefWidthProperty().bind(parent.widthProperty());
		this.setBackground(Background.EMPTY);
		
		this.breadcrumbName = breadcrumbName;
		this.subtitle = subtitle;
	}
	
	public StringProperty subTitle() {
		return subtitle;
	}
	
	public String toString() {
		return breadcrumbName;
	}
}
 