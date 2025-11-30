package events;

import user.User;
import user.UserController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


public class EventController {

    private EventDatabase eventDatabase;

    public boolean validateEventEntry(String eventTitle, String location, LocalDate startDate, LocalDate endDate,LocalTime startTime,LocalTime endTime,int maxAttendees, List<User> initialAttendees, String description) {

        if (eventTitle == null || eventTitle.isBlank()) return false;
        if (location == null || location.isBlank()) return false;
        if (startDate == null || endDate == null) return false;
        if (startDate.isBefore(LocalDate.now())) return false;
        if (endDate.isBefore(startDate)) return false;
        if (startTime.isBefore(LocalTime.now())) return false;
        if (endTime.isBefore(LocalTime.now())) return false;
        if (maxAttendees <= 0) return false;

        if (initialAttendees == null) return false;
        if (initialAttendees.size() > maxAttendees) return false;


        long unique = initialAttendees.stream().distinct().count();
        if (unique != initialAttendees.size()) return false;

        if (eventDatabase.existsByName(eventTitle)) return false;

        return true;
    }

    public boolean createEvent(String eventTitle,String location, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int maxAttendees, List<User> initialAttendees, String description){

        if (!validateEventEntry(eventTitle,location,startDate,endDate,startTime,endTime,maxAttendees,initialAttendees,description)){
            return false;
        }

        int eventId = eventDatabase.generateNewId();
        int creatorId = UserController.getLoggedInUser().getUserId();


        Event event = new Event(eventId,creatorId,eventTitle,location,startDate,endDate,startTime,endTime,maxAttendees,initialAttendees,description);
        eventDatabase.add(event);
        EventDatabaseStorage.save(eventDatabase.getEvents());

        return true;
    }

    public boolean deleteEvent(int eventId) {
        Event event = eventDatabase.get(eventId);
        if (event == null) return false;

        eventDatabase.delete(eventId);
        EventDatabaseStorage.save(eventDatabase.getEvents());
        return true;
    }

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


    public List<Event> getUsersEvents() {
        int userId= UserController.getLoggedInUser().getUserId();

        List<Event> myEvents = eventDatabase.getEvents().values().stream().filter(e -> e.getCreatorId() == userId).collect(Collectors.toList());

        return myEvents;
    }


    public void importAttendees(List<Event> events) {

        //figure this logic out later
    }

    public List<Event> getFilteredEvents() {
        // need to get the filters like if the user wants a certain location maybe events before a certain day and time stuff like that and key word searching
        //to the apply them to the search to shorten them down
        // and then send a list which then javaFx can work with and display in like a dropdown box
        return List.of();
    }


    public List<Event> getEventInfo(){System.out.print("working on it ");}

}
