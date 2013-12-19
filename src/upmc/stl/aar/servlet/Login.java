package upmc.stl.aar.servlet;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Login extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		String url = userService.createLoginURL(req.getRequestURI());
		if (req.getUserPrincipal() != null) { 
			resp.sendRedirect("/pages/STLBetApplication.jsp");
		}else{ 
			resp.sendRedirect(url);
		    
		} 
	}
	
}
