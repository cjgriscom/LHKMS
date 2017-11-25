package edu.letu.lhkms.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import org.json.JSONObject;

import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import edu.letu.lhkms.structure.LoadableContent;
import edu.letu.lhkms.structure.Screen;
import edu.letu.lhkms.structure.View;

public class SerialTest extends TestClass {
	
	CompleteDatabasePipeline testDB = new CompleteDatabasePipeline();
	public SerialTest() {setupTest();}
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
	
	@TestCase
	boolean screenJSONSerialTest(final PrintWriter out) {
		JSONObject db1 = testDB.screenList().get(0).serialize();
		JSONObject db2 = new Screen(db1).serialize(); // Deserialize and Reserialize through JSON
		//System.out.println(db1);
		//System.out.println(db2);
		return db1.toString().equals(db2.toString());
	}
	
	@TestCase
	boolean viewJSONViewTest(final PrintWriter out) {
		JSONObject db1 = testDB.viewList().get(0).serialize();
		JSONObject db2 = new View(db1).serialize(); // Deserialize and Reserialize through JSON
		System.out.println(db1);
		System.out.println(db2);
		return db1.toString().equals(db2.toString());
	}
	
	@TestCase
	boolean contentJSONViewTest(final PrintWriter out) {
		JSONObject db1 = testDB.contentList().get(0).serialize();
		JSONObject db2 = new Content(db1).serialize(); // Deserialize and Reserialize through JSON
		//System.out.println(db1);
		//System.out.println(db2);
		return db1.toString().equals(db2.toString());
	}
	
	@TestCase
	boolean fullJSONSerialTest(final PrintWriter out) {
		JSONObject db1 = testDB.serialize();
		JSONObject db2 = new CompleteDatabasePipeline(db1).serialize(); // Deserialize and Reserialize through JSON
		//System.out.println(db1);
		//System.out.println(db2);
		return db1.toString().equals(db2.toString());
	}
	
	@TestCase
	boolean fullJavaSerialTest(final PrintWriter out) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(testDB);
		oos.close();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		CompleteDatabasePipeline deser = (CompleteDatabasePipeline)ois.readObject();
		
		System.out.println(testDB.serialize());
		System.out.println(deser.serialize());
		
		JSONObject db1 = testDB.serialize();
		JSONObject db2 = deser.serialize(); // Deserialize and Reserialize through JSON
		//System.out.println(db1);
		//System.out.println(db2);
		return db1.toString().equals(db2.toString());
	}
	
}
