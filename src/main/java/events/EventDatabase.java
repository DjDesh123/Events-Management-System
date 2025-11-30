package events;

import events.Event;
import user.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class EventDatabase {
    private Map<Integer,Event> eventMap = new HashMap();
    private AtomicInteger idCounter = new AtomicInteger(0);

    public int generateNewId() {
        return idCounter.incrementAndGet();
    }

    //adds the users to the local database
    public void save (Event event){
        if (event.getEventId() > idCounter.get()) {
            idCounter.set(event.getEventId());
        }
        eventMap.put(event.getEventId(),event);
    }


    //adds the event to the local database
    public void add(Event event){
        eventMap.put(event.getEventId(),event);
    }

    // gets the certain event infomation
    public Event get(int eventId){
        return eventMap.get(eventId);
    }

    // deletes the event from the local database
    public void delete(int EventId){
        eventMap.remove(EventId);
    }

    // gets all the event
    public Map<Integer,Event> getEvents(){
        return eventMap;
    }

    // check duplicate names
    public boolean existsByName(String name){
        return eventMap.values().stream().anyMatch(e -> e.getEventTitle().equalsIgnoreCase(name));
    }

    // get upcoming events
    public Map<Integer, Event> getUpcoming() {
        Map<Integer, Event> upcoming = new HashMap<>();

        for (Event e : eventMap.values()) {
            if (e.getEndDate().isAfter(LocalDate.now())) {
                upcoming.put(e.getEventId(), e);
            }
        }
        return upcoming;
    }
}


