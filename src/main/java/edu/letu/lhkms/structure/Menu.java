package edu.letu.lhkms.structure;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON Format
 * {
 * "entries": [
 *   {
 *     "type":string     ("menu" or "content")
 *     "title":string
 *     "selectable":{Menu} or {LoadableContent} 
 *   },
 * ...]
 * 
 * }
 */
public class Menu 
		extends InteractiveListImpl<Entry<String, Selectable>> 
		implements Selectable, Serializable, JSONSerializable {
	
	private static final long serialVersionUID = -6483672860914849698L;
	
	public Menu() {
		super(new ArrayList<>());
	}
	
	public Menu(JSONObject ser) {
		super(new ArrayList<>());
		ser.getJSONArray("entries").forEach((o) -> {
			JSONObject j = (JSONObject) o;
			String type = j.getString("type");
			String title = j.getString("title");
			JSONObject selectableJSON = j.getJSONObject("selectable");
			Selectable s = null;
			if (type.equals("menu")) s = new Menu(selectableJSON);
			else if (type.equals("content")) s = new LoadableContent(selectableJSON);
			if (s == null) throw new IllegalArgumentException("Type field "+type+" not recognized in Menu entry");
			list.add(new SimpleEntry<>(title, s));
		});
	}
	
	public void addEntry(String title, Selectable s) {
		this.list.add(new SimpleEntry<>(title, s));
	}
	
	public String getTitle(int entryNum) {
		return list.get(entryNum).getKey();
	}
	
	public Selectable getSelectable(int entryNum) {
		return list.get(entryNum).getValue();
	}
	
	/**
	 * 
	 * @return Copy of entries for iteration
	 */
	public List<Entry<String, Selectable>> entryList() {
		return list;
	}
	
	// Used in menu serialization
	@Override
	public String jsonTypeID() {
		return "menu";
	}

	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		JSONArray entries = new JSONArray();
		for (Entry<String, Selectable> e : entryList()) {
			JSONObject entry = new JSONObject();
			entry.put("type", e.getValue().jsonTypeID());
			entry.put("title", e.getKey());
			entry.put("selectable", e.getValue().serialize());
			entries.put(entry);
		}
		ser.put("entries", entries);
		return ser;
	}
}
