package edu.letu.lhkms.javafx.pages.editors;

import java.util.List;

import edu.letu.lhkms.javafx.ClientApp;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.InteractiveList;
import edu.letu.lhkms.structure.View;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ViewsEditor extends TopLevelListEditor<View> {
	public ViewsEditor(ClientApp app, Region parent) {
		super("Views", "View Editor", parent, app, (v) -> app.handleEditItem(v.getViewID()));
	}
	
	public Label constructListLabel(View item) {
		Label l = new Label(item.getName());
		//TODO
		return l;
	}

	@Override
	public List<View> getMatchingDatabaseList(CompleteDatabasePipeline db) {
		return db.viewList();
	}

	@Override
	public InteractiveList<View> getMatchingModifierList(CompleteDatabasePipeline db) {
		return db.viewListModifier();
	}
}