package edu.letu.lhkms.db;

import org.mapdb.Atomic;

import edu.letu.lhkms.Database;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;

public class FlatJSON extends Database<FlatJSON> {
	private Atomic.String dataCache;
	public FlatJSON() {
		super("FlatJSON");
		if (!this.db.exists("FlatJSON")) {
			dataCache = this.db.atomicString("FlatJSON").create();
			setFrom(new CompleteDatabasePipeline());
		} else {
			dataCache = this.db.atomicString("FlatJSON").open();
		}
	}
	
	public void setFrom(CompleteDatabasePipeline db) {
		dataCache.set(db.serialize().toString());
		this.db.commit();
	}
	
	public Atomic.String data() {
		return dataCache;
	}
}
