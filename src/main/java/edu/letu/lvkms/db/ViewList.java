package edu.letu.lvkms.db;

import java.util.List;
import java.util.Map;

import edu.letu.lvkms.Database;
import fi.iki.elonen.NanoHTTPD.Response;

public class ViewList extends Database<ViewList> implements JSONContentHandler {

	public ViewList() {
		super("ViewList");
	}

	@Override
	public Response handleJSONRequest(Map<String, List<String>> args) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
