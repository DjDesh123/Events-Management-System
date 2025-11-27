package events;

import events.Event;
import java.util.HashMap;
import java.util.Map;



public class EventDatabase {
    private Map<Integer,Event> events = new HashMap();

    //adds the users to the local database
    public void add(Event event){
        events.put(event.getEventId(),event);
    }

    // gets the certain users infomation
    public Event get(int EventId){
        return events.get(EventId);
    }

    // deletes the user from the local database
    public void delete(int EventId){
        events.remove(EventId);
    }

    // gets all the users
    public Map<Integer,Event> getEvents(){
        return events;
    }
}


