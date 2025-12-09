package user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDatabase {
    private final Map<Integer, User> userMap = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public UserDatabase() {
        // Load existing users from DB
        Map<Integer, User> loadedUsers = UserDatabaseStorage.load();
        if (loadedUsers != null) {
            userMap.putAll(loadedUsers);

            // Update counter to highest userId to avoid overwriting
            int maxId = loadedUsers.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
            idCounter.set(maxId);
        }
    }

    // Generate new unique ID
    public int generateNewId() {
        return idCounter.incrementAndGet();
    }

    // Add or update user
    public void add(User user) {
        if (user.getUserId() > idCounter.get()) {
            idCounter.set(user.getUserId());
        }
        userMap.put(user.getUserId(), user);
        UserDatabaseStorage.save(userMap); // persist immediately
    }

    // Get user by ID
    public User get(int userId) {
        return userMap.get(userId);
    }

    // Delete user
    public void delete(int userId) {
        userMap.remove(userId);
        UserDatabaseStorage.save(userMap);
    }

    // Get all users
    public Map<Integer, User> getUsers() {
        return userMap;
    }

    // Get user by email
    public User getByEmail(String email) {
        for (User user : userMap.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    // Convenience method to persist all users manually
    public void saveAll() {
        UserDatabaseStorage.save(userMap);
    }
}

