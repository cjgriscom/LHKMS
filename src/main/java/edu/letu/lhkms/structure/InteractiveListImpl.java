package edu.letu.lhkms.structure;

import java.util.ArrayList;

public class InteractiveListImpl<T> implements InteractiveList {
	protected ArrayList<T> list;
	public InteractiveListImpl(ArrayList<T> src) {
		this.list = src;
	}
	
	@Override
	public boolean exists(int i) {
		return i >= 0 && i < list.size();
	}

	@Override
	public boolean canMoveUp(int i) {
		return i > 0;
	}

	@Override
	public boolean canMoveDown(int i) {
		return i < list.size() - 1;
	}

	@Override
	public int moveUp(int i) {
		if (!canMoveUp(i)) throw new IllegalArgumentException("Cannot move entry up");
		list.add(i-1, list.remove(i));
		return i-1;
	}

	@Override
	public int moveDown(int i) {
		if (!canMoveDown(i)) throw new IllegalArgumentException("Cannot move entry down");
		list.add(i+1, list.remove(i));
		return i+1;
	}

	@Override
	public void delete(int i) {
		list.remove(i);
	}

	@Override
	public int size() {
		return list.size();
	}

}
