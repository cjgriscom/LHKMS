package edu.letu.lhkms.javafx.pages.editors.content;

import edu.letu.lhkms.javafx.ContainerChild;
import edu.letu.lhkms.javafx.FXUtil;
import edu.letu.lhkms.structure.Content;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ContentItemEditor extends ContainerChild {
	int gpY = 0;
	Content c;
	
	public ContentItemEditor(Content c, Region parent) {
		super(c.getName(), c.getName(), parent);
		this.c = c;
		
		TextField name = new TextField(c.getName());
		name.setFont(Font.font(28));

		add(name, 0, gpY++);
		add(FXUtil.vspring(), 0, gpY++);
		
		addField("Type", gpY++);
		addField("URL", gpY++);
		add(FXUtil.vspring(), 0, gpY++);

	}
	
	
	public TextField addField(String label, int index) {
		TextField tf = new TextField();
		Label l = new Label(label);
		add(l, 0, index);
		add(tf, 1, index);
		return tf;
	}
}
