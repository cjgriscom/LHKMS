package edu.letu.lhkms.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ViewsEditor extends ContainerChild {
	public ViewsEditor(ClientApp app, Region parent) {
		super("Views", new SimpleStringProperty("Views Editor"), parent);
		Label conf = new Label("Views Editor");
		conf.setFont(Font.font(28));
		this.setAlignment(Pos.CENTER);
		int y = 0;
		this.add(conf, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		for (Node n : getChildren()) 
			GridPane.setHalignment(n, HPos.CENTER);
	}
}
