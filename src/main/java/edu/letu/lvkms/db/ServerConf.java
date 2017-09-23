package edu.letu.lvkms.db;

import org.mapdb.Atomic.Boolean;

import edu.letu.lvkms.Database;

public class ServerConf extends Database<ServerConf> {
	Boolean configured = db.atomicBoolean("domainCodeSet").createOrOpen();
	
	
	public ServerConf() {
		super("serverConf");
		
	}
	
	public boolean isConfigured() {
		return configured.get();
	}
	
	public void setConfigured() {
		configured.set(true);
	}
	
	public long sessionExpirationTime () {
		return 1000*60*60*24; // TODO
	}
	
	/*public void setSessionExpirationTime() {
		
	}*/

}
