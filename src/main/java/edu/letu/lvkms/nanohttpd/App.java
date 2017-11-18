package edu.letu.lvkms.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import edu.letu.lhkms.test.HTTPUtil;
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
	
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	private final ScreenManager sm = new ScreenManager();
	
	//private UserList userList = new UserList();
	private FlatJSON flatDB;
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
	}
	
	public App() throws IOException {
		super(8080);
		setupTest();
		flatDB = new FlatJSON(); // Init flat database
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		running.set(true);
		System.out.println("\nRunning! Point your browser to http://127.0.0.1:8080/ \n");

	}

	@Override
	public void stop() {
		System.out.println("Stopping server...");
		running.set(false);
		flatDB.close();
		super.stop();
	}
	
	public static void main(String[] args) {
		Set<App> app = new HashSet<>();
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			System.out.println("Caught shutdown hook");
			app.forEach((a)->{
				if(a.running.getAndSet(false)) {
					a.stop();
				}
			});
		}));
		try {
			app.add(new App());
		} catch (IOException ioe) {
			System.err.println("Couldn't start server:\n" + ioe);
		}
	}

	@Override
	public Response serve(IHTTPSession session) {
		Map<String, List<String>> parms = session.getParameters();
		
		System.out.println(session.getUri());
		switch(session.getUri()) {
		case "/msSinceLastUpdate": return newFixedLengthResponse(Status.OK, PLAINTEXT, ""+sm.lastUpdateDifferential());
		case "/testDatabase": return newFixedLengthResponse(Status.OK, JSON, testDB.serialize().toString(3));
		case "/getDatabase": return newFixedLengthResponse(Status.OK, JSON, flatDB.data().get());
		case "/setDatabase": {
			return flatDB.accessor().access((db) -> {
				String json;
				try {
					if (!session.getHeaders().containsKey("content-length")) {
						return newFixedLengthResponse(Status.OK, PLAINTEXT, 
								"Error: content-length not specified");
					}
					System.out.println(session.getHeaders());
					json = HTTPUtil.readFixedLengthStream(session.getInputStream(), StandardCharsets.UTF_8, Integer.parseInt(session.getHeaders().get("content-length")));
					System.out.println(json);
					try {
						db.setFrom(new CompleteDatabasePipeline(new JSONObject(json)));
						sm.registerDatabaseUpdate();
					} catch (Exception e) {
						return newFixedLengthResponse(Status.OK, PLAINTEXT, 
								HTTPUtil.getStackDump("Error: The database interpreter failed with the following message: ", e));
					}
					return newFixedLengthResponse(Status.OK, PLAINTEXT, "Success");
				} catch (IOException e) {
					e.printStackTrace();
					return newFixedLengthResponse(Status.OK, PLAINTEXT, HTTPUtil.getStackDump("Error: Exception during connection: ", e));
				}
			});
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