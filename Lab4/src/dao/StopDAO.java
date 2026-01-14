package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Stop table
 */
public class StopDAO {
    
    /**
     * Get all stops
     */
    public static List<Object[]> getAllStops() throws SQLException {
        List<Object[]> stops = new ArrayList<>();
        String query = "SELECT StopNumber, StopAddress FROM Stop ORDER BY StopNumber";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                stops.add(new Object[]{
                    rs.getInt("StopNumber"),
                    rs.getString("StopAddress")
                });
            }
        }
        return stops;
    }
    
    /**
     * Add a new stop
     */
    public static boolean addStop(int stopNumber, String address) throws SQLException {
        String query = "INSERT INTO Stop (StopNumber, StopAddress) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, stopNumber);
            pstmt.setString(2, address);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}
