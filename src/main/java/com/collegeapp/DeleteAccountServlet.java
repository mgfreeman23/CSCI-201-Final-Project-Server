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

@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("DeleteAccountServlet is running.");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");

		try
		{
			HttpSession session = request.getSession(false);
			int userId = (int) session.getAttribute("userID");
			
			Connection conn = DatabaseConnector.getConnection();
			
			// Delete user account.
			String deleteAccountQuery = "DELETE FROM users WHERE user_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(deleteAccountQuery))
			{
				ps.setInt(1, userId);
				int rowsAffected = ps.executeUpdate();

				// Invalidate session after account deletion.
				if (rowsAffected > 0)
				{
					session.invalidate();
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(new Gson().toJson("Account deleted successfully."));
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write(new Gson().toJson("Account deletion failed."));
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
