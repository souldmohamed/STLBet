package upmc.stl.aar.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





//import sun.util.calendar.BaseCalendar.Date;
import upmc.stl.aar.dao.Dao;
import upmc.stl.aar.exceptions.InsufficientBalanceException;
import upmc.stl.aar.utils.RatesParser;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Servlet called to create a bet
 */
@SuppressWarnings("serial")
public class CreateBet extends HttpServlet {
	
	private final static Logger logger = Logger.getLogger(CreateBet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			logger.info("Creating new bet ");
			User user = (User) req.getAttribute("user");
			if (user == null) {
				UserService userService = UserServiceFactory.getUserService();
				user = userService.getCurrentUser();
			}

			String type = URLEncoder.encode(
					checkNull(req.getParameter("type")), "UTF-8");
			String qty = URLEncoder.encode(
					checkNull(req.getParameter("quantity")), "UTF-8");
			String rate = URLEncoder.encode(
					checkNull(req.getParameter("rate")), "UTF-8");
			String currency = URLEncoder.encode(
					checkNull(req.getParameter("currency")), "UTF-8");
			String term = URLEncoder.encode(
					checkNull(req.getParameter("term")), "UTF-8");
			String balance = URLEncoder.encode(
					checkNull(req.getParameter("balance")), "UTF-8");
			Date betDate = new Date();
			Date termDate = null;

			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(betDate); // sets calendar time/date
			logger.info(term);

			// Check on quantity parameter
			int cquantity = Integer.valueOf(qty);
			logger.info("QTY : " + Float.valueOf(qty));
			// Check on rate parameter
			String s = rate;
			if (rate.length() > 7) {
				s = String.copyValueOf(rate.toCharArray(), 0, 7);
			}
			Float crate = Float.valueOf(s);
			logger.info("crate : " + Float.valueOf(s));
			
			String tc = RatesParser.getCurrencyRate(currency);
			Float trate=null;
			if(tc!=null){
				trate = Float.valueOf(tc);
			    logger.info("trate : " + trate);
			}
			
			// Calculating term date
			if (term.equals("1+h")) {
				cal.add(Calendar.HOUR_OF_DAY, 1);
				termDate = cal.getTime();
			} else if (term.equals("3+h")) {
				cal.add(Calendar.HOUR_OF_DAY, 3);
				termDate = cal.getTime();
			} else if (term.equals("6+h")) {
				cal.add(Calendar.HOUR_OF_DAY, 6);
				termDate = cal.getTime();
			} else if (term.equals("12+h")) {
				cal.add(Calendar.HOUR_OF_DAY, 12);
				termDate = cal.getTime();
			} else if (term.equals("1+day")) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				termDate = cal.getTime();
			} else if (term.equals("2+day")) {
				cal.add(Calendar.DAY_OF_MONTH, 2);
				termDate = cal.getTime();
			} else if (term.equals("3+day")) {
				cal.add(Calendar.DAY_OF_MONTH, 3);
				termDate = cal.getTime();
			}

			// Check on balance
			Float bal = Float.valueOf(balance);
			if (bal < (cquantity / Math.abs(crate - trate)))
			{
				throw new InsufficientBalanceException();
			}
			
			Dao.INSTANCE.addBet(user.getUserId(), user.getEmail(), type, trate,  
					cquantity, crate, currency, betDate, term, termDate);

			resp.sendRedirect("login");
		} catch (NumberFormatException e) {
			logger.info("NUMBERFORMATEXCEPTION");
			req.setAttribute("Err", 2);
			try {
				req.getRequestDispatcher("login").forward(req, resp);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (InsufficientBalanceException e) {
			logger.info("SET1");
			req.setAttribute("Err", 1);
			try {
				logger.info("LOGIN");
				req.getRequestDispatcher("login").forward(req, resp);
			} catch (ServletException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

}