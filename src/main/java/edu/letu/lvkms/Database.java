package edu.letu.lvkms;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public abstract class Database<T extends Database<T>> {
	
	@SuppressWarnings("unchecked")
	Accessor<T> acc = new Accessor<T>((T) this);
	protected DB db;

	public Database(String name) {
		db = DBMaker
				.fileDB(new File(getDBDirectory(), name+".db"))
				.fileMmapEnable()
				.make();
	}
	
	public void close() {
		db.close();
	}

	public static File getDBDirectory() {
		File dirs = Host.getConfigurationDirectory();
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		return dirs;
	}
	
	public Accessor<T> accessor() {
		return acc;
	}
}
