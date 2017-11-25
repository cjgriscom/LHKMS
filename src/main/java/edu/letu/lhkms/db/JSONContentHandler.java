package edu.letu.lhkms.db;

import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.Response;

public interface JSONContentHandler {
	public Response handleJSONRequest(Map<String, List<String>> args);
}
