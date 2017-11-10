package edu.letu.lvkms.structure;

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
public class Menu implements Selectable, Serializable, JSONSerializable {
	
	private static final long serialVersionUID = -6483672860914849698L;
	
	private final ArrayList<Entry<String, Selectable>> entries;
	
	public Menu() {
		this.entries = new ArrayList<>();
	}
	
	public Menu(JSONObject ser) {
		this.entries = new ArrayList<>();
		ser.getJSONArray("entries").forEach((o) -> {
			JSONObject j = (JSONObject) o;
			String type = j.getString("type");
			String title = j.getString("title");
			JSONObject selectableJSON = j.getJSONObject("selectable");
			Selectable s = null;
			if (type.equals("menu")) s = new Menu(selectableJSON);
			else if (type.equals("content")) s = new LoadableContent(selectableJSON);
			if (s == null) throw new IllegalArgumentException("Type field "+type+" not recognized in Menu entry");
			entries.add(new SimpleEntry<>(title, s));
		});
	}
	
	public void addEntry(String title, Selectable s) {
		this.entries.add(new SimpleEntry<>(title, s));
	}
	
	public String getTitle(int entryNum) {
		return entries.get(entryNum).getKey();
	}
	
	public Selectable getSelectable(int entryNum) {
		return entries.get(entryNum).getValue();
	}
	
	public void remove(int entryNum) {
		entries.remove(entryNum);
	}
	
	/**
	 * 
	 * @return Copy of entries for iteration
	 */
	public List<Entry<String, Selectable>> entryList() {
		return entries;
	}
	
	public boolean canMoveUp(int entryNum) {
		return entryNum > 0;
	}
	
	public boolean canMoveDown(int entryNum) {
		return entryNum < size() - 1;
	}
	
	public void moveUp(int entryNum) {
		if (!canMoveUp(entryNum)) throw new IllegalArgumentException("Cannot move entry upwards");
		entries.add(entryNum-1, entries.remove(entryNum));
	}
	
	public void modeDown(int entryNum) {
		if (!canMoveDown(entryNum)) throw new IllegalArgumentException("Cannot move entry upwards");
		entries.add(entryNum+1, entries.remove(entryNum));
	}
	
	public int size() {
		return entries.size();
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
