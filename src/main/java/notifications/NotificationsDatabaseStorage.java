package notifications;

import java.sql.*;
import java.util.Map;

public class NotificationsDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:NotificationsDatabase.sqlite";

    //create table if its missing
    public static void createTableIfNotExist() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS notifications (" +
                    "notId INTEGER PRIMARY KEY, " +
                    "userId INTEGER NOT NULL, " +
                    "eventId INTEGER, " +
                    "creatorId INTEGER, " +
                    "message TEXT NOT NULL" +
                    ");";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Load all notifications from the database
    public static Map<Integer, Notifications> load() {
        Map<Integer, Notifications> notifications = new java.util.HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notifications")) {

            while (rs.next()) {
                Notifications n = new Notifications(
                        rs.getInt("notId"),
                        rs.getInt("userId"),
                        rs.getInt("eventId"),
                        rs.getInt("creatorId"),
                        rs.getString("message")
                );
                notifications.put(n.getNotId(), n);
            }

        } catch (SQLException e) {
            System.err.println("Load error: " + e.getMessage());
            e.printStackTrace();
        }

        return notifications;
    }

    // Save all notifications to the database safely
    public static void save(Map<Integer, Notifications> notificationsMap) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            String sql = "INSERT OR REPLACE INTO notifications " +
                    "(notId, userId, eventId, creatorId, message) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Notifications n : notificationsMap.values()) {
                    pstmt.setInt(1, n.getNotId());
                    pstmt.setInt(2, n.getUserId());
                    pstmt.setInt(3, n.getEventId());
                    pstmt.setInt(4, n.getCreatorId());
                    pstmt.setString(5, n.getMessage());
                    pstmt.addBatch();
                }
                int[] counts = pstmt.executeBatch();
                System.out.println("Saved " + counts.length + " notifications to the database.");
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Save error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
