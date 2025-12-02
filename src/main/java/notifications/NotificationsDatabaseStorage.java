package notifications;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NotificationsDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:NotificationsDatabase.sqlite";

    public static void createTableIfNotExist() {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS notifications (" +
                            "notId INTEGER PRIMARY KEY, " +
                            "userId INTEGER NOT NULL, " +
                            "eventId INTEGER NOT NULL, " +
                            "creatorId INTEGER NOT NULL, " +
                            "message TEXT NOT NULL" +
                            ");"
            );

        } catch (SQLException e) {
            System.err.println("Notifications table creation error: " + e.getMessage());
        }
    }

    public static Map<Integer, Notifications> load() {

        Map<Integer, Notifications> notifications = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notifications")) {

            while (rs.next()) {

                Notifications not = new Notifications(
                        rs.getInt("notId"),
                        rs.getInt("userId"),
                        rs.getInt("eventId"),
                        rs.getInt("creatorId"),
                        rs.getString("message")
                );

                notifications.put(not.getNotId(), not);
            }

        } catch (SQLException e) {
            System.err.println("Notifications load error: " + e.getMessage());
        }

        return notifications;
    }

    public static void save(Map<Integer, Notifications> notificationsMap) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            stmt.execute("DELETE FROM notifications");

            String sql = "INSERT INTO notifications VALUES(?,?,?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                for (Notifications n : notificationsMap.values()) {

                    pstmt.setInt(1, n.getNotId());
                    pstmt.setInt(2, n.getUserId());
                    pstmt.setInt(3, n.getEventId());
                    pstmt.setInt(4, n.getCreatorId());
                    pstmt.setString(5, n.getMessage());

                    pstmt.addBatch();
                }

                pstmt.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Notifications save error: " + e.getMessage());
        }
    }
}
