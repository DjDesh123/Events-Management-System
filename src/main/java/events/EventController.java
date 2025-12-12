package events;

import user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventController {

    private final EventDatabase eventDatabase;

    public EventController() {
        eventDatabase = new EventDatabase();
        EventDatabaseStorage.load(); // Load existing events
    }


    public String createEvent(User creator, String eventTitle, String location, LocalDate startDate, LocalDate endDate,
                              LocalTime startTime, LocalTime endTime, int maxAttendees,
                              List<User> initialAttendees, String description) {

        // basic required field checks
        if (eventTitle == null || eventTitle.isBlank()) return "Event title is required!";
        if (location == null || location.isBlank()) return "Location is required!";
        if (description == null || description.isBlank()) return "Description is required!";
        if (startDate == null) return "Start date is required!";
        if (endDate == null) return "End date is required!";
        if (startTime == null) return "Start time is required!";
        if (endTime == null) return "End time is required!";
        if (maxAttendees <= 0) return "Capacity must be greater than 0!";

        // ensures dates and time are valid
        if (!isValidDate(startDate)) return "Start date is invalid!";
        if (!isValidDate(endDate)) return "End date is invalid!";
        if (startDate.isAfter(endDate)) return "Start date cannot be after end date!";
        if (startTime.isAfter(endTime)) return "Start time cannot be after end time!";

        // validates attendee
        if (initialAttendees == null) initialAttendees = List.of();
        if (initialAttendees.size() > maxAttendees) return "Initial attendees exceed capacity!";
        long unique = initialAttendees.stream().distinct().count();
        if (unique != initialAttendees.size()) return "Duplicate users in initial attendees list!";

        // dupe event title
        if (eventDatabase.existsByName(eventTitle)) return "Event title already exists!";

        // create event object
        int eventId = eventDatabase.generateNewId();
        int creatorId = creator.getUserId();

        Event event = new Event(eventId, creatorId, eventTitle, location,
                startDate, endDate, startTime, endTime, maxAttendees, initialAttendees, description);

        // stores the event locally and externally
        eventDatabase.add(event);
        EventDatabaseStorage.save(eventDatabase.getAllEvents());
        return null;
    }

    // ensures the data exists ont he calendar
    private boolean isValidDate(LocalDate date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        return month >= 1 && month <= 12 && day > 0 && day <= date.lengthOfMonth();
    }

    // deletes the event by the id
    public boolean deleteEvent(int eventId) {
        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        eventDatabase.delete(eventId);
        EventDatabaseStorage.save(eventDatabase.getAllEvents());
        return true;
    }

    // editing existing event details
    public boolean editEvent(int eventId, String eventTitle, String location, LocalDate startDate, LocalDate endDate,
                             LocalTime startTime, LocalTime endTime, int maxAttendees, String description) {

        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        // validates basic rules
        if (eventTitle == null || eventTitle.isBlank()) return false;
        if (startDate.isAfter(endDate)) return false;
        if (maxAttendees < event.getAttendee().size()) return false;

        //updates the fields
        event.setEventTitle(eventTitle);
        event.setLocation(location);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setMaxAttendees(maxAttendees);
        event.setDescription(description);

        EventDatabaseStorage.save(eventDatabase.getAllEvents());
        return true;
    }

    // gets the upcoming events
    public List<Event> getUpcomingEvents() {
        LocalDate today = LocalDate.now();

        return eventDatabase.getAllEvents().values().stream()
                .filter(e -> !e.getStartDate().isBefore(today))
                .sorted((a, b) -> {
                    int dateCompare = a.getStartDate().compareTo(b.getStartDate());
                    return dateCompare != 0 ? dateCompare : a.getStartTime().compareTo(b.getStartTime());
                })
                .collect(Collectors.toList());
    }

    // get events created bt a specific user
    public List<Event> getUsersEvents(User user) {
        int userId = user.getUserId();
        return eventDatabase.getAllEvents().values().stream()
                .filter(e -> e.getCreatorId() == userId)
                .collect(Collectors.toList());
    }

    // apply filters
    public List<Event> getFilteredEvents(Filter filter) {
        return eventDatabase.getAllEvents().values().stream()
                .filter(event -> filter.location == null || event.getLocation().equalsIgnoreCase(filter.location))
                .filter(event -> filter.keyword == null || event.getEventTitle().toLowerCase().contains(filter.keyword.toLowerCase())
                        || event.getDescription().toLowerCase().contains(filter.keyword.toLowerCase()))
                .filter(event -> filter.startDate == null || !event.getStartDate().isBefore(filter.startDate))
                .filter(event -> filter.endDate == null || !event.getEndDate().isAfter(filter.endDate))
                .filter(event -> filter.startTime == null || !event.getStartTime().isBefore(filter.startTime))
                .filter(event -> filter.endTime == null || !event.getEndTime().isAfter(filter.endTime))
                .collect(Collectors.toList());
    }
}
