package edu.letu.lhkms.javafx;

import java.util.List;

import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.InteractiveList;
import edu.letu.lhkms.structure.Screen;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ScreensEditor extends TopLevelListEditor<Screen> {
	public ScreensEditor(ClientApp app, Region parent) {
		super("Screens", "Screen Editor", parent, app);
	}
	
	public Label constructListLabel(Screen item) {
		Label l = new Label(item.getName());
		//TODO
		return l;
	}

	@Override
	public List<Screen> getMatchingDatabaseList(CompleteDatabasePipeline db) {
		return db.screenList();
	}

	@Override
	public InteractiveList getMatchingModifierList(CompleteDatabasePipeline db) {
		return db.screenListModifier();
	}
}