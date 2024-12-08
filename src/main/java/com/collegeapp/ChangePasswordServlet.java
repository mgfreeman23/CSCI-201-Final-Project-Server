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

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String URL = "jdbc:mysql://localhost:3306/database";
	private static final String USER = "root";
	private static final String PASSWORD = ""; // Replace with SQL database password

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().write("ChangePasswordServlet is running.");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		Settings.ChangePasswordMessage message = new Gson().fromJson(request.getReader(), Settings.ChangePasswordMessage.class);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD))
		{
			HttpSession session = request.getSession(false);
			int userId = (int) session.getAttribute("userID");

			// Update the user's password in the database
			String updatePasswordQuery = "UPDATE users SET password = ? WHERE user_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(updatePasswordQuery))
			{
				ps.setString(1, message.newPassword);
				ps.setInt(2, userId);

				int rowsAffected = ps.executeUpdate();

				if (rowsAffected > 0)
				{
					response.getWriter().write(new Gson().toJson(new Settings.StatusMessage(true, null)));
				}
				else
				{
					response.getWriter().write(new Gson().toJson(new Settings.StatusMessage(false, "Failed to update password.")));
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
