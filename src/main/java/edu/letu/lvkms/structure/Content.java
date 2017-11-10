package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON Format
 * {
 * "contentID": string
 * "users": [string, ...]    (list of viewIDs that use this content)
 * "name": string
 * "type": string            (Slides, Doc, YouTube, PDF)
 * "contentData": string     (usually URL of content)
 * 
 * }
 *
 */
public class Content implements Serializable, JSONSerializable {
	
	private static final long serialVersionUID = -5406518350967038592L;
	
	public static enum Type {
		Slides, Doc, YouTube, PDF
	}
	
	private final UUID contentID;
	
	/**
	 * Used to keep a running list of all the views that 
	 * use this Content.  
	 */
	private final TreeSet<UUID> users; // Set of viewIDs that use this Content 
	
	private final Type type;
	
	private String name;
	
	private String contentData; // I.e. URL for slides
	
	public Content(String name, Type type, String contentData) {
		this.contentID = UUID.randomUUID();
		this.name = name;
		this.type = type;
		this.contentData = contentData;
		users = new TreeSet<>();
	}
	
	public Content(JSONObject ser) {
		contentID = UUID.fromString(ser.getString("contentID"));
		name = ser.getString("name");
		type = Type.valueOf(ser.getString("type"));
		contentData = ser.getString("contentData");
		users = new TreeSet<>();
		ser.getJSONArray("users").forEach((o) -> users.add(UUID.fromString(o.toString())));
	}

	public UUID getContentID() {
		return contentID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContentData() {
		return contentData;
	}
	
	public void setContentData(String contentData) {
		this.contentData = contentData;
	}
	
	public Collection<UUID> getUsers() {
		return users;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isUnused() {
		return users.isEmpty();
	}
	
	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		JSONArray users = new JSONArray();
		for (UUID user : getUsers()) users.put(user.toString());
		ser.put("contentID", getContentID().toString());
		ser.put("users", users);
		ser.put("name", getName());
		ser.put("type", getType().name());
		ser.put("contentData", getContentData());
		
		return ser;
	}
	
}
