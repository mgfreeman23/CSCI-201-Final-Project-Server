package maiawein_CSCI201_Final;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LogOutServlet")
public class LogOutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		
		// Invalidate the session to log out the user.
		if (session != null)
		{
			session.invalidate();
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
}