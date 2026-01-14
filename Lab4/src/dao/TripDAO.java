package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Trip table
 */
public class TripDAO {
    
    /**
     * Get all trips
     */
    public static List<Object[]> getAllTrips() throws SQLException {
        List<Object[]> trips = new ArrayList<>();
        String query = "SELECT TripNumber, StartLocationName, DestinationName FROM Trip";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                trips.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getString("StartLocationName"),
                    rs.getString("DestinationName")
                });
            }
        }
        return trips;
    }
    
    /**
     * Add a new trip
     */
    public static boolean addTrip(int tripNumber, String startLocation, String destination) throws SQLException {
        String query = "INSERT INTO Trip (TripNumber, StartLocationName, DestinationName) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            pstmt.setString(2, startLocation);
            pstmt.setString(3, destination);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Check if trip exists
     */
    public static boolean tripExists(int tripNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Trip WHERE TripNumber = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
