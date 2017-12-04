package edu.letu.lhkms.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;

import org.json.JSONObject;

import edu.letu.lhkms.HTTPUtil;
import edu.letu.lhkms.db.FlatJSON;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import edu.letu.lhkms.structure.LoadableContent;
import edu.letu.lhkms.structure.Screen;
import edu.letu.lhkms.structure.StatusBar;
import edu.letu.lhkms.structure.View;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class App extends NanoHTTPD {
	private static final boolean DEBUG = false;
	
	private static final String JSON = "application/json";
	private static final String PLAINTEXT = "text/plain";
	private static final String HTML = "text/html";
	
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
		
		testView1.getStatusBar().getStocks().addAll(Arrays.asList(
				"NVDA", "AMD", "AAPL", "INTC",
				"GOOGL", "AMZN", "FB", "TWTR",
				"MSFT", "GE", "NFLX", "TSLA", 
				"VEZ", "DIS", "WMP", "T",
				"SBUX", "XOM"
				));
		
		
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
		case "/screen": try {
				return newFixedLengthResponse(Status.OK, HTML, getScreen(
						parms.get("uuid") == null ? null : parms.get("uuid").get(0),
						parms.get("name") == null ? null : parms.get("name").get(0)));
			} catch (Exception e) {
				e.printStackTrace();
				return newFixedLengthResponse(Status.OK, PLAINTEXT, "Unhandled exception loading screen");
			}
			
		case "/stock": try {
				if (parms.containsKey("sym")) {
					return newFixedLengthResponse(Status.OK, PLAINTEXT, RobinhoodAPI.getStockTable(
							parms.get("sym").get(0)));
					
				} else {
					return newFixedLengthResponse(Status.OK, PLAINTEXT, "");
				}
			} catch (Exception e1) {
				return newFixedLengthResponse(Status.OK, PLAINTEXT, "");
			}
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
					if (DEBUG) System.out.println(session.getHeaders());
					json = HTTPUtil.readFixedLengthStream(session.getInputStream(), StandardCharsets.UTF_8, Integer.parseInt(session.getHeaders().get("content-length")));
					if (DEBUG) System.out.println(json);
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

	private String getScreen(String uuid, String name) throws IOException, URISyntaxException {
		if (uuid == null && name == null) return "A screen uuid or name parameter must be supplied";
		CompleteDatabasePipeline db = new CompleteDatabasePipeline(flatDB);

		Screen screen;
		if (uuid != null) screen = findByUUID(uuid, db.screenList(), (s)->{return s.getScreenID();});
		else screen = findByString(name, db.screenList(), (s)->{return s.getName();});
		
		if (screen != null) {
			View v = findByUUID(screen.getViewID(), db.viewList(), (view)->{return view.getViewID();});
			if (v != null) {
				String template = read("screen_code/screen_template.html");

				StatusBar sb = v.getStatusBar();
				if (sb.hasStocks()) {
					String stock_ajax_js = read("screen_code/stock_ajax.js");
					String stock_tablecell_html = read("screen_code/stock_tablecell.html");
					
					StringBuilder stock_ajax = new StringBuilder();
					StringBuilder stock_table = new StringBuilder();
					
					int i = 0;
					for (String sym : sb.getStocks()) {
						stock_ajax.append(
								replaceNode("sym", sym, 
								replaceNode("i", i+"", stock_ajax_js))
								);
						stock_table.append(
								replaceNode("i", i+"", stock_tablecell_html)
								);
						i++;
					}
					template = replaceNode("list stock_ajax.js", stock_ajax.toString(), template);
					template = replaceNode("list stock_tablecell.html", stock_table.toString(), template);
				}
				return template;
			} else {
				return "The view referenced by this screen does not exist";
			}
		} else {
			return "The specified screen could not be found";
		}
	}
	
	private String replaceNode(String nodeName, String replacement, String src) {
		return src.replaceAll("\\{"+nodeName+"\\}", Matcher.quoteReplacement(replacement));
	}
	
	private <T> T findByUUID(UUID uuid, Collection<T> list, Function<T, UUID> idFunc) {
		for (T s : list) {
			if (idFunc.apply(s).equals(uuid)) {
				return s;
			}
		}
		return null;
	}
	
	private <T> T findByUUID(String uuid, Collection<T> list, Function<T, UUID> idFunc) {
		for (T s : list) {
			if (idFunc.apply(s).toString().equals(uuid)) {
				return s;
			}
		}
		return null;
	}

	private <T> T findByString(String str, Collection<T> list, Function<T, String> strFunc) {
		for (T s : list) {
			if (strFunc.apply(s).equals(str)) {
				return s;
			}
		}
		return null;
	}
	
	private String read(String filename) throws IOException, URISyntaxException {
		return new String(Files.readAllBytes(Paths.get(
				App.class.getResource("/WebContent/"+filename).toURI())));
	}

	public Response get404() {
		return newFixedLengthResponse(Status.NOT_FOUND, MIME_PLAINTEXT, "Page not found.");
	}
}