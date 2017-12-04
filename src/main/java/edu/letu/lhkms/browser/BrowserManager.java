package edu.letu.lhkms.browser;

import java.io.IOException;

public class BrowserManager {
	public static void launchBrowser() throws IOException, InterruptedException {
		// Windows
		//Runtime.getRuntime().exec(new String[]{"cmd", "/c",
		//		"start chrome --incognito --kiosk http://localhost:8080/test.html"}).waitFor();
		
		// Try this if the other don't work
		/*
		Runtime.getRuntime().exec(new String[]{
				"start", "chrome --incognito --kiosk http://localhost:8080/test.html"}).waitFor();
		
		 */
		
		// Linux
		Runtime.getRuntime().exec(new String[]{"bash", "-c",
				"chromium --incognito --kiosk http://localhost:8080/test.html"});
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		launchBrowser();
	}
}
