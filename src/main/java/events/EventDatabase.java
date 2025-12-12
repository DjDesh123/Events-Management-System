package events;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EventDatabase {
    private Map<Integer, Event> eventMap;
    private AtomicInteger idCounter = new AtomicInteger(0);

    public EventDatabase() {
        // Load from storage
        eventMap = EventDatabaseStorage.load();

        // Update idCounter to the highest existing ID
        eventMap.keySet().stream().max(Integer::compareTo).ifPresent(idCounter::set);
    }

    //generate a new unique event ID
    public int generateNewId() {
        return idCounter.incrementAndGet();
    }

    // Add or update event
    public void save(Event event) {
        if (event.getEventId() > idCounter.get()) {
            idCounter.set(event.getEventId());
        }
        eventMap.put(event.getEventId(), event);
        EventDatabaseStorage.save(eventMap);
    }

    // Add new event without saving immediately
    public void add(Event event) {
        eventMap.put(event.getEventId(), event);
    }

    // Get a single event
    public Event get(int eventId) {
        return eventMap.get(eventId);
    }

    // Delete an event
    public void delete(int eventId) {
        eventMap.remove(eventId);
        EventDatabaseStorage.save(eventMap);
    }

    // Returns all events
    public Map<Integer, Event> getAllEvents() {
        return new HashMap<>(eventMap);
    }

    // Check duplicate names
    public boolean existsByName(String name) {
        return eventMap.values().stream().anyMatch(e -> e.getEventTitle().equalsIgnoreCase(name));
    }
}
