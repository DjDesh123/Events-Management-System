package joinEvents;

import java.util.HashMap;
import java.util.Map;

public class JoinEventDatabase {

    private Map<Integer, JoinEvent> joinEvents = new HashMap<>();
    private int nextId = 1;

    /* ====================== LOAD / SAVE SUPPORT ======================= */

    public Map<Integer, JoinEvent> getJoinEvents() {
        return joinEvents;
    }

    // FIX — controller calls this at startup
    public void setJoinEvents(Map<Integer, JoinEvent> events) {
        if (events != null) {
            this.joinEvents = events;
            // next id continues from last saved one
            this.nextId = events.keySet().stream().max(Integer::compare).orElse(0) + 1;
        }
    }

    /* ======================= ID GENERATION ============================ */

    // FIX — controller uses this
    public int generateNewId() {
        return nextId++;
    }

    /* ====================== CRUD OPERATIONS ============================ */

    // FIX — controller calls add(join)
    public void add(JoinEvent joinEvent) {
        joinEvents.put(joinEvent.getJoinId(), joinEvent);
    }

    public JoinEvent get(int id) {
        return joinEvents.get(id);
    }

    // Update if needed later
    public void update(int id, JoinEvent event) {
        if (joinEvents.containsKey(id)) joinEvents.put(id, event);
    }

    // FIX — controller calls delete(int)
    public void delete(int id) {
        joinEvents.remove(id);
    }
}
