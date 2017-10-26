package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;

/**
 * JSON Format:
 * 
 * {
 * "contentID": string
 * 
 * }
 *
 */
public class LoadableContent implements Selectable, Serializable, JSONSerializable {

	private static final long serialVersionUID = -6743094463689414632L;
	
	private final UUID contentID;
	
	public LoadableContent(UUID contentID) {
		this.contentID = contentID;
	}
	
	public UUID getContentID() {
		return contentID;
	}

	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		ser.put("contentID", getContentID().toString());
		return ser;
	}
}
