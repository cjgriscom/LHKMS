package edu.letu.lhkms.javafx;

import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ContentEditor extends ContainerChild {
	
	ObservableList<ContentItem> contentList = FXCollections.observableArrayList();
	
	static class ContentItem {
		private final Content c;
		public ContentItem(Content c) {
			this.c=c;
		}
		
	}
	
	public ContentEditor(ClientApp app, Region parent) {
		super("Content", new SimpleStringProperty("Content Editor"), parent);
		
		app.db.addListener((e,o,n) -> repopulate(n));
		
		Label conf = new Label("Content Editor");
		conf.setFont(Font.font(28));
		ListView<ContentItem> lv = new ListView<>(contentList);
		this.setAlignment(Pos.CENTER);
		int y = 0;
		this.add(conf, 0, y++);
		this.add(lv, 0, y++);
		for (Node n : getChildren()) 
			GridPane.setHalignment(n, HPos.CENTER);
		
		
	}

	private void repopulate(CompleteDatabasePipeline db) {
		contentList.clear();
		db.contentList().forEach((c) -> {
			this.contentList.add(new ContentItem(c));
		});
	}
}
