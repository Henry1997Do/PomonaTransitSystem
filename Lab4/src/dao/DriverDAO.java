package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Driver table
 */
public class DriverDAO {
    
    /**
     * Get all drivers
     */
    public static List<Object[]> getAllDrivers() throws SQLException {
        List<Object[]> drivers = new ArrayList<>();
        String query = "SELECT DriverID, DriverName, DriverTelephoneNumber FROM Driver ORDER BY DriverID";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                drivers.add(new Object[]{
                    rs.getInt("DriverID"),
                    rs.getString("DriverName"),
                    rs.getString("DriverTelephoneNumber")
                });
            }
        }
        return drivers;
    }
    
    /**
     * Add a new driver
     */
    public static boolean addDriver(String name, String phone) throws SQLException {
        String query = "INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Get weekly schedule for a driver
     */
    public static List<Object[]> getWeeklySchedule(String driverName, String startDate) throws SQLException {
        List<Object[]> schedule = new ArrayList<>();
        String query = "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, " +
                      "toff.`Date`, toff.ScheduledStartTime, toff.ScheduledArrivalTime, toff.BusID " +
                      "FROM TripOffering toff " +
                      "JOIN Trip t ON toff.TripNumber = t.TripNumber " +
                      "WHERE toff.DriverName = ? AND toff.`Date` >= ? AND toff.`Date` < DATE_ADD(?, INTERVAL 7 DAY) " +
                      "ORDER BY toff.`Date`, toff.ScheduledStartTime";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, driverName);
            pstmt.setString(2, startDate);
            pstmt.setString(3, startDate);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                schedule.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getString("StartLocationName"),
                    rs.getString("DestinationName"),
                    rs.getDate("Date"),
                    rs.getTime("ScheduledStartTime"),
                    rs.getTime("ScheduledArrivalTime"),
                    rs.getInt("BusID")
                });
            }
        }
        return schedule;
    }
    
    /**
     * Check if driver exists
     */
    public static boolean driverExists(String driverName) throws SQLException {
        String query = "SELECT COUNT(*) FROM Driver WHERE DriverName = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, driverName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Get driver name by driver ID
     */
    public static String getDriverNameByID(int driverID) throws SQLException {
        String query = "SELECT DriverName FROM Driver WHERE DriverID = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, driverID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("DriverName");
            }
        }
        return null;
    }
}
