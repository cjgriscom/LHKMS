package edu.letu.lhkms.javafx.pages.editors.content;

import java.util.Collection;

import edu.letu.lhkms.javafx.ContainerChild;
import edu.letu.lhkms.javafx.FXUtil;
import edu.letu.lhkms.structure.Screen;
import edu.letu.lhkms.structure.View;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ScreenItemEditor extends ContainerChild {
	int gpY = 0;
	Screen s;
	
	public ScreenItemEditor(Screen s, Collection<View> views, Region parent) {
		super(s.getName(), s.getName(), parent);
		this.s = s;
		
		TextField name = new TextField(s.getName());
		name.setFont(Font.font(28));
		
		GridPane fields = new GridPane();

		add(name, 0, 0);
		add(FXUtil.vspring(), 0, 1);
		add(fields, 0, 2);
		add(FXUtil.vspring(), 0, 3);
		
		fields.add(new Label("Attached View"), 0, gpY);
		ComboBox<String> viewList = new ComboBox<String>();
		for (View v : views) {
			viewList.getItems().add(v.getName());
		}
		fields.add(viewList, 1, gpY++);
		
		fields.setAlignment(Pos.CENTER);
		this.setAlignment(Pos.CENTER);
	}
}
