

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegisterServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
    static class Errors {
    	String usernameError;
    	String passwordError;
    	String emailError;
    }
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
		String username = data.get("username").getAsString();
		String password = data.get("password").getAsString();
		String email = data.get("email").getAsString();
		
		Gson gson = new Gson();
		Errors e = new Errors();
		if(!email.endsWith("@usc.edu")) {
			e.emailError = "Not a valid email (must be @usc.edu)";
		}
		if(username == null || username.isBlank()) {
			e.usernameError = "Username Missing";
		}
		if(password == null || password.isBlank()) {
			e.passwordError = "Password Missing";
		}
		
		if(e.emailError!=null || e.usernameError!=null || e.passwordError!=null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			pw.write(gson.toJson(e));
			pw.flush();
			return;
		}
		
		int userId = registerUser(email, username, password);
		if(userId == -2) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			pw.write(gson.toJson("Username is already taken"));
			pw.flush();
		}
		else if(userId == -1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			pw.write(gson.toJson("Registration failed"));
			pw.flush();
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(userId));
			pw.flush();
		}
	}
	
	private int registerUser(String email, String username, String password) {
		try {
			
			Connection c = DatabaseConnector.getConnection();
			// check if username already exists in database of users
			PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
			ps.setString(1,  username);
			ResultSet rs = ps.executeQuery();
			// username already taken
			if(rs.next() && rs.getInt(1) > 0) {
				return -2;
			}
			
			PreparedStatement insertUser = c.prepareStatement("INSERT INTO users (email, username, password) VALUES (?, ?, ?)",
		            Statement.RETURN_GENERATED_KEYS);
			insertUser.setString(1, email);
			insertUser.setString(2, username);
			insertUser.setString(3, password);
			
			insertUser.executeUpdate();
			ResultSet user_id = insertUser.getGeneratedKeys();
			// return the user id of the newly added user
			if(user_id.next()) {
				return user_id.getInt(1);
			} else {
				return -1;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
