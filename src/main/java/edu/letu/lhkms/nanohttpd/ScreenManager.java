package edu.letu.lhkms.nanohttpd;

public class ScreenManager {
	private long lastUpdate = System.currentTimeMillis();
	
	public long lastUpdateDifferential() {
		
		return System.currentTimeMillis() - lastUpdate;
	}
	
	public void registerDatabaseUpdate() {
		this.lastUpdate = System.currentTimeMillis();
	}
}
