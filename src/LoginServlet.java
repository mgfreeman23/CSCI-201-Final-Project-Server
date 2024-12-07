

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
			ps = conn.prepareStatement("SELECT username FROM user WHERE username=?");
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
			    ps = conn.prepareStatement("SELECT password, userID FROM user WHERE username=?");
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

