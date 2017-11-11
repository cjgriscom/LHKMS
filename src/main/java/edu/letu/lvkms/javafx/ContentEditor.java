package edu.letu.lvkms.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ContentEditor extends ContainerChild {
	public ContentEditor(ClientApp app, Region parent) {
		super("Content", new SimpleStringProperty("Content Editor"), parent);
		Label conf = new Label("Content Editor");
		conf.setFont(Font.font(28));
		Button content = new Button("Edit Content");
		content.setOnAction(app::handleEditContent);
		Button views = new Button("Edit Views");
		views.setOnAction(app::handleEditViews);
		Button screens = new Button("Edit Screens");
		screens.setOnAction(app::handleEditScreens);
		content.setPrefWidth(150); content.setPrefHeight(40);
		views.setPrefWidth(150);   views.setPrefHeight(40);
		screens.setPrefWidth(150); screens.setPrefHeight(40);
		this.setAlignment(Pos.CENTER);
		int y = 0;
		this.add(conf, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		this.add(content, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		this.add(views, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		this.add(screens, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		for (Node n : getChildren()) 
			GridPane.setHalignment(n, HPos.CENTER);
	}
}
