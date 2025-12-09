package user;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseStorage {

    private static final String DB_URL = "jdbc:sqlite:UserDatabase.sqlite";

    // Ensure users table exists
    public static void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "userId INTEGER PRIMARY KEY," +
                "firstName TEXT NOT NULL," +
                "lastName TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "salt TEXT NOT NULL," +
                "accountType TEXT NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table verified or created.");
        } catch (SQLException e) {
            System.err.println("User DB table creation error: " + e.getMessage());
        }
    }

    // Load all users from DB
    public static Map<Integer, User> load() {
        createTableIfNotExist();

        Map<Integer, User> userMap = new HashMap<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("salt"),
                        User.accountType.valueOf(rs.getString("accountType"))
                );
                userMap.put(user.getUserId(), user);
            }

        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        return userMap;
    }

    // Save all users to DB (insert or replace)
    public static void save(Map<Integer, User> userMap) {
        createTableIfNotExist();

        String sql = "INSERT OR REPLACE INTO users(userId, firstName, lastName, email, password, salt, accountType) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (User user : userMap.values()) {
                    pstmt.setInt(1, user.getUserId());
                    pstmt.setString(2, user.getFirstName());
                    pstmt.setString(3, user.getLastName());
                    pstmt.setString(4, user.getEmail());
                    pstmt.setString(5, user.getPassword());
                    pstmt.setString(6, user.getSalt());
                    pstmt.setString(7, user.getAccountType().name());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            System.out.println("Users saved to DB.");
        } catch (SQLException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // Convenience method to initialize DB
    public static void init() {
        createTableIfNotExist();
        System.out.println("Database initialized.");
    }
}
