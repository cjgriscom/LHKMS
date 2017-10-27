package edu.letu.lvkms.nanohttpd;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import edu.letu.lvkms.db.UserList;
import edu.letu.lvkms.db.ViewList;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class App extends NanoHTTPD {

	private UserList userList = new UserList();
	private ViewList viewList = new ViewList();
	
	public App() throws IOException {
		super(8080);
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");

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
		default: {
			URL res = this.getClass().getResource("/WebContent"+session.getUri());
			if (res != null) {
				try {
					InputStream is = res.openStream();
					return newChunkedResponse(Status.OK, MIME_TYPES.get(session.getUri().substring(session.getUri().lastIndexOf('.'))), is);
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