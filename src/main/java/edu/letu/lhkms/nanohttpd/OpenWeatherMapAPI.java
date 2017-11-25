package edu.letu.lhkms.nanohttpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class OpenWeatherMapAPI {
	static final String URL = "http://api.openweathermap.org/data/2.5/forecast?q=Longview,US&appid=d2b6ac842005c6f8ed294b31eba32aa5";
	static HttpClient client = HttpClients.createDefault();
	
	public static String getWeatherTable() throws ClientProtocolException, IOException, URISyntaxException {
		BufferedReader reader = get(URL);
		JSONObject quote = new JSONObject(reader.readLine());
		reader.close();
		
		return quote.toString(3);
		
		/*String image = up ? "icons/StockUP.png" : "icons/StockDOWN.png";
		
		String table = new String(Files.readAllBytes(Paths.get(
				OpenWeatherMapAPI.class.getResource("/WebContent/stockTable.html").toURI())))
				.replaceAll("\\{SYM\\}", symbol)
				.replaceAll("\\{IMG\\}", image)
				.replaceAll("\\{PRICE\\}", df.format(price))
				.replaceAll("\\{DPRICE\\}", df.format(Math.abs(changePrice)))
				.replaceAll("\\{DPERCENT\\}", percent)
				.replaceAll("\\{RANDOM\\}", ""+(""+Math.random()).hashCode())
				;
		
		return table;*/
	}
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		
		System.out.println(getWeatherTable());
	}
	
	
	public static BufferedReader get(String url) throws ClientProtocolException, IOException {

		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		
		return new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	}
	
}
