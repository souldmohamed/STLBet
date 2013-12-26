package upmc.stl.aar.servlet;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.Player;

public class DailyCredit extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	{
		Dao dao = Dao.INSTANCE;
		
		List<Player> pList = dao.getPlayers();
		
		for (Player p : pList)
		{
			p.setEligible(true);
		}
	}
}
