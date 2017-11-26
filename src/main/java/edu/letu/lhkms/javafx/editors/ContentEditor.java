package edu.letu.lhkms.javafx.editors;

import java.util.List;

import edu.letu.lhkms.javafx.ClientApp;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import edu.letu.lhkms.structure.InteractiveList;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ContentEditor extends TopLevelListEditor<Content> {
	public ContentEditor(ClientApp app, Region parent) {
		super("Content", "Content Editor", parent, app);
	}
	
	public Label constructListLabel(Content item) {
		Label l = new Label(item.getName());
		if (!item.isUnused(app.db.get().viewList())) {
			l.setStyle("-fx-font-weight:bold;");
		}
		return l;
	}

	@Override
	public List<Content> getMatchingDatabaseList(CompleteDatabasePipeline db) {
		return db.contentList();
	}

	@Override
	public InteractiveList getMatchingModifierList(CompleteDatabasePipeline db) {
		return db.contentListModifier();
	}
}
