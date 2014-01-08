package upmc.stl.aar.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.model.Player;
import upmc.stl.aar.model.ProductBet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class Login extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
		UserService userService = UserServiceFactory.getUserService();
		
		if (req.getUserPrincipal() != null) {
			ServletContext ctx = req.getSession().getServletContext();
			User user = userService.getCurrentUser();
			
			Dao dao = Dao.INSTANCE;
			
			List<ProductBet> bets = dao.getBets(user.getUserId());
			List<ProductBet> historybets = dao.getHistoryBets(user.getUserId());
			Player player = dao.getPlayer(user.getUserId(), user.getEmail());
			System.out.println("balance :"+player.getBalance());
			ctx.setAttribute("Cbets", bets);
			ctx.setAttribute("Hbets", historybets);
			ctx.setAttribute("Player", player);
			
			ctx.setAttribute("Logout", userService.createLogoutURL(req.getRequestURI()));
			System.out.println("before redirect");
			resp.sendRedirect("pages/STLBetApplication.jsp");
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
}
