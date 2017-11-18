package edu.letu.lvkms.structure;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.letu.lvkms.db.FlatJSON;

/**
 * JSON Format
 * {
 * "views":   [{View}, ...]
 * "content": [{Content}, ...]
 * "screen":  [{Screen}, ...]
 * 
 * }
 */
public class CompleteDatabasePipeline implements JSONSerializable {
	
	private final ArrayList<Content> content = new ArrayList<>();
	private final ArrayList<View> views = new ArrayList<>();
	private final ArrayList<Screen> screens = new ArrayList<>();
	
	public CompleteDatabasePipeline() {
		// Blank const
	}
	
	public CompleteDatabasePipeline(FlatJSON vl) { // Construct from Server DB
		this(new JSONObject(vl.data()));
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
	
	public boolean canMoveUp(List<?> list, int entryNum) {
		return entryNum > 0;
	}
	
	public boolean canMoveDown(List<?> list, int entryNum) {
		return entryNum < list.size() - 1;
	}
	
	public <T> void moveUp(List<T> list, int entryNum) {
		if (!canMoveUp(list, entryNum)) throw new IllegalArgumentException("Cannot move entry upwards");
		list.add(entryNum-1, list.remove(entryNum));
	}
	
	public <T> void modeDown(List<T> list, int entryNum) {
		if (!canMoveDown(list, entryNum)) throw new IllegalArgumentException("Cannot move entry upwards");
		list.add(entryNum+1, list.remove(entryNum));
	}
	
}
