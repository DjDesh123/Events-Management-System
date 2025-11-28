package events;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EventDatabaseStorage {

    private static final String FILE_NAME = "EventDatabase.dat";

    public static void save(Map<Integer, Event> eventMap){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(eventMap);
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
        }
    }

    public static Map<Integer, Event> load(){
        try (ObjectInputStream in = new ObjectInputStream
                (new FileInputStream(FILE_NAME))) {
            return (Map<Integer, Event>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }
}
