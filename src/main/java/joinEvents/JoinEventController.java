package joinEvents;

import events.Event;
import events.EventDatabase;
import events.EventDatabaseStorage;
import user.User;
import user.UserController;

import java.util.List;
import java.util.stream.Collectors;

public class JoinEventController {

    private final EventDatabase eventDatabase;
    private final JoinEventDatabase joinEventDatabase;

    // Load everything properly at startup
    public JoinEventController(EventDatabase eventDatabase, JoinEventDatabase joinEventDatabase) {
        this.eventDatabase = eventDatabase;
        this.joinEventDatabase = joinEventDatabase;

        EventDatabaseStorage.load();
        JoinEventDatabaseStorage.createTableIfNotExist();
        joinEventDatabase.setJoinEvents(JoinEventDatabaseStorage.load());
    }

    // Returns all events a user has joined by lookup
    public List<Event> getJoinedEvents(int userId) {
        return joinEventDatabase.getJoinEvents().values().stream()
                .filter(join -> join.getUserId() == userId)
                .map(join -> eventDatabase.get(join.getEventId()))
                .collect(Collectors.toList());
    }

    // + Join event
    public boolean joinEvent(int eventId) {
        User user = UserController.getLoggedInUser();
        if (user == null) return false;

        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        // Check already joined
        boolean alreadyJoined = joinEventDatabase.getJoinEvents()
                .values().stream()
                .anyMatch(j -> j.getEventId() == eventId && j.getUserId() == user.getUserId());

        if (alreadyJoined) return false;

        // Check capacity
        long current = joinEventDatabase.getJoinEvents()
                .values().stream()
                .filter(j -> j.getEventId() == eventId)
                .count();

        if (current >= event.getMaxAttendees()) return false;

        // Create join record (+)
        int joinId = joinEventDatabase.generateNewId();
        JoinEvent join = new JoinEvent(joinId, user.getUserId(), eventId);

        joinEventDatabase.add(join);
        JoinEventDatabaseStorage.save(joinEventDatabase.getJoinEvents());

        return true;
    }

    // - Leave event
    public boolean leaveEvent(int eventId) {
        User user = UserController.getLoggedInUser();
        if (user == null) return false;

        Integer joinToRemove = joinEventDatabase.getJoinEvents().values()
                .stream()
                .filter(j -> j.getEventId() == eventId && j.getUserId() == user.getUserId())
                .map(JoinEvent::getJoinId)
                .findFirst().orElse(null);

        if (joinToRemove == null) return false;

        joinEventDatabase.delete(joinToRemove);
        JoinEventDatabaseStorage.save(joinEventDatabase.getJoinEvents());

        return true;
    }
}
