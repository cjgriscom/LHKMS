package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;

/**
 * JSON Format:
 * 
 * {
 * "name": string
 * "viewID": string
 * 
 * }
 *
 */
public class Screen implements Serializable, JSONSerializable {

	private static final long serialVersionUID = -6743094463689414632L;
	
	private String name;
	private UUID viewID;
	
	public Screen(String name, UUID viewID) {
		this.name = name;
		this.viewID = viewID;
	}
	
	public Screen(JSONObject ser) {
		this.name = ser.getString("name");
		this.viewID = UUID.fromString(ser.getString("viewID"));
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public UUID getViewID() {
		return viewID;
	}
	
	public void setViewID(UUID viewID) {
		this.viewID = viewID;
	}

	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		ser.put("name", getName());
		ser.put("viewID", getViewID().toString());
		return ser;
	}
}
