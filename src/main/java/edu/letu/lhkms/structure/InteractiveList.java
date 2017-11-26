package edu.letu.lhkms.structure;

public interface InteractiveList {
	boolean exists(int i);
	boolean canMoveUp(int i);
	boolean canMoveDown(int i);
	int moveUp(int i);
	int moveDown(int i);
	void delete(int i);
	int size();
}
