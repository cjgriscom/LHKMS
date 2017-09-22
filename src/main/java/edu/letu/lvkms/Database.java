package edu.letu.lvkms;

import java.io.File;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Database {
	public static void initDB() {
		
		DB lvkmsDB = DBMaker.fileDB(new File(getDBDirectory(),"master.db")).make();
		ConcurrentMap map = lvkmsDB.hashMap("asdasdasd").createOrOpen();
		map.put("something", "here");
		
	}
	
	public static File getDBDirectory() {
		File dirs = Host.getConfigurationDirectory();
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		return dirs;
	}
}
