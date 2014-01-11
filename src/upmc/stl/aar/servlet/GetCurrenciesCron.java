package upmc.stl.aar.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;

/**
 * Servlet used by scheduled cron task to update currency values. 
 * Contains API key. Is protected from intrusive access by authorization constraint in web.xml
 */
public class GetCurrenciesCron extends HttpServlet {
	
	private final static Logger logger = Logger.getLogger(GetCurrenciesCron.class.getName());
	
	private final String url = "http://openexchangerates.org/api/latest.json?app_id=7b689b62ead6401a81006d181eb87730";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		logger.info("GetCurrencies Cron");
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
	 
		int responseCode = con.getResponseCode();
		logger.info("\nSending 'GET' request to URL : " + url);
		logger.info("Response Code : " + responseCode);
	 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
			
		return response.toString(); 
	}
}
