package edu.letu.lhkms.test;

import java.io.IOException;
import java.io.PrintWriter;

import edu.letu.lvkms.nanohttpd.App;

public class ServerTest extends TestClass {
	
	private static final String mainURL = "http://127.0.0.1:8080";
	
	@TestCase
	boolean serverGetTest(final PrintWriter out) {
		try {
			return runOnServer(out, () -> {

				System.out.println(HTTPUtil.sendGet("http://127.0.0.1:8080/getDatabase"));
				return true;
			});
		} catch (Exception e) {
			System.out.println("Failed to contact server");
			e.printStackTrace(out);
			return false;
		}
	}
	
	@TestCase
	boolean retentionTest(final PrintWriter out) {
		try {
			return runOnServer(out, () -> {

				String oldDB = HTTPUtil.sendGet(mainURL+"/getDatabase");
				String newDB = "{\"retentionKey\":\"retentionValue\"}";
				
				System.out.println(HTTPUtil.sendPost(mainURL+"/setDatabase", newDB));
				
				
				System.out.println();
				return true;
			});
		} catch (Exception e) {
			System.out.println("Failed to contact server");
			e.printStackTrace(out);
			return false;
		}
	}
	
	private boolean runOnServer(final PrintWriter out, SupplierExp<Boolean> r) throws Exception {
		App server;
		try {
			server = new App();
		} catch (IOException e) {
			e.printStackTrace(out);
			System.out.println("Failed to start server: " + e.getClass());
			return false;
		}
		Exception retainE = null;
		boolean ret = false;
		try {
			ret = r.get();
		} catch (Exception e) {
			retainE = e;
		}
		
		server.stop();
		if (retainE != null) throw retainE;
		else return ret;
	}
}
