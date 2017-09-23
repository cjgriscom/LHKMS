package edu.letu.lvkms;

import java.io.File;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class Database {
	DB lvkmsDB;

	public Database() {
		lvkmsDB = DBMaker
				.fileDB(new File(getDBDirectory(), "master.db"))
				.fileMmapEnable()
				.make();
	}
	
	public void commit(Consumer<DB> consumer) {
		consumer.accept(lvkmsDB);
		lvkmsDB.commit();
	}
	
	public void close() {
		lvkmsDB.close();
	}

	public static File getDBDirectory() {
		File dirs = Host.getConfigurationDirectory();
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		return dirs;
	}
}
