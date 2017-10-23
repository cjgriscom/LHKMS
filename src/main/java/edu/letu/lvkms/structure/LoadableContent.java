package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.UUID;

public class LoadableContent implements Selectable, Serializable {

	private static final long serialVersionUID = -6743094463689414632L;
	
	private final UUID contentID;
	
	public LoadableContent(UUID contentID) {
		this.contentID = contentID;
	}
	
	public UUID getContentID() {
		return contentID;
	}
}
