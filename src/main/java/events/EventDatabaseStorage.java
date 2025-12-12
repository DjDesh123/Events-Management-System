package events;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class EventDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:EventDatabase.sqlite";

    // creates the SQLite table if its doesnt already exist
    public static void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS events (" +
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
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Events table verified or created.");

        } catch (SQLException e) {
            System.err.println("Event DB table creation error: " + e.getMessage());
        }
    }

    // loads all events from the database and returns them as a map
    public static Map<Integer, Event> load() {
        createTableIfNotExist(); // ensure table exists before loading

        Map<Integer, Event> eventMap = new HashMap<>();

        String sql = "SELECT * FROM events";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

            System.out.println("Events loaded: " + eventMap.size());

        } catch (SQLException e) {
            System.err.println("Event load error: " + e.getMessage());
        }

        return eventMap;
    }

    // saves the entire in-memory event map to the database
    public static void save(Map<Integer, Event> eventMap) {
        createTableIfNotExist(); // ensure table exists before saving

        String sqlInsert = "INSERT INTO events VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            // delete old data
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM events");
            }

            // insert all current events
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
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
            System.out.println("Events saved to DB: " + eventMap.size());

        } catch (SQLException e) {
            System.err.println("Event save error: " + e.getMessage());
        }
    }
}
