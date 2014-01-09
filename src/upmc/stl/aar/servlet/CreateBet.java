package upmc.stl.aar.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




//import sun.util.calendar.BaseCalendar.Date;
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
    
    String type = URLEncoder.encode(checkNull(req.getParameter("type")), "UTF-8");
    String quantity = URLEncoder.encode(checkNull(req.getParameter("quantity")), "UTF-8");
    String rate = URLEncoder.encode(checkNull(req.getParameter("rate")), "UTF-8");
    String currency = URLEncoder.encode(checkNull(req.getParameter("currency")), "UTF-8");
    String term = URLEncoder.encode(checkNull(req.getParameter("term")), "UTF-8");
    Date betDate = new Date();
    Date termDate = null;
    
    Calendar cal = Calendar.getInstance(); // creates calendar
    cal.setTime(betDate); // sets calendar time/date
    
    	
    if(term.equals("1 h")){
    	cal.add(Calendar.HOUR_OF_DAY, 1); 
    	termDate = cal.getTime();
    }else if(term.equals("3 h")){
    	cal.add(Calendar.HOUR_OF_DAY, 3); 
    	termDate = cal.getTime();	
    }else if(term.equals("6 h")){
    	cal.add(Calendar.HOUR_OF_DAY, 6); 
    	termDate = cal.getTime();
    }else if(term.equals("12 h")){
    	cal.add(Calendar.HOUR_OF_DAY, 12); 
    	termDate = cal.getTime();	
    }else if(term.equals("1 day")){
    	cal.add(Calendar.DAY_OF_MONTH, 1); 
    	termDate = cal.getTime();
    }else if(term.equals("2 day")){
    	cal.add(Calendar.DAY_OF_MONTH, 2); 
    	termDate = cal.getTime();
    }else if(term.equals("3 day")){
    	cal.add(Calendar.DAY_OF_MONTH, 3); 
    	termDate = cal.getTime();
    }
    Dao.INSTANCE.addBet(user.getUserId(), user.getEmail(), type, quantity, rate,currency,betDate,term,termDate);
   
    resp.sendRedirect("login");
    
  }

  private String checkNull(String s) {
    if (s == null) {
      return "";
    }
    return s;
  }
  
} 