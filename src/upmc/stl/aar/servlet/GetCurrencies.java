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

public class GetCurrencies extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		try {
			out.print(Dao.INSTANCE.getCurrencyRates().getRates().getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.flush();
		out.close();
	}
	
}
