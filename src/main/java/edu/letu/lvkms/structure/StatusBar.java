package edu.letu.lvkms.structure;

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
	
	private final ArrayList<String> stocks;
	
	private boolean weather;
	private boolean time;
	
	private String marqueeText = null;
	
	public StatusBar() {
		this.stocks = new ArrayList<>();
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
	
	public void setMarquee(String text) {
		this.marqueeText = text;
	}
	
	public void removeMarquee() {
		this.marqueeText = null;
	}
	
	public boolean hasMarquee() {
		return this.marqueeText == null;
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
