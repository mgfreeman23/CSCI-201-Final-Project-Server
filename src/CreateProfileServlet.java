

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class CreateProfileServlet
 */
@WebServlet("/CreateProfileServlet")
public class CreateProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
			int userId = data.get("userId").getAsInt();
			int age = data.get("age").getAsInt();
			String major = data.get("major").getAsString();
			String hometown = data.get("hometown").getAsString();
			String instagram = data.get("instagram").getAsString();
			
			Type hobbyListType = new TypeToken<List<String>>(){}.getType();
			List<String> hobbies = new Gson().fromJson(data.get("hobbies"), hobbyListType);
			
			 // Update users table
            Connection conn = DatabaseConnector.getConnection();
            String updateUserSQL = "UPDATE users SET age = ?, major = ?, hometown = ?, instagram_handle = ? WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateUserSQL)) {
                pstmt.setInt(1, age);
                pstmt.setString(2, major);
                pstmt.setString(3, hometown);
                pstmt.setString(4, instagram);
                pstmt.setInt(5, userId);
                pstmt.executeUpdate();
            }
            
            // update hobbies table
            // by clearing existing hobbies for that user and adding the new selected ones
            String deleteHobbiesSQL = "DELETE FROM hobbies WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteHobbiesSQL)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
            String insertHobbySQL = "INSERT INTO hobbies (user_id, hobby) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertHobbySQL)) {
                for (String hobby : hobbies) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, hobby);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            // Send success response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson("Profile updated successfully"));
            
		} catch(Exception e) {
            	e.printStackTrace();
            	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            	response.getWriter().write(new Gson().toJson("Error updating profile"));
            }	
	}

}
