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
		String username = request.getParameter("username");
		
		//get a DB connection
		PreparedStatement ps;
		Connection conn;
		List<User> users = new ArrayList<User>();
		
		//get a list of users except that username (if username sent)
		 try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DatabaseConnector.getConnection();
            
            String sql = "";
            if (username == null) {
            	sql = "SELECT * FROM app_database.users";
            } else {
            	sql = "SELECT * FROM app_database.users WHERE username != " + username;
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
                
                Connection conn_hobby = DatabaseConnector.getConnection();

                String hobby_sql = "SELECT * FROM app_database.hobbies WHERE user_id = ?";
                PreparedStatement hobby_ps = conn_hobby.prepareStatement(hobby_sql);
                hobby_ps.setString(1, id);
                ResultSet hobby_rs = hobby_ps.executeQuery();
                List<String> hobbies = new ArrayList<String>();
                while(hobby_rs.next()) {
                	String hobby = hobby_rs.getString("hobby");
                	hobbies.add(hobby);
                }
                
                User u = new User(Integer.parseInt(id), email, uname, "none", Integer.parseInt(age), major, hometown, hobbies, ig);
                users.add(u);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		//MATCHING ALGORITHM ?
		 
		//convert list into JSON and send
		 PrintWriter out = response.getWriter();
		 Gson gson = new Gson();
	     String json = gson.toJson(users);
	     out.print(json);
	     out.flush();
	}
}