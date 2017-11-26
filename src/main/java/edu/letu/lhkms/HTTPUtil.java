package edu.letu.lhkms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// Modified from https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

public class HTTPUtil {
	
	private static boolean DEBUG = false;
	
	public static String sanitizeParameter(String parm) {
		try {
			return URLEncoder.encode(parm, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	// HTTP GET request
	public static String sendGet(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		con.setUseCaches( false );

		int responseCode = con.getResponseCode();
		if (DEBUG) System.out.println("\nSending 'GET' request to URL : " + url);
		if (DEBUG) System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}

	// HTTP POST request
	public static String sendPost(String url, String json) throws IOException {

		
		URL obj = new URL(url);

		byte[] postData       = json.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		HttpURLConnection con= (HttpURLConnection) obj.openConnection();           
		con.setDoOutput( true );
		con.setInstanceFollowRedirects( false );
		con.setFixedLengthStreamingMode(postDataLength);
		con.setRequestMethod( "POST" );
		con.setRequestProperty( "Content-Type", "application/json"); 
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		con.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
			wr.write( postData );
			wr.flush();
		}

		int responseCode = con.getResponseCode();
		if (DEBUG) System.out.println("\nSending 'POST' request to URL : " + url);
		if (DEBUG) System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}
	
	// Adapted from https://community.oracle.com/blogs/pat/2004/10/23/stupid-scanner-tricks
	public static String convertStreamToString(InputStream is, String charset) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer text = new StringBuffer();
		for (String line; (line = br.readLine()) != null;) {
			//System.out.println(line);
			text.append(line);
		}
		br.close();
		return text.toString();
	}

	public static String readFixedLengthStream(InputStream inputStream, Charset charset, int length) throws IOException {
		int off = 0;
		byte[] buffer = new byte[length];
		for (int read = inputStream.read(buffer, off, buffer.length - off); read > -1 && off < buffer.length; off += read);
		//inputStream.close();
		return new String(buffer, charset);
	}

	public static String getStackDump(String header, Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ps.println(header);
		e.printStackTrace(ps);
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}
	
}