package edu.letu.lvkms.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import edu.letu.lvkms.db.FlatJSON;
import edu.letu.lvkms.structure.CompleteDatabasePipeline;
import edu.letu.lvkms.structure.Content;
import edu.letu.lvkms.structure.LoadableContent;
import edu.letu.lvkms.structure.Screen;
import edu.letu.lvkms.structure.View;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class App extends NanoHTTPD {
	
	private static final String JSON = "application/json";
	private static final String PLAINTEXT = "text/plain";
	
	//private UserList userList = new UserList();
	private FlatJSON flatDB = new FlatJSON();
	CompleteDatabasePipeline testDB = new CompleteDatabasePipeline();
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
		
		System.out.println(slidesDoc.getUsers(testDB.viewList()));
	}
	
	public App() throws IOException {
		super(8080);
		setupTest();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("\nRunning! Point your browser to http://127.0.0.1:8080/ \n");

	}

	public static void main(String[] args) {
		try {
			new App();
		} catch (IOException ioe) {
			System.err.println("Couldn't start server:\n" + ioe);
		}
	}

	@Override
	public Response serve(IHTTPSession session) {
		Map<String, List<String>> parms = session.getParameters();
		System.out.println(session.getUri());
		switch(session.getUri()) {
		case "/testDatabase": return newFixedLengthResponse(Status.OK, JSON, testDB.serialize().toString(3));
		case "/getDatabase": return newFixedLengthResponse(Status.OK, JSON, flatDB.data().get());
		case "/setDatabase": {
			String response = flatDB.accessor().access((db) -> {
				if (!parms.containsKey("db")) {
					return "The 'db' parameter must be set";
				}
				db.setFrom(
						new CompleteDatabasePipeline(
								new JSONObject(
										parms.get("db").get(0)
										)));
				return "Success";
			});
			return newFixedLengthResponse(Status.OK, PLAINTEXT, response);
		}
		default: {
			URL res = this.getClass().getResource("/WebContent"+session.getUri());
			if (res != null) {
				try {
					if (session.getUri().contains(".")) {
						InputStream is = res.openStream();
						return newChunkedResponse(Status.OK, mimeTypes().get(session.getUri().substring(session.getUri().lastIndexOf('.'))), is);
					} else {
						
						return get404();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					return get404();
				}
			} else {
				return get404();
			}
		}
		}
		
	}

	public Response get404() {
		return newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT, "Page not found.");
	}
}