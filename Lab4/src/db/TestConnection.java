package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test utility to verify database connection
 */
public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        if (DBConnection.testConnection()) {
            System.out.println("✓ Connection successful!");
            
            try {
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                
                // Test query
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Trip");
                if (rs.next()) {
                    System.out.println("✓ Found " + rs.getInt("count") + " trips in database");
                }
                
                rs.close();
                stmt.close();
            } catch (Exception e) {
                System.err.println("✗ Error testing database: " + e.getMessage());
            }
        } else {
            System.out.println("✗ Connection failed!");
        }
        
        DBConnection.closeConnection();
    }
}
