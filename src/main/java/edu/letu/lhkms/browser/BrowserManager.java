package edu.letu.lhkms.browser;

import java.io.IOException;

public class BrowserManager {
	public static void main(String[] args) throws IOException {
		// Windows
		Runtime.getRuntime().exec(new String[]{"cmd", "/c",
				"start chrome --incognito --kiosk http://localhost:8080/test.html"});
		
		/*// Linux
		Runtime.getRuntime().exec(new String[]{"bash", "-c",
				"chromium --incognito --kiosk http://localhost:8080/test.html"});
		*/
	}
}
