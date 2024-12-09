package com.collegeapp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public LoginServlet(){
        super();
    }

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throw IOException{
		String email = req.getParameter("email");

		PrintWriter pw = res.getWriter();
		Gson gson = new Gson();

		DatabaseConnector dbc = null;
		Connection conn = null;

		try{
			dbc = new DatabaseConnector();
			conn = DatabaseConnector.getConnection();
		}
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("SELECT email FROM app_database.users WHERE email_address=?");
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
		}

		try {
			ps.setString(1, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResultSet rs = null;
		try { 							// checks if email exists in database already
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try{
			if(!rs.next()){
				// if it's null --> email does not exist, do not send
				// return error code
				pw.write(gson.toJson(-1));
			    pw.flush();
			}
			else{
				// not null --> can send email, success code
				// need to get password
				ps = conn.prepareStatement("SELECT password FROM app_database.users WHERE email_address=?");
				ps.setString(1, email);
				rs.next();
				String pword = rs.getString();
				// code from https://www.geeksforgeeks.org/send-email-using-java-program/
				String sender = "gjlee@usc.edu";
				String host = "127.0.0.1";
				Properties properties = System.getProperties(); 
      			// Setting up mail server 
      			properties.setProperty("mail.smtp.host", host); 
      			// creating session object to get properties 
     			Session session = Session.getDefaultInstance(properties); 
				try { 
					// MimeMessage object. 
					MimeMessage message = new MimeMessage(session); 
					// Set From Field: adding senders email to from field. 
					message.setFrom(new InternetAddress(sender)); 
					// Set To Field: adding recipient's email to from field. 
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); 
					message.setSubject("[TrojanMatch] Forgot Password"); 
					message.setText("Thank you for using TrojanMatch. Please try logging in with this password: " + pword); 
			
					// Send email. 
					Transport.send(message); 
					System.out.println("Mail successfully sent"); 
				} 
				catch (MessagingException mex) { 
					mex.printStackTrace(); 
				} 
				pw.write(gson.toJson(0));
			    pw.flush();
			}
		}

	}
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
    	
        String uname = req.getParameter("uname");
        String pass = req.getParameter("pass");
        PrintWriter pw = res.getWriter();
        Gson gson = new Gson();

        DatabaseConnector dbc = null;
        Connection conn = null;
        
        try{
            dbc = new DatabaseConnector();
            conn = DatabaseConnector.getConnection();
        }
        catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // SQL script for finding just the username first
        PreparedStatement ps  = null;
        
		try {
			ps = conn.prepareStatement("SELECT username FROM app_database.users WHERE username=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			ps.setString(1, uname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // if it's null -> wrong username: code -2
        try {
			if(!rs.next()){
			    pw.write(gson.toJson(-2)); 
			    pw.flush();
			}
			else{ // if username exists, then we can check that the passwords match as well
			    ps = conn.prepareStatement("SELECT password, userID FROM app_database.users WHERE username=?");
			    ps.setString(1, uname);
			    rs = ps.executeQuery();

			    rs.next();
			    if(rs.getString("password") != pass){ // wrong password: code -1
			        pw.write(gson.toJson(-1));
			        pw.flush();
			    }
			    else{ // if it does equal, return the userID for future use within user's session
			        pw.write(gson.toJson(rs.getInt("userID")));
			        pw.flush();
			    }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }

}

