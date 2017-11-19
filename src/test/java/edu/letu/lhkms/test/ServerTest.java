package edu.letu.lhkms.test;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import edu.letu.lhkms.HTTPUtil;
import edu.letu.lvkms.nanohttpd.App;
import edu.letu.lvkms.structure.CompleteDatabasePipeline;
import edu.letu.lvkms.structure.Content;
import edu.letu.lvkms.structure.LoadableContent;
import edu.letu.lvkms.structure.Screen;
import edu.letu.lvkms.structure.View;

public class ServerTest extends TestClass {
	
	CompleteDatabasePipeline testDB = new CompleteDatabasePipeline();
	public ServerTest() {setupTest();}
	private void setupTest() {
		Content slidesDoc = new Content("Virtual Prototype", 
				Content.Type.Slides, "https://docs.google.com/presentation/d/e/2PACX-1vRYaSEFJhJibDZ__KUn0Rn_VttvgEge9RpZ-XC753ZOgihALxtL5o3UonkD10-Qs2v0oPy-KfWgt--T/embed?start=true&loop=true&delayms=3000");
		Content baseball = new Content("Baseball", 
				Content.Type.YouTube, "https://www.youtube.com/embed/kt5VeNNf7iI");
		
		View testView1 = new View("Test View 1");
		testView1.getStatusBar().setWeather(true);
		testView1.getButtonBox().addEntry("VirtProto Btn", new LoadableContent(slidesDoc.getContentID()));
		testView1.getButtonBox().addEntry("Baseball!!!", new LoadableContent(baseball.getContentID()));
		testView1.setDefaultContent(baseball.getContentID());
		testDB.viewList().add(testView1);
		
		testDB.contentList().add(slidesDoc);
		testDB.contentList().add(baseball);
		
		Screen onlyScreen = new Screen("LeftScreen", testView1.getViewID());
		testDB.screenList().add(onlyScreen);
	}
	
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
		final String[] oldDB = new String[1];
		final String[] newDB = new String[1];
		try {
			runOnServer(out, () -> {
				oldDB[0] = HTTPUtil.sendGet(mainURL+"/getDatabase"); // Preserve

				JSONObject newDBModified = new JSONObject(testDB.serialize().toString(3));
				CompleteDatabasePipeline newDBM_java = new CompleteDatabasePipeline(newDBModified);
				
				newDBM_java.contentList().get(0).setName("MODIFIED");
				newDB[0] = newDBM_java.serialize().toString(3);
				
				HTTPUtil.sendPost(mainURL+"/setDatabase", newDB[0]);
				
				out.println("Test1: Set database to " + newDB[0].hashCode());
				return true;
			});
			boolean test1Passed = runOnServer(out, () -> {
				String retained = HTTPUtil.sendGet(mainURL+"/getDatabase"); // Preserve
				
				out.println("Test1: Database retained to " + retained.hashCode());
				out.println(retained);
				
				JSONObject newDBModified = new JSONObject(testDB.serialize().toString(3));
				CompleteDatabasePipeline newDBM_java = new CompleteDatabasePipeline(newDBModified);
				
				newDBM_java.contentList().get(0).setName("MODIFIED");
				newDB[0] = newDBM_java.serialize().toString(3);
				
				HTTPUtil.sendPost(mainURL+"/setDatabase", newDB[0]);
				
				out.println("Test2: Set database to " + newDB[0].hashCode());
				return retained.hashCode() == newDB[0].hashCode();
			});
			boolean test2Passed = runOnServer(out, () -> {
				String retained = HTTPUtil.sendGet(mainURL+"/getDatabase"); // Preserve
				
				out.println("Test2: Database retained to " + retained.hashCode());
				out.println(retained);

				HTTPUtil.sendPost(mainURL+"/setDatabase", oldDB[0]); // Restore old
				
				return retained.hashCode() == newDB[0].hashCode();
			});
			return test1Passed && test2Passed;
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
