package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for TripStopInfo table
 */
public class TripStopInfoDAO {
    
    /**
     * Get all stops for a specific trip
     */
    public static List<Object[]> getTripStops(int tripNumber) throws SQLException {
        List<Object[]> stops = new ArrayList<>();
        String query = "SELECT tsi.StopNumber, s.StopAddress, tsi.SequenceNumber, tsi.DrivingTime " +
                      "FROM TripStopInfo tsi " +
                      "JOIN Stop s ON tsi.StopNumber = s.StopNumber " +
                      "WHERE tsi.TripNumber = ? " +
                      "ORDER BY tsi.SequenceNumber";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                stops.add(new Object[]{
                    rs.getInt("StopNumber"),
                    rs.getString("StopAddress"),
                    rs.getInt("SequenceNumber"),
                    rs.getInt("DrivingTime")
                });
            }
        }
        return stops;
    }
    
    /**
     * Add trip stop info
     */
    public static boolean addTripStopInfo(int tripNumber, int stopNumber, int sequenceNumber, int drivingTime) throws SQLException {
        String query = "INSERT INTO TripStopInfo (TripNumber, StopNumber, SequenceNumber, DrivingTime) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            pstmt.setInt(2, stopNumber);
            pstmt.setInt(3, sequenceNumber);
            pstmt.setInt(4, drivingTime);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}
