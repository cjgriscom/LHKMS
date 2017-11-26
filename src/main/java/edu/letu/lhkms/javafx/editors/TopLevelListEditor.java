package edu.letu.lhkms.javafx.editors;

import java.util.List;

import edu.letu.lhkms.javafx.ClientApp;
import edu.letu.lhkms.javafx.ContainerChild;
import edu.letu.lhkms.javafx.FXUtil;
import edu.letu.lhkms.javafx.ListManager;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.InteractiveList;
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

public abstract class TopLevelListEditor<T> extends ContainerChild {
	
	private final ObservableList<Label> labelList = FXCollections.observableArrayList();
	protected final ClientApp app;

	public TopLevelListEditor(String breadcrumbName, String name, Region parent, ClientApp app) {
		super(breadcrumbName, new SimpleStringProperty(name), parent);
		
		this.app = app;
		
		app.db.addListener((e,o,n) -> repopulate(n));
		
		Label conf = new Label(name);
		conf.setFont(Font.font(28));
		ListView<Label> lv = new ListView<>(labelList);
		ListManager lm = new ListManager(lv, ()->{return getMatchingModifierList(app.db.get());}, () -> {
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
		labelList.clear();
		getMatchingDatabaseList(db).forEach((c) -> {
			this.labelList.add(constructListLabel(c));
		});
	}
	
	public abstract List<T> getMatchingDatabaseList(CompleteDatabasePipeline db);
	public abstract InteractiveList getMatchingModifierList(CompleteDatabasePipeline db);
	public abstract Label constructListLabel(T item);
}
