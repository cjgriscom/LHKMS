package edu.letu.lhkms.javafx;

import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class ContentEditor extends ContainerChild {
	
	private final ObservableList<ContentItem> contentList = FXCollections.observableArrayList();
	private final ClientApp app;
	
	private class ContentItem extends Label {
		final Content c;
		ContentItem(Content c) {
			super(c.getName());
			if (!c.isUnused(app.db.get().viewList())) {
				this.setStyle("-fx-font-weight:bold;");
			}
			this.c=c;
		}
	}
	
	public ContentEditor(ClientApp app, Region parent) {
		super("Content", new SimpleStringProperty("Content Editor"), parent);
		
		this.app = app;
		
		app.db.addListener((e,o,n) -> repopulate(n));
		
		Label conf = new Label("Content Editor");
		conf.setFont(Font.font(28));
		ListView<ContentItem> lv = new ListView<>(contentList);
		ListManager lm = new ListManager(lv, ()->{return app.db.get().contentListModifier();}, () -> {
			repopulate(app.db.get());
			app.commitDB();
		});
		
		HBox navigator = new HBox(lv, lm);
		
		navigator.translateXProperty().bind(lm.widthProperty().divide(2)); // Center with respect to lv
		
		this.setAlignment(Pos.CENTER);
		int y = 0;
		this.add(conf, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
		this.add(navigator, 0, y++);
		this.add(FXUtil.vspring(), 0, y++);
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
