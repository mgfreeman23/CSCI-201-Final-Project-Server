import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // Hardcoded database connection details
    private static final String URL = "jdbc:mysql://database-1.cp4ike4u0cyv.us-east-2.rds.amazonaws.com:3306/app_database";
    private static final String USERNAME = "admin"; // Database username
    private static final String PASSWORD = "CSCIProject"; // Database password

    static {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}