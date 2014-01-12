package upmc.stl.aar.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.util.logging.Logger;

import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Login servlet. Redirect to application page
 */
@SuppressWarnings("serial")
public class Login extends HttpServlet {

	private final static Logger logger = Logger
			.getLogger(Login.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{

		UserService userService = UserServiceFactory.getUserService();

		if (req.getUserPrincipal() != null) {
			ServletContext ctx = req.getSession().getServletContext();
			User user = userService.getCurrentUser();

			Dao dao = Dao.INSTANCE;

			logger.info("login  userId:" + user.getUserId());

			List<ProductBet> bets = dao.getBets(user.getUserId());
			List<ProductBet> historybets = dao.getHistoryBets(user.getUserId());
			Player player = dao.getPlayer(user.getUserId(), user.getEmail());
			List<Float> gains = fillScores(dao.getBestGainScores());
			List<Float> losses = fillScores(dao.getBestLossScores());

			ctx.setAttribute("Cbets", bets);
			ctx.setAttribute("Hbets", historybets);
			ctx.setAttribute("Player", player);
			ctx.setAttribute("Logout",
					userService.createLogoutURL(req.getRequestURI()));
			ctx.setAttribute("Gains", gains);
			ctx.setAttribute("Losses", losses);
			logger.info("ERR" + req.getAttribute("Err"));
			if (req.getAttribute("Err") == null) {
				ctx.setAttribute("Err", 0);
				resp.sendRedirect("pages/STLBetApplication.jsp");
			} else {
				ctx.setAttribute("Err", req.getAttribute("Err"));
				resp.sendRedirect("pages/STLBetApplication.jsp");
			}

		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
	
	public List<Float> fillScores(List<Float> scoreList)
	{
		if (scoreList.size() < 5)
		{
			for (int i = scoreList.size(); i < 5; i++)
			{
				scoreList.add(i, new Float(0));
			}
		}
		return scoreList;
	}
}
