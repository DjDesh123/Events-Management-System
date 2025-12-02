package events;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class EventDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:EventDatabase.sqlite";

    public static void createTableIfNotExist() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS events (" +
                            "eventId INTEGER PRIMARY KEY, " +
                            "creatorId INTEGER, " +
                            "eventTitle TEXT, " +
                            "location TEXT, " +
                            "startDate TEXT, " +
                            "endDate TEXT, " +
                            "startTime TEXT, " +
                            "endTime TEXT, " +
                            "maxAttendees INTEGER, " +
                            "description TEXT" +
                            ");"
            );

        } catch (SQLException e) {
            System.err.println("Event DB table creation error: " + e.getMessage());
        }
    }

    public static Map<Integer, Event> load() {

        Map<Integer, Event> eventMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM events")) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("eventId"),
                        rs.getInt("creatorId"),
                        rs.getString("eventTitle"),
                        rs.getString("location"),
                        LocalDate.parse(rs.getString("startDate")),
                        LocalDate.parse(rs.getString("endDate")),
                        LocalTime.parse(rs.getString("startTime")),
                        LocalTime.parse(rs.getString("endTime")),
                        rs.getInt("maxAttendees"),
                        null,
                        rs.getString("description")
                );

                eventMap.put(event.getEventId(), event);
            }

        } catch (SQLException e) {
            System.err.println("Event load error: " + e.getMessage());
        }

        return eventMap;
    }

    public static void save(Map<Integer, Event> eventMap) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            // wipe existing data
            stmt.execute("DELETE FROM events");

            String sql = "INSERT INTO events VALUES(?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                for (Event e : eventMap.values()) {

                    pstmt.setInt(1, e.getEventId());
                    pstmt.setInt(2, e.getCreatorId());
                    pstmt.setString(3, e.getEventTitle());
                    pstmt.setString(4, e.getLocation());
                    pstmt.setString(5, e.getStartDate().toString());
                    pstmt.setString(6, e.getEndDate().toString());
                    pstmt.setString(7, e.getStartTime().toString());
                    pstmt.setString(8, e.getEndTime().toString());
                    pstmt.setInt(9, e.getMaxAttendees());
                    pstmt.setString(10, e.getDescription());

                    pstmt.addBatch();
                }

                pstmt.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Event save error: " + e.getMessage());
        }
    }
}
