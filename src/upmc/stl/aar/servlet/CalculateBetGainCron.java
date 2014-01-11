package upmc.stl.aar.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import upmc.stl.aar.dao.Dao;

/**
 * Servlet used by scheduled cron task to calculate gains
 */
@SuppressWarnings("serial")
public class CalculateBetGainCron extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Dao.INSTANCE.calculateGain();
	}

}