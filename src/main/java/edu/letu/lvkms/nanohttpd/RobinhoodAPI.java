package edu.letu.lvkms.nanohttpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class RobinhoodAPI {
	static final String URL = "https://api.robinhood.com";
	static final String QUOTES = "/quotes";
	static HttpClient client = HttpClients.createDefault();
	
	public static String getStockTable(String symbol) throws ClientProtocolException, IOException, URISyntaxException {
		BufferedReader reader = get(symbolQuote(symbol));
		JSONObject quote = new JSONObject(reader.readLine());
		reader.close();
		
		double price = quote.getDouble("last_trade_price");
		double prev = quote.getDouble("previous_close");
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		double changePrice = price-prev;
		double priceDouble = (price - prev)/prev*100.;
		boolean up = priceDouble >= 0;
		String percent = df.format(Math.abs(priceDouble)) + "%";
		
		String image = up ? "icons/StockUP.png" : "icons/StockDOWN.png";
		
		String table = new String(Files.readAllBytes(Paths.get(
				RobinhoodAPI.class.getResource("/WebContent/stockTable.html").toURI())))
				.replaceAll("\\{SYM\\}", symbol)
				.replaceAll("\\{IMG\\}", image)
				.replaceAll("\\{PRICE\\}", df.format(price))
				.replaceAll("\\{DPRICE\\}", df.format(Math.abs(changePrice)))
				.replaceAll("\\{DPERCENT\\}", percent)
				.replaceAll("\\{RANDOM\\}", (""+Math.random()).hashCode())
				;
		
		return table;
	}
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		
		System.out.println(getStockTable("NVDA"));
	}
	
	private static LocalDateTime conv(Date src) {
		return LocalDateTime.ofInstant(src.toInstant(), ZoneId.of("GMT"));
	}
	
	//interval=week|day|10minute|5minute|null (all)
	//span=day|week|year|5year|all
	//bounds=extended|regular|trading
	
	public static String symbolQuote(String symbol) {
		return URL + "/quotes/" + symbol + 
				"/";
	}
	
	public static BufferedReader get(String url) throws ClientProtocolException, IOException {

		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		
		return new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	}
	
}
