package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.UUID;

public class Content implements Serializable {
	
	private static final long serialVersionUID = -5406518350967038592L;
	
	public static enum Type {
		Slides, Doc, YouTube, PDF
	}
	
	private final UUID contentID;
	
	/**
	 * Used to keep a running list of all the views that 
	 * use this Content.  
	 */
	private final TreeMap<UUID, View> users;
	
	private final Type type;
	
	private String name;
	
	private String contentData; // I.e. URL for slides
	
	public Content(String name, Type type, String contentData) {
		this.contentID = UUID.randomUUID();
		this.name = name;
		this.type = type;
		this.contentData = contentData;
		users = new TreeMap<>();
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
	
	public Type getType() {
		return type;
	}
	
	public boolean isUnused() {
		return users.isEmpty();
	}
	
}
