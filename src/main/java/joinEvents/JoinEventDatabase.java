package joinEvents;

import java.util.HashMap;
import java.util.Map;

public class JoinEventDatabase {

    private Map<Integer, JoinEvent> joinEvents = new HashMap<>();
    private int nextId = 1;


    //returns all joins
    public Map<Integer, JoinEvent> getJoinEvents() {
        return joinEvents;
    }

    // loads join records from the db to memory
    public void setJoinEvents(Map<Integer, JoinEvent> events) {
        if (events != null) {
            this.joinEvents = events;
            // next id continues from last saved one
            this.nextId = events.keySet().stream().max(Integer::compare).orElse(0) + 1;
        }
    }



    // generates the new id
    public int generateNewId() {
        return nextId++;
    }


    //CRUD methods

    public void add(JoinEvent joinEvent) {
        joinEvents.put(joinEvent.getJoinId(), joinEvent);
    }

    public JoinEvent get(int id) {
        return joinEvents.get(id);
    }

    public void delete(int id) {
        joinEvents.remove(id);
    }
}
