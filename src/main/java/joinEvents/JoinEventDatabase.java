package joinEvents;

import java.util.HashMap;
import java.util.Map;

public class JoinEventDatabase {
    private Map<Integer, JoinEvent> joinEventMap = new HashMap<>();

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

