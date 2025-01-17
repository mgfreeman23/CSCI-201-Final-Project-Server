package com.collegeapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/explore")
public class ExploreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String uid = request.getParameter("uid");
		
		//get a DB connection
		PreparedStatement ps;
		List<User> users = new ArrayList<User>();
		
		//get a list of users except that username (if username sent)
		 try {
			Connection conn = DatabaseConnector.getConnection();
            
            String sql = "";
            if (uid == null) {
            	sql = "SELECT * FROM users";
            } else {
            	sql = "SELECT * FROM users WHERE user_id != " + uid;
            }
            ps = conn.prepareStatement(sql);
            
          //for each user, create a user object and add to the list
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
	            	String id = rs.getString("user_id");
	                String email = rs.getString("email_address");
	                String uname = rs.getString("username");
	                String age = rs.getString("age");
	                String major = rs.getString("major");
	                String hometown = rs.getString("hometown");
	                String ig = rs.getString("instagram_handle");
	
	                String hobby_sql = "SELECT * FROM hobbies WHERE user_id = ?";
	                PreparedStatement hobby_ps = conn.prepareStatement(hobby_sql);
	                hobby_ps.setString(1, id);
	                ResultSet hobby_rs = hobby_ps.executeQuery();
	                List<String> hobbies = new ArrayList<String>();
	                while(hobby_rs.next()) {
	                	String hobby = hobby_rs.getString("hobby");
	                	hobbies.add(hobby);
	                }
	            try {   
	            	int id_int = Integer.parseInt(id);
	            	int age_int = Integer.parseInt(age);
	                User u = new User(id_int, email, uname, "none", age_int, major, hometown, hobbies, ig);
	                users.add(u);
	            } catch (Exception e) {
	            	continue;
	            } finally {
	            	
	            }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		 
		 if (uid != null) {
			// RUN MATCHING ALGORITHM
			 Matching matching = new Matching(users);
			 List<Matching.Match> matches_ = null;
			 for (User user : users) {
				 if (user.getUserID() == Integer.parseInt(uid)) {
					 matches_ = matching.findMatches(user.getUserID());
					 break;
				 }
			 }
			 // If there are no matches, return all users
			 // If there are matches, return them in descending score order
			 if (matches_ != null) {
				 users = new ArrayList<User>();
				 for (Matching.Match match : matches_) {
					 users.add(match.getUser());
				 }
			 }
		 }
		
		
		//convert list into JSON and send
		 PrintWriter out = response.getWriter();
		 Gson gson = new Gson();
	     String json = gson.toJson(users);
	     out.print(json);
	     out.flush();
	}
}
