package edu.letu.lhkms.structure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.json.JSONObject;

/**
 * JSON Format
 * {
 * "contentID": string
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
	
	private final Type type;
	
	private String name;
	
	private String contentData; // I.e. URL for slides
	
	public Content(String name, Type type, String contentData) {
		this.contentID = UUID.randomUUID();
		this.name = name;
		this.type = type;
		this.contentData = contentData;
	}
	
	public Content(JSONObject ser) {
		contentID = UUID.fromString(ser.getString("contentID"));
		name = ser.getString("name");
		type = Type.valueOf(ser.getString("type"));
		contentData = ser.getString("contentData");
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
	
	public Set<UUID> getUsers(Collection<View> views) {
		TreeSet<UUID> tree = new TreeSet<>();
		populateUsersList(tree, null, views);
		return tree;
	}
	
	// Recursively fill out the users tree
	@SuppressWarnings("unchecked")
	private void populateUsersList(TreeSet<UUID> tree, View source, Collection<?> someList) {
		for (Object o : someList ) {
			// Sanitize entries
			if (o instanceof Entry) {
				o = ((Entry<String, Selectable>) o).getValue();
			}
			
			if (o instanceof View) {
				View v = (View) o;
				// Add defaultContent entries
				if (v.getDefaultContent() != null && v.getDefaultContent().equals(this.getContentID())) tree.add(v.getViewID());
				populateUsersList(tree, v, v.getButtonBox().entryList());
			} else if (o instanceof Menu) {
				Menu m = (Menu) o;
				populateUsersList(tree, source, m.entryList());
			} else if (o instanceof LoadableContent) {
				// Base case
				LoadableContent c = (LoadableContent) o;
				if (c.getContentID().equals(this.getContentID())) tree.add(source.getViewID());
			}
		}
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isUnused(Collection<View> views) {
		return getUsers(views).isEmpty();
	}
	
	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		ser.put("contentID", getContentID().toString());
		ser.put("name", getName());
		ser.put("type", getType().name());
		ser.put("contentData", getContentData());
		
		return ser;
	}
	
}
