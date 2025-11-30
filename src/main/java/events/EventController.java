package events;

import events.EventDatabase;
import user.User;
import user.UserController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


public class EventController {

    private EventDatabase eventDatabase = new  EventDatabase();

    //constructor
    public EventController() {
        //loads of creates a local event database
        eventDatabase = new EventDatabase();
        //load events from file storage if exists
        EventDatabaseStorage.load();
    }

    //validates before creating an event
    public boolean validateEventEntry(String eventTitle, String location, LocalDate startDate, LocalDate endDate,LocalTime startTime,LocalTime endTime,int maxAttendees, List<User> initialAttendees, String description) {
        //checks if reileds are not empty
        if (eventTitle == null || eventTitle.isBlank()) return false;
        if (location == null || location.isBlank()) return false;
        if (startDate == null || endDate == null) return false;
        //checks if dates are legal
        if (startDate.isBefore(LocalDate.now())) return false;
        if (endDate.isBefore(startDate)) return false;
        //checks if times are legal
        if (startTime.isBefore(LocalTime.now())) return false;
        if (endTime.isBefore(LocalTime.now())) return false;

        //checks if the attendee number is valid
        if (maxAttendees <= 0) return false;

        if (initialAttendees == null) return false;
        if (initialAttendees.size() > maxAttendees) return false;

        // ensures that there are no dupes users inside the attendee list
        long unique = initialAttendees.stream().distinct().count();
        if (unique != initialAttendees.size()) return false;

        // checks events doesnt already exist by name
        if (eventDatabase.existsByName(eventTitle)) return false;

        return true;
    }

    //creates events
    public boolean createEvent(String eventTitle,String location, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int maxAttendees, List<User> initialAttendees, String description){

        //validates events fields
        if (!validateEventEntry(eventTitle,location,startDate,endDate,startTime,endTime,maxAttendees,initialAttendees,description)){
            return false;
        }

        //generates unique id
        int eventId = eventDatabase.generateNewId();

        // gets the users id
        int creatorId = UserController.getLoggedInUser().getUserId();

        //builds the event object
        Event event = new Event(eventId,creatorId,eventTitle,location,startDate,endDate,startTime,endTime,maxAttendees,initialAttendees,description);

        //adds to database and saves
        eventDatabase.add(event);
        EventDatabaseStorage.save(eventDatabase.getEvents());

        return true;
    }

    // delets events
    public boolean deleteEvent(int eventId) {
        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        eventDatabase.delete(eventId);
        EventDatabaseStorage.save(eventDatabase.getEvents());
        return true;
    }

    //edit event details
    public boolean editEvent(int eventId, String eventTitle, String location, LocalDate startDate, LocalDate endDate,LocalTime startTime,LocalTime endTime ,int maxAttendees,String description) {

        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        // validations
        if (eventTitle == null || eventTitle.isBlank()) return false;
        if (startDate.isAfter(endDate)) return false;
        if (maxAttendees < event.getAttendee().size()) return false;

        // Apply changes
        event.setEventTitle(eventTitle);
        event.setLocation(location);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setDescription(description);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setMaxAttendees(maxAttendees);

        //update the local here too

        EventDatabaseStorage.save(eventDatabase.getEvents());
        return true;
    }

    //gets the upcoming events sorted by the time and the date
    public List<Event> getUpcomingEvents() {
        LocalDate today = LocalDate.now();

        return eventDatabase.getEvents().values().stream().filter(e -> !e.getStartDate().isBefore(today)).sorted((a, b) -> {
                    int dateCompare = a.getStartDate().compareTo(b.getStartDate());
                    if (dateCompare != 0) {
                        return dateCompare;
                    }
                    return a.getStartTime().compareTo(b.getStartTime());
                })
                .collect(Collectors.toList());

    }

    // gets all events created by the logged in user
    public List<Event> getUsersEvents() {
        int userId= UserController.getLoggedInUser().getUserId();

        List<Event> myEvents = eventDatabase.getEvents().values().stream().filter(e -> e.getCreatorId() == userId).collect(Collectors.toList());

        return myEvents;
    }


    public void importAttendees(List<Event> events) {

        //figure this logic out later
    }

    // filters events by using Filter
    public List<Event> getFilteredEvents(Filter filter) {
        return eventDatabase.getEvents().values().stream()
                .filter(event -> filter.location == null
                        || event.getLocation().equalsIgnoreCase(filter.location))

                .filter(event -> filter.keyword == null
                        || event.getEventTitle().toLowerCase().contains(filter.keyword.toLowerCase())
                        || event.getDescription().toLowerCase().contains(filter.keyword.toLowerCase()))

                .filter(event -> filter.startDate == null
                        || !event.getStartDate().isBefore(filter.startDate))

                .filter(event -> filter.endDate == null
                        || !event.getEndDate().isAfter(filter.endDate))

                .filter(event -> filter.startTime == null
                        || !event.getStartTime().isBefore(filter.startTime))

                .filter(event -> filter.endTime == null
                        || !event.getEndTime().isAfter(filter.endTime))
                .collect(Collectors.toList());
    }


    public List<Event> getEventInfo(){System.out.print("working on it ");
        return eventDatabase.getEvents().values().stream().collect(Collectors.toList());
    }

}
