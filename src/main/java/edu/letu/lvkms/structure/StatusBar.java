package edu.letu.lvkms.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusBar implements Serializable {
	
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
	
	public boolean getTime() {
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
}
