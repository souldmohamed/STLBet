package upmc.stl.aar.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;

public class GetCurrenciesCron extends HttpServlet {
	
	private final String url = "http://openexchangerates.org/api/latest.json?app_id=7b689b62ead6401a81006d181eb87730";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		System.out.println("GetCurrencies Cron");
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		try {
			Dao.INSTANCE.saveCurrencyRates(sendGet());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	// HTTP GET request
	private String sendGet() throws Exception {
	 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
		// optional default is GET
		con.setRequestMethod("GET");
	 
		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);
	 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
	 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	 
		//print result
		System.out.println(response.toString());
			
		return response.toString(); 
	}
}
