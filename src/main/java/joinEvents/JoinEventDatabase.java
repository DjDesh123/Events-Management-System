package joinEvents;

import events.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JoinEventDatabase {
    private Map<Integer, JoinEvent> joinEventMap = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger(0);

    public int generateNewId() {
        return idCounter.incrementAndGet();
    }

    //adds the users to the local database
    public void save (JoinEvent joinEvent){
        if (joinEvent.getJoinId() > idCounter.get()) {
            idCounter.set(joinEvent.getJoinId());
        }
        joinEventMap.put(joinEvent.getJoinId(),joinEvent);
    }

    public void add(JoinEvent joinEvent){

        joinEventMap.put(joinEvent.getJoinId(), joinEvent);
    }

    public JoinEvent get(int joinId){
        return joinEventMap.get(joinId);
    }

    public void delete(int joinId){
        joinEventMap.remove(joinId);
    }

    public Map<Integer, JoinEvent> getJoinEvents(){
        return joinEventMap;
    }
}

