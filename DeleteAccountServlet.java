package maiawein_CSCI201_Final;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String URL = "jdbc:mysql://localhost:3306/database";
	private static final String USER = "root";
	private static final String PASSWORD = ""; // Replace with SQL database password

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().write("DeleteAccountServlet is running.");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("userID");


		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD))
		{
			String deleteAccountQuery = "DELETE FROM users WHERE user_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(deleteAccountQuery))
			{
				ps.setInt(1, userId);
				int rowsAffected = ps.executeUpdate();

				// Invalidate session after account deletion.
				if (rowsAffected > 0)
				{
					session.invalidate();
					response.getWriter().write(new Gson().toJson(new Settings.StatusMessage(true, null)));
				}
				else
				{
					response.getWriter().write(new Gson().toJson(new Settings.StatusMessage(false, "Account deletion failed.")));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.getWriter().write(new Gson().toJson(new Settings.StatusMessage(false, "An error occurred.")));
		}
	}
}
