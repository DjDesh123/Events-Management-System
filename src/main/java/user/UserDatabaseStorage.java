package user;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseStorage {

    static String DB_URL = "jdbc:sqlite:UserDatabase.sqlite";

    /**
     * Ensure the users table exists.
     */
    public static void createTableIfNotExist() {
        String sql =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "firstName TEXT NOT NULL," +
                        "lastName TEXT NOT NULL," +
                        "email TEXT NOT NULL UNIQUE," +           // unique email is helpful
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

    /**
     * Load all users from the DB. Ensures table exists first.
     */
    public static Map<Integer, User> load() {
        // Make sure the table exists before attempting to read.
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

    /**
     * Save all users. Ensures table exists first and uses a transaction for speed/atomicity.
     */
    public static void save(Map<Integer, User> userMap) {
        // Ensure table exists first.
        createTableIfNotExist();

        // Using a transaction and a single connection improves performance and reliability.
        String sql = "INSERT OR IGNORE INTO users(firstName,lastName,email,password,salt,accountType) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (User user : userMap.values()) {
                    pstmt.setString(1, user.getFirstName());
                    pstmt.setString(2, user.getLastName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getPassword());
                    pstmt.setString(5, user.getSalt());
                    pstmt.setString(6, user.getAccountType().name());
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

    /**
     * Convenience init method you can call once at application startup.
     */
    public static void init() {
        createTableIfNotExist();
    }
}
