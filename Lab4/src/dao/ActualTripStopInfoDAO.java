package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ActualTripStopInfo table
 */
public class ActualTripStopInfoDAO {
    
    /**
     * Get all actual trip stop info
     */
    public static List<Object[]> getAllActualTripStopInfo() throws SQLException {
        List<Object[]> records = new ArrayList<>();
        String query = "SELECT TripNumber, `Date`, ScheduledStartTime, StopNumber, " +
                      "ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, " +
                      "NumberOfPassengerIn, NumberOfPassengerOut " +
                      "FROM ActualTripStopInfo " +
                      "ORDER BY `Date`, ScheduledStartTime, StopNumber";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                records.add(new Object[]{
                    rs.getInt("TripNumber"),
                    rs.getDate("Date"),
                    rs.getTime("ScheduledStartTime"),
                    rs.getInt("StopNumber"),
                    rs.getTime("ScheduledArrivalTime"),
                    rs.getTime("ActualStartTime"),
                    rs.getTime("ActualArrivalTime"),
                    rs.getInt("NumberOfPassengerIn"),
                    rs.getInt("NumberOfPassengerOut")
                });
            }
        }
        return records;
    }
    
    /**
     * Add actual trip stop info record
     */
    public static boolean addActualTripStopInfo(int tripNumber, String date, String scheduledStartTime,
                                               int stopNumber, String scheduledArrivalTime,
                                               String actualStartTime, String actualArrivalTime,
                                               int passengersIn, int passengersOut) throws SQLException {
        String query = "INSERT INTO ActualTripStopInfo " +
                      "(TripNumber, `Date`, ScheduledStartTime, StopNumber, ScheduledArrivalTime, " +
                      "ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, tripNumber);
            pstmt.setString(2, date);
            pstmt.setString(3, scheduledStartTime);
            pstmt.setInt(4, stopNumber);
            pstmt.setString(5, scheduledArrivalTime);
            pstmt.setString(6, actualStartTime);
            pstmt.setString(7, actualArrivalTime);
            pstmt.setInt(8, passengersIn);
            pstmt.setInt(9, passengersOut);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}
