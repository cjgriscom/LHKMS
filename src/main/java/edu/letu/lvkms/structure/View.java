package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;

/**
 * JSON Format:
 * 
 * {
 * "viewID": string
 * "name": string
 * "buttonBox": {Menu}
 * "statusBar": {StatusBar}
 * }
 */
public class View implements Serializable, JSONSerializable {
	
	private static final long serialVersionUID = 5841199622853631198L;
	
	private final UUID viewID;
	
	private String name;
	
	private final Menu buttonBox;
	
	private final StatusBar statusBar;
	
	public View(String name) {
		this.name = name;
		this.viewID = UUID.randomUUID();
		this.buttonBox = new Menu();
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
	
	public Menu getButtonBox() {
		return buttonBox;
	}
	
	public StatusBar getStatusBar() {
		return statusBar;
	}
	
	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		ser.put("viewID", getViewID());
		ser.put("name", getName());
		ser.put("buttonBox", getButtonBox().serialize());
		ser.put("statusBar", getStatusBar().serialize());
		
		return ser;
	}
}
