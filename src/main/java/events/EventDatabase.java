package events;

import events.Event;
import java.util.HashMap;
import java.util.Map;



public class EventDatabase {
    private Map<Integer,Event> eventMap = new HashMap();

    //adds the event to the local database
    public void add(Event event){
        eventMap.put(event.getEventId(),event);
    }

    // gets the certain event infomation
    public Event get(int EventId){
        return eventMap.get(EventId);
    }

    // deletes the event from the local database
    public void delete(int EventId){
        eventMap.remove(EventId);
    }

    // gets all the event
    public Map<Integer,Event> getEvents(){
        return eventMap;
    }
}


