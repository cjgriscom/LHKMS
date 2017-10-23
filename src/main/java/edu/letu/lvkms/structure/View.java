package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class View implements Serializable {
	
	private static final long serialVersionUID = 5841199622853631198L;
	
	private final UUID viewID;
	
	private String name;
	
	private final ArrayList<Selectable> buttonBox;
	
	private final StatusBar statusBar;
	
	public View(String name) {
		this.name = name;
		this.viewID = UUID.randomUUID();
		this.buttonBox = new ArrayList<>();
		this.statusBar = new StatusBar();
	}
	
	public UUID getViewID() {
		return viewID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Selectable> getButtonBox() {
		return buttonBox;
	}
	
	public StatusBar statusBar() {
		return statusBar;
	}
	
}
