package upmc.stl.aar.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import upmc.stl.aar.dao.Dao;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class CalculateBetGainCron extends HttpServlet {
	
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException { 
	  System.out.println("Calculate bet gain");
//	  User user = (User) req.getAttribute("user");
//	  if (user == null) {
//		  UserService userService = UserServiceFactory.getUserService();
//		  user = userService.getCurrentUser();
//	  }    
	  Dao.INSTANCE.calculateGain();
  }
  
} 