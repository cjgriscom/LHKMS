package edu.letu.lvkms.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import edu.letu.lvkms.db.FlatJSON;
import edu.letu.lvkms.structure.CompleteDatabasePipeline;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class App extends NanoHTTPD {

	//private UserList userList = new UserList();
	private FlatJSON flatDB = new FlatJSON();
	
	public App() throws IOException {
		super(8080);
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
		case "/test": return newFixedLengthResponse("test success");
		case "/getDatabase": return newFixedLengthResponse(flatDB.data().get());
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
			return newFixedLengthResponse(response);
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