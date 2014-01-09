package upmc.stl.aar.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.Player;

public class DailyCredit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Dao dao = Dao.INSTANCE;

		// Si une personne connectée essaye de lancer le cron, cela redirige
		// vers une page d'erreur
		dao.creditUsers();
	}
}
