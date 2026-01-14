package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for TripOffering table
 */
public class TripOfferingDAO {
    
    /**
     * Get all trip offerings with details
     */
    public static List<Object[]> getAllTripOfferings() throws SQLException {
        List<Object[]> offerings = new ArrayList<>();
        String query = "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, " +
                      "toff.`Date`, toff.ScheduledStartTime, toff.ScheduledArrivalTime, " +
                      "toff.DriverID, toff.DriverName, d.DriverTelephoneNumber, toff.BusID " +
                      "FROM TripOffering toff " +
                      "JOIN Trip t ON toff.TripNumber = t.TripNumber " +
                      "LEFT JOIN Driver d ON toff.DriverID = d.DriverID " +
                      "ORDER BY toff.`Date`, toff.ScheduledStartTime";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                offerings.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getString("StartLocationName"),
                    rs.getString("DestinationName"),
                    rs.getDate("Date"),
                    rs.getTime("ScheduledStartTime"),
                    rs.getTime("ScheduledArrivalTime"),
                    rs.getObject("DriverID"),  // Can be null
                    rs.getString("DriverName"),
                    rs.getString("DriverTelephoneNumber"),
                    rs.getInt("BusID")
                });
            }
        }
        return offerings;
    }
    
    /**
     * Search trip offerings by criteria (supports any combination of parameters)
     */
    public static List<Object[]> searchTripOfferings(String tripNumber, String startLocation, 
                                                     String destination, String date, 
                                                     String startTime, String driverID,
                                                     String driverName, String busID) throws SQLException {
        List<Object[]> offerings = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT t.TripNumber, t.StartLocationName, t.DestinationName, " +
            "toff.`Date`, toff.ScheduledStartTime, toff.ScheduledArrivalTime, " +
            "toff.DriverID, toff.DriverName, d.DriverTelephoneNumber, toff.BusID " +
            "FROM TripOffering toff " +
            "JOIN Trip t ON toff.TripNumber = t.TripNumber " +
            "LEFT JOIN Driver d ON toff.DriverID = d.DriverID WHERE 1=1"
        );
        
        if (tripNumber != null && !tripNumber.trim().isEmpty()) {
            query.append(" AND t.TripNumber = ?");
        }
        if (startLocation != null && !startLocation.trim().isEmpty()) {
            query.append(" AND t.StartLocationName LIKE ?");
        }
        if (destination != null && !destination.trim().isEmpty()) {
            query.append(" AND t.DestinationName LIKE ?");
        }
        if (date != null && !date.trim().isEmpty()) {
            query.append(" AND toff.`Date` = ?");
        }
        if (startTime != null && !startTime.trim().isEmpty()) {
            query.append(" AND toff.ScheduledStartTime = ?");
        }
        if (driverID != null && !driverID.trim().isEmpty()) {
            query.append(" AND toff.DriverID = ?");
        }
        if (driverName != null && !driverName.trim().isEmpty()) {
            query.append(" AND toff.DriverName LIKE ?");
        }
        if (busID != null && !busID.trim().isEmpty()) {
            query.append(" AND toff.BusID = ?");
        }
        
        query.append(" ORDER BY toff.`Date`, toff.ScheduledStartTime");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            int paramIndex = 1;
            if (tripNumber != null && !tripNumber.trim().isEmpty()) {
                pstmt.setInt(paramIndex++, Integer.parseInt(tripNumber));
            }
            if (startLocation != null && !startLocation.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + startLocation + "%");
            }
            if (destination != null && !destination.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + destination + "%");
            }
            if (date != null && !date.trim().isEmpty()) {
                pstmt.setString(paramIndex++, date);
            }
            if (startTime != null && !startTime.trim().isEmpty()) {
                pstmt.setString(paramIndex++, startTime);
            }
            if (driverID != null && !driverID.trim().isEmpty()) {
                pstmt.setInt(paramIndex++, Integer.parseInt(driverID));
            }
            if (driverName != null && !driverName.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + driverName + "%");
            }
            if (busID != null && !busID.trim().isEmpty()) {
                pstmt.setInt(paramIndex++, Integer.parseInt(busID));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                offerings.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getString("StartLocationName"),
                    rs.getString("DestinationName"),
                    rs.getDate("Date"),
                    rs.getTime("ScheduledStartTime"),
                    rs.getTime("ScheduledArrivalTime"),
                    rs.getObject("DriverID"),
                    rs.getString("DriverName"),
                    rs.getString("DriverTelephoneNumber"),
                    rs.getInt("BusID")
                });
            }
        }
        return offerings;
    }
    
    /**
     * Add a new trip offering
     */
    public static boolean addTripOffering(int tripNumber, String date, String startTime, 
                                         String arrivalTime, String driverName, int busID) throws SQLException {
        // First, get the DriverID from DriverName
        String getDriverIDQuery = "SELECT DriverID FROM Driver WHERE DriverName = ?";
        Integer driverID = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getDriverIDQuery)) {
            pstmt.setString(1, driverName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                driverID = rs.getInt("DriverID");
            }
        }
        
        // Insert with both DriverID and DriverName
        String query = "INSERT INTO TripOffering (TripNumber, `Date`, ScheduledStartTime, " +
                      "ScheduledArrivalTime, DriverID, DriverName, BusID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            pstmt.setString(2, date);
            pstmt.setString(3, startTime);
            pstmt.setString(4, arrivalTime);
            if (driverID != null) {
                pstmt.setInt(5, driverID);
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            pstmt.setString(6, driverName);
            pstmt.setInt(7, busID);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Delete a trip offering
     */
    public static boolean deleteTripOffering(int tripNumber, String date, String startTime) throws SQLException {
        String query = "DELETE FROM TripOffering WHERE TripNumber = ? AND `Date` = ? AND ScheduledStartTime = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            pstmt.setString(2, date);
            pstmt.setString(3, startTime);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Change driver for a trip offering
     */
    public static boolean changeDriver(int tripNumber, String date, String startTime, String newDriverName) throws SQLException {
        // First, get the DriverID from DriverName
        String getDriverIDQuery = "SELECT DriverID FROM Driver WHERE DriverName = ?";
        Integer driverID = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getDriverIDQuery)) {
            pstmt.setString(1, newDriverName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                driverID = rs.getInt("DriverID");
            }
        }
        
        // Update both DriverID and DriverName
        String query = "UPDATE TripOffering SET DriverID = ?, DriverName = ? " +
                      "WHERE TripNumber = ? AND `Date` = ? AND ScheduledStartTime = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (driverID != null) {
                pstmt.setInt(1, driverID);
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, newDriverName);
            pstmt.setInt(3, tripNumber);
            pstmt.setString(4, date);
            pstmt.setString(5, startTime);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Change bus for a trip offering
     */
    public static boolean changeBus(int tripNumber, String date, String startTime, int newBusID) throws SQLException {
        String query = "UPDATE TripOffering SET BusID = ? " +
                      "WHERE TripNumber = ? AND `Date` = ? AND ScheduledStartTime = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, newBusID);
            pstmt.setInt(2, tripNumber);
            pstmt.setString(3, date);
            pstmt.setString(4, startTime);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}
