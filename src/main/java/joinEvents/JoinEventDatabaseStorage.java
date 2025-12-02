package joinEvents;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class JoinEventDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:JoinEventDatabase.sqlite";

    public static void createTableIfNotExist() {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS joinEvents (" +
                            "joinId INTEGER PRIMARY KEY, " +
                            "userId INTEGER NOT NULL, " +
                            "eventId INTEGER NOT NULL" +
                            ");"
            );

        } catch (SQLException e) {
            System.err.println("JoinEvent table creation error: " + e.getMessage());
        }
    }

    public static Map<Integer, JoinEvent> load() {

        Map<Integer, JoinEvent> joinMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM joinEvents")) {

            while (rs.next()) {

                JoinEvent je = new JoinEvent(
                        rs.getInt("joinId"),
                        rs.getInt("userId"),
                        rs.getInt("eventId")
                );

                joinMap.put(je.getJoinId(), je);
            }

        } catch (SQLException e) {
            System.err.println("JoinEvent load error: " + e.getMessage());
        }

        return joinMap;
    }


    public static void save(Map<Integer, JoinEvent> joinMap) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            stmt.execute("DELETE FROM joinEvents");

            String sql = "INSERT INTO joinEvents VALUES(?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                for (JoinEvent je : joinMap.values()) {
                    pstmt.setInt(1, je.getJoinId());
                    pstmt.setInt(2, je.getUserId());
                    pstmt.setInt(3, je.getEventId());

                    pstmt.addBatch();
                }

                pstmt.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("JoinEvent save error: " + e.getMessage());
        }
    }
}
