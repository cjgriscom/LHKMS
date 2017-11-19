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
 * "defaultContentID": string   <-- Optional ContentID
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
	
	private UUID defaultContentID = null;
	
	public View(String name) {
		this.name = name;
		this.viewID = UUID.randomUUID();
		this.buttonBox = new Menu();
		this.statusBar = new StatusBar();
	}
	
	public View(JSONObject ser) {
		this.name = ser.getString("name");
		this.viewID = UUID.fromString(ser.getString("viewID"));
		if (ser.has("defaultContentID")) 
			this.defaultContentID = UUID.fromString(ser.getString("defaultContentID"));
		this.buttonBox = new Menu(ser.getJSONObject("buttonBox"));
		this.statusBar = new StatusBar(ser.getJSONObject("statusBar"));
	}
	
	public UUID getViewID() {
		return viewID;
	}
	
	public boolean hasDefaultContent() {
		return defaultContentID != null;
	}
	
	public UUID getDefaultContent() {
		return defaultContentID;
	}
	
	public void setDefaultContent(UUID defaultContentID) {
		this.defaultContentID = defaultContentID;
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
		ser.put("viewID", getViewID().toString());
		ser.put("name", getName());
		if (defaultContentID != null) {
			ser.put("defaultContentID", getDefaultContent().toString());
		}
		ser.put("buttonBox", getButtonBox().serialize());
		ser.put("statusBar", getStatusBar().serialize());
		
		return ser;
	}
}
