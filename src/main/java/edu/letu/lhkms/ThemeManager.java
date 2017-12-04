package edu.letu.lhkms;

import org.json.JSONObject;

public class ThemeManager {
	public static final JSONObject themeList = getThemeList();
	private static JSONObject getThemeList() {
		try {
			String dat = Util.read("themeList.json");
			return new JSONObject(dat);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load themes JSON", e);
		}
		
	}
}
