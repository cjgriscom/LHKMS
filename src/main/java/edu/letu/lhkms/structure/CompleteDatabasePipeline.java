package edu.letu.lhkms.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.letu.lhkms.db.FlatJSON;

/**
 * JSON Format
 * {
 * "views":   [{View}, ...]
 * "content": [{Content}, ...]
 * "screen":  [{Screen}, ...]
 * 
 * }
 */
public class CompleteDatabasePipeline implements JSONSerializable, Serializable {
	
	private static final long serialVersionUID = -3724170060943693872L;
	private final ArrayList<Content> content = new ArrayList<>();
	private final ArrayList<View> views = new ArrayList<>();
	private final ArrayList<Screen> screens = new ArrayList<>();
	private final transient InteractiveList<Content> contentMod = new InteractiveList<>(content);
	private final transient InteractiveList<View> viewsMod = new InteractiveList<>(views);
	private final transient InteractiveList<Screen> screensMod = new InteractiveList<>(screens);
	
	public CompleteDatabasePipeline() {
		// Blank const
	}
	
	public CompleteDatabasePipeline(FlatJSON vl) { // Construct from Server DB
		this(new JSONObject(vl.data().get()));
	}
	
	public CompleteDatabasePipeline(JSONObject ser) { // Construct from JSON
		JSONArray contentJ = ser.getJSONArray("content");
		JSONArray viewsJ = ser.getJSONArray("views");
		JSONArray screenJ = ser.getJSONArray("screen");
		
		contentJ.forEach((o) -> {
			JSONObject j = (JSONObject) o;
			Content c = new Content(j);
			content.add(c);
		});
		
		viewsJ.forEach((o) -> {
			JSONObject j = (JSONObject) o;
			View c = new View(j);
			views.add(c);
		});
		
		screenJ.forEach((o) -> {
			JSONObject j = (JSONObject) o;
			Screen c = new Screen(j);
			screens.add(c);
		});
	}
	
	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		JSONArray contentJ = new JSONArray();
		JSONArray viewsJ = new JSONArray();
		JSONArray screensJ = new JSONArray();
		
		for (JSONSerializable js : content) contentJ.put(js.serialize());
		for (JSONSerializable js : views) viewsJ.put(js.serialize());
		for (JSONSerializable js : screens) screensJ.put(js.serialize());
		
		ser.put("content", contentJ);
		ser.put("views", viewsJ);
		ser.put("screen", screensJ);
		
		return ser;
	}

	public List<Content> contentList() {
		return content;
	}

	public List<View> viewList() {
		return views;
	}

	public List<Screen> screenList() {
		return screens;
	}

	public InteractiveList<Content> contentListModifier() {
		return contentMod;
	}

	public InteractiveList<View> viewListModifier() {
		return viewsMod;
	}

	public InteractiveList<Screen> screenListModifier() {
		return screensMod;
	}
	
}
