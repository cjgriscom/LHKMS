package edu.letu.lhkms.javafx.pages.editors.content;

import edu.letu.lhkms.javafx.ContainerChild;
import edu.letu.lhkms.javafx.FXUtil;
import edu.letu.lhkms.structure.Content;
import edu.letu.lhkms.structure.View;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ViewItemEditor extends ContainerChild {
	int gpY = 0;
	View v;
	
	public ViewItemEditor(View v, Region parent) {
		super(v.getName(), v.getName(), parent);
		this.v = v;
		
		TextField name = new TextField(v.getName());
		name.setFont(Font.font(28));
		
		GridPane fields = new GridPane();

		add(name, 0, 0);
		add(FXUtil.vspring(), 0, 1);
		add(fields, 0, 2);
		add(FXUtil.vspring(), 0, 3);
		
		addField(fields, "Type", gpY++);
		addField(fields, "URL", gpY++);
		
		fields.setAlignment(Pos.CENTER);
		this.setAlignment(Pos.CENTER);
	}
	
	
	public TextField addField(GridPane fields, String label, int index) {
		TextField tf = new TextField();
		Label l = new Label(label);
		fields.add(l, 0, index);
		fields.add(tf, 1, index);
		return tf;
	}
}
