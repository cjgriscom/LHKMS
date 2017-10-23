package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Menu implements Selectable, Serializable {
	
	private static final long serialVersionUID = -6483672860914849698L;
	
	private final ArrayList<Entry<String, Selectable>> entries;
	
	public Menu() {
		this.entries = new ArrayList<>();
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
}
