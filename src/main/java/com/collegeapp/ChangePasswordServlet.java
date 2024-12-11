package com.collegeapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("ChangePasswordServlet is running.");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		try
		{
			JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
			String newPassword = data.get("newPassword").getAsString();
			
			HttpSession session = request.getSession(false);
			int userId = (int) session.getAttribute("userID");

			Connection conn = DatabaseConnector.getConnection();
			
			// Update the user's password in the database
			String updatePasswordQuery = "UPDATE users SET password = ? WHERE user_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(updatePasswordQuery))
			{
				ps.setString(1, newPassword);
				ps.setInt(2, userId);

				int rowsAffected = ps.executeUpdate();
;
				if (rowsAffected > 0)
				{
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(new Gson().toJson("Password updated successfully."));
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write(new Gson().toJson("Failed to update password."));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(new Gson().toJson("An error occurred."));
		}
	}
}
