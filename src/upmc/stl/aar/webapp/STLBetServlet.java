package upmc.stl.aar.webapp;
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class STLBetServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		//test
		resp.sendRedirect("/pages/login.jsp");
		
	}
}
