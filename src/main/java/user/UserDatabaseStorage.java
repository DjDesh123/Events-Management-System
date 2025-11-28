package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseStorage {
    private static final String FILE_NAME = "UserDatabase.dat";

    public static void save(Map<Integer, User> userMap){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(userMap);
        } catch(IOException e){
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public static Map<Integer, User> load(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<Integer, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous save found, creating new.");
            return new HashMap<>();
        }
    }
}
