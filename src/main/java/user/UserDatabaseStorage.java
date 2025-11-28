package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserDatabaseStorage {

    public static void save(Map<Integer, User> userMap){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UserDatabase.dat"))) {
            oos.writeObject(userMap);
        } catch(IOException e){
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public static Map<Integer, User> load(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UserDatabase.dat"))) {
            return (Map<Integer, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous save found, creating new.");
            return new HashMap<>();
        }
    }
}
