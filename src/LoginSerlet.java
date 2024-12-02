package login;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    public LoginServlet(){
        super();
    }
}

protected void doGet(HttpServletRequest req, HttpServletResponse res){
    String uname = req.getParameter("uname");
    String pass = req.getParameter("pass");
    PrintWriter pw = res.getWriter();
    Gson gson = new Gson();

    DatabaseConnector dbc = null;
    Connection conn = null;
    
    try{
        dbc = new DatabaseConnector();
        conn = dbc.getConnection();
    }
    catch(SQLException sqle){
        System.out.println(sqle.getMessage());
    }

    res.setContentType("applicaiton/json");
    res.setCharacterEncoding("UTF-8");

    // SQL script for finding just the username first
    PreparedStatement ps = conn.prepareStatement("SELECT username FROM user WHERE username=?");
    ps.setString(1, uname);
    ResultSet rs = ps.executeQuery();

    // if it's null -> wrong username: code -2
    if(rs.next() == null){
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

    ps.close();
    rs.close();


}