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
public class CreateBet extends HttpServlet {
	
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  
    System.out.println("Creating new bet ");
    User user = (User) req.getAttribute("user");
    if (user == null) {
      UserService userService = UserServiceFactory.getUserService();
      user = userService.getCurrentUser();
    }
    
    String type = checkNull(req.getParameter("type"));
    String quantity = checkNull(req.getParameter("quantity"));
    String rate = checkNull(req.getParameter("rate"));
    String currency = checkNull(req.getParameter("currency"));
    String term = checkNull(req.getParameter("term"));
    
    Dao.INSTANCE.addBet(user.getUserId(), type, quantity, rate,currency,term);
   
    resp.sendRedirect("/pages/STLBetApplication.jsp");
    
  }

  private String checkNull(String s) {
    if (s == null) {
      return "";
    }
    return s;
  }
  
} 