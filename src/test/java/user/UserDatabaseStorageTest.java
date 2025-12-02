package user;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserDatabaseStorageTest {

    @BeforeAll
    static void setupDatabase() throws Exception {
        UserDatabaseStorage.DB_URL = "jdbc:sqlite:testUserDB.db";

        try (Connection conn = DriverManager.getConnection(UserDatabaseStorage.DB_URL)) {
            String sql =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "firstName TEXT NOT NULL," +
                            "lastName TEXT NOT NULL," +
                            "email TEXT NOT NULL," +
                            "password TEXT NOT NULL," +
                            "salt TEXT NOT NULL," +
                            "accountType TEXT NOT NULL" +
                            ");";

            conn.createStatement().execute(sql);
        }
    }

    @Test
    void testSaveUser() throws Exception {

        Map<Integer, User> users = new HashMap<>();
        User u = new User(
                1,
                "John",
                "Smith",
                "test@example.com",
                "pass",
                "salt",
                User.accountType.STUDENT
        );

        users.put(1, u);

        UserDatabaseStorage.save(users);

        try (Connection conn = DriverManager.getConnection(UserDatabaseStorage.DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email=?");
            stmt.setString(1, "test@example.com");
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            assertEquals("John", rs.getString("firstName"));
            assertEquals("Smith", rs.getString("lastName"));
            assertEquals("test@example.com", rs.getString("email"));
        }
    }

    @Test
    void testLoadUser() throws Exception {
        try (Connection conn = DriverManager.getConnection(UserDatabaseStorage.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users(firstName,lastName,email,password,salt,accountType) VALUES (?,?,?,?,?,?)"
             )) {

            stmt.setString(1, "Alice");
            stmt.setString(2, "Johnson");
            stmt.setString(3, "alice@example.com");
            stmt.setString(4, "pass123");
            stmt.setString(5, "salt123");
            stmt.setString(6, User.accountType.STUDENT.name());
            stmt.executeUpdate();
        }

        Map<Integer, User> loadedUsers = UserDatabaseStorage.load();

        assertFalse(loadedUsers.isEmpty());

        User alice = loadedUsers.values().stream()
                .filter(u -> u.getEmail().equals("alice@example.com"))
                .findFirst()
                .orElse(null);

        assertNotNull(alice);
        assertEquals("Alice", alice.getFirstName());
        assertEquals("Johnson", alice.getLastName());
        assertEquals(User.accountType.STUDENT, alice.getAccountType());
    }

}
