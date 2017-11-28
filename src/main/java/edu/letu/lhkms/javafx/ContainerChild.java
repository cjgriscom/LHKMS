package edu.letu.lhkms.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ContainerChild extends GridPane {
	
	private final StringProperty breadcrumbName;
	private final StringProperty subtitle;
	
	public ContainerChild(String breadcrumbName, String subtitle, Region parent) {
		this(new SimpleStringProperty(breadcrumbName), 
				subtitle == null ? null : new SimpleStringProperty(subtitle), 
				parent);
	}
	
	public ContainerChild(StringProperty breadcrumbName, StringProperty subtitle, Region parent) {
		
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefHeightProperty().bind(parent.heightProperty());
		this.maxHeightProperty().bind(parent.heightProperty());
		this.prefWidthProperty().bind(parent.widthProperty());
		this.setBackground(Background.EMPTY);
		
		this.breadcrumbName = breadcrumbName;
		this.subtitle = subtitle;
	}
	
	public StringProperty breadcrumbName() {
		return breadcrumbName;
	}
	
	public StringProperty subTitle() {
		return subtitle;
	}
	
	public String toString() {
		return breadcrumbName.get();
	}
}
 