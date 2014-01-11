package upmc.stl.aar.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;

/**
 * Servlet called to retrieve currency rates from the database.
 * Called by ajax method
 */
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
