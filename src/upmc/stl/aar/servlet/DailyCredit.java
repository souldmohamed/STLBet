package upmc.stl.aar.servlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;

/**
 * Servlet called by scheduled cron task to render players eligible
 */
public class DailyCredit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Dao dao = Dao.INSTANCE;
		dao.creditUsers();
	}
}
