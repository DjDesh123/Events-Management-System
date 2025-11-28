package joinEvents;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JoinEventDatabaseStorage {

    private static final String FILE_NAME = "JoinEventDatabase.dat";

    public static void save(Map<Integer, JoinEvent> joinEventMap){
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {

            out.writeObject(joinEventMap);
        } catch (IOException e) {
            System.err.println("Error saving JoinEvent DB: " + e.getMessage());
        }
    }

    public static Map<Integer, JoinEvent> load(){
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {

            return (Map<Integer, JoinEvent>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }
}
