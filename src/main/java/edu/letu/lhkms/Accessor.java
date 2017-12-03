package edu.letu.lhkms;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Accessor<T extends Database<T>> {
	T src;
	
	Accessor(T src) {
		this.src = src;
	}
	
	public synchronized void commit(Consumer<T> consumer) {
		consumer.accept(src);
		src.db.commit();
	}
	
	public synchronized <V> V commit(Function<T, V> func) {
		try {
			return func.apply(src);
		} finally {
			src.db.commit(); // Commit prior to return
		}
	}
	
	public synchronized void access(Consumer<T> consumer) {
		consumer.accept(src);
	}
	
	public synchronized <V> V access(Function<T, V> func) {
		return func.apply(src);
	}
	
	public T access() {
		return src;
	}
}