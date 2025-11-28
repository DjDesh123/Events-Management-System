package notifications;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NotificationsDatabaseStorage {

    private static final String FILE_NAME = "NotificationsDatabase.dat";

    public static void save(Map<Integer, Notifications> notificationsMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(notificationsMap);
        } catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
        }
    }

    public static Map<Integer, Notifications> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<Integer, Notifications>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous notification save found. Creating new...");
            return new HashMap<>();
        }
    }
}
