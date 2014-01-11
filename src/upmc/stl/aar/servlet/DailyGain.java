package upmc.stl.aar.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import upmc.stl.aar.dao.Dao;

/**
 * Servlet called to credit user with daily amount of USD. Second check iterated in addDailyGain method for security
 */
public class DailyGain extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Dao dao = Dao.INSTANCE;
		
		UserService userService = UserServiceFactory.getUserService();
		
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			dao.addDailyGain(user.getUserId());
		}
		
		resp.sendRedirect("login");
	}

}
