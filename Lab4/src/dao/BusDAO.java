package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Bus table
 */
public class BusDAO {
    
    /**
     * Get all buses
     */
    public static List<Object[]> getAllBuses() throws SQLException {
        List<Object[]> buses = new ArrayList<>();
        String query = "SELECT BusID, Model, Year FROM Bus ORDER BY BusID";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                buses.add(new Object[]{
                    rs.getInt("BusID"),
                    rs.getString("Model"),
                    rs.getInt("Year")
                });
            }
        }
        return buses;
    }
    
    /**
     * Add a new bus
     */
    public static boolean addBus(int busID, String model, int year) throws SQLException {
        String query = "INSERT INTO Bus (BusID, Model, Year) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busID);
            pstmt.setString(2, model);
            pstmt.setInt(3, year);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Delete a bus
     */
    public static boolean deleteBus(int busID) throws SQLException {
        String query = "DELETE FROM Bus WHERE BusID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busID);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Check if bus exists
     */
    public static boolean busExists(int busID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Bus WHERE BusID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Get list of trips using a specific bus
     */
    public static List<Object[]> getTripsUsingBus(int busID) throws SQLException {
        List<Object[]> trips = new ArrayList<>();
        String query = "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, " +
                      "toff.`Date`, toff.ScheduledStartTime " +
                      "FROM TripOffering toff " +
                      "JOIN Trip t ON toff.TripNumber = t.TripNumber " +
                      "WHERE toff.BusID = ? " +
                      "ORDER BY toff.`Date`, toff.ScheduledStartTime";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, busID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                trips.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getString("StartLocationName"),
                    rs.getString("DestinationName"),
                    rs.getDate("Date"),
                    rs.getTime("ScheduledStartTime")
                });
            }
        }
        return trips;
    }
    
    /**
     * Reassign all trips from one bus to another
     */
    public static boolean reassignTripsToNewBus(int oldBusID, int newBusID) throws SQLException {
        String query = "UPDATE TripOffering SET BusID = ? WHERE BusID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, newBusID);
            pstmt.setInt(2, oldBusID);
            
            return pstmt.executeUpdate() >= 0;
        }
    }
}
