package user;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseStorage {

    // IMPORTANT: For SQLite you must prefix with jdbc:sqlite:
    private static final String DB_URL = "jdbc:sqlite:UserDatabase.sqlite";

    public static void createTableIfNotExist() {
        String sql =
            "CREATE TABLE IF NOT EXISTS users ("+
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT NOT NULL," +
                "lastName TEXT NOT NULL," +
                "email TEXT NOT NULL," +
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

    public static Map<Integer, User> load() {
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

    public static void save(User user) {
        String sql = " INSERT INTO users(firstName,lastName,email,password,salt,accountType) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getSalt());
            pstmt.setString(6, user.getAccountType().name());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }
}
