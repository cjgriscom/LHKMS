package edu.letu.lhkms.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * JSON Format:
 * 
 * {
 * "weather": boolean
 * "time": boolean
 * "marquee": boolean
 * "marqueeText": string         (optional)
 * "stocks": boolean
 * "stockList": [string, ...]      (optional)
 * 
 * }
 *
 */
public class StatusBar implements Serializable, JSONSerializable {
	
	private static final long serialVersionUID = 3700830180680986009L;
	
	private final ArrayList<String> stocks = new ArrayList<>();
	private transient final InteractiveList<String> stocksMod = new InteractiveList<>(stocks);
	
	private boolean weather;
	private boolean time;
	
	private String marqueeText = null;
	
	public StatusBar() {}
	
	public StatusBar(JSONObject ser) {
		this.weather = ser.getBoolean("weather");
		this.time = ser.getBoolean("time");
		if (ser.getBoolean("marquee")) this.marqueeText = ser.getString("marqueeText");
		if (ser.getBoolean("stocks")) {
			ser.getJSONArray("stockList").forEach((o) -> {
				this.stocks.add(o.toString());
			});
		}
	}
	
	public void setWeather(boolean weather) {
		this.weather = weather;
	}
	
	public boolean hasWeather() {
		return weather;
	}
	
	public void setTime(boolean time) {
		this.time = time;
	}
	
	public boolean hasTime() {
		return time;
	}
	
	public boolean hasStocks() {
		return !stocks.isEmpty();
	}
	
	public List<String> getStocks() {
		return stocks;
	}
	
	public InteractiveList<String> getStocksModifier() {
		return stocksMod;
	}
	
	public void setMarquee(String text) {
		this.marqueeText = text;
	}
	
	public void removeMarquee() {
		this.marqueeText = null;
	}
	
	public boolean hasMarquee() {
		return this.marqueeText != null;
	}
	
	public String getMarqueeText() {
		return marqueeText;
	}

	@Override
	public JSONObject serialize() {
		JSONObject ser = new JSONObject();
		ser.put("weather", hasWeather());
		ser.put("time", hasTime());
		ser.put("marquee", hasMarquee());
		ser.put("stocks", hasStocks());
		if (hasMarquee()) ser.put("marqueeText", getMarqueeText());
		if (hasStocks()) ser.put("stockList", stocks);
		
		return ser;
	}
}
