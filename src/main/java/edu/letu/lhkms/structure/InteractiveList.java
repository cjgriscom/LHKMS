package edu.letu.lhkms.structure;

import java.util.ArrayList;

public class InteractiveList<T> {
	protected ArrayList<T> list;
	public InteractiveList(ArrayList<T> src) {
		this.list = src;
	}
	
	public T get(int i) {
		return list.get(i);
	}
	
	public boolean exists(int i) {
		return i >= 0 && i < list.size();
	}

	public boolean canMoveUp(int i) {
		return i > 0;
	}

	public boolean canMoveDown(int i) {
		return i < list.size() - 1;
	}

	public int moveUp(int i) {
		if (!canMoveUp(i)) throw new IllegalArgumentException("Cannot move entry up");
		list.add(i-1, list.remove(i));
		return i-1;
	}

	public int moveDown(int i) {
		if (!canMoveDown(i)) throw new IllegalArgumentException("Cannot move entry down");
		list.add(i+1, list.remove(i));
		return i+1;
	}

	public void delete(int i) {
		list.remove(i);
	}

	public int size() {
		return list.size();
	}

}
