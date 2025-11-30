package joinEvents;

import events.EventDatabase;
import java.util.List;

public class JoinEventController {

    public List<JoinEvent> getJoinEventsById(int userId) {

        // i ahve a session tracker so i get the user session then get the id and then thats fine that makes this issue a lot easier
        // then just make it skim and return the list at the end and im basically done with this and then just a method for leaving an registered event
        // which is get the event and leave and remove it off the lsit
        // same concept with adding which ill make soon
        return eventDatabase.getEvents().values().stream().filter(event -> event.getAttendees().stream().anyMatch(user -> user.getUserId() == userId)).toList();

    }
}
