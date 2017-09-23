package edu.letu.lvkms;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Accessor<T extends Database<T>> {
	T src;
	
	Accessor(T src) {
		this.src = src;
	}
	
	public void commit(Consumer<T> consumer) {
		consumer.accept(src);
		src.db.commit();
	}
	
	public void access(Consumer<T> consumer) {
		consumer.accept(src);
	}
	
	public <V> V access(Function<T, V> func) {
		return func.apply(src);
	}
	
	public T access() {
		return src;
	}
}