package edu.letu.lvkms.structure;

import java.io.Serializable;

public class LoadableContent implements Selectable, Serializable {

	private static final long serialVersionUID = -6743094463689414632L;
	
	private final String contentID;
	
	public LoadableContent(String contentID) {
		this.contentID = contentID;
	}
	
	public String getContentID() {
		return contentID;
	}
}
