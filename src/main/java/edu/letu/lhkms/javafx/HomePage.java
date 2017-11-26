package edu.letu.lhkms.javafx;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class HomePage extends ContainerChild {
	public HomePage(ClientApp app, Region parent) {
		super("Home", null, parent);
		Label conf = new Label("LHKMS Configuration");
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
		
		content.disableProperty().bind(app.loaded.not());
		views.disableProperty().bind(app.loaded.not());
		screens.disableProperty().bind(app.loaded.not());
		
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
