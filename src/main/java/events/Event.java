package events;

import user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Event{
    // core event details
    private int eventId;
    private int creatorId;
    private String eventTitle;
    private String location;
    public LocalDate startDate;
    public LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxAttendees;
    private List<User> attendee;
    private String description;

    // event constructor
    public Event(int  eventId, int creatorId, String eventTitle, String location, LocalDate startDate, LocalDate endDate,LocalTime startTime,LocalTime endTime,int maxAttendees, List<User> initialAttendees, String description) {
        this.eventId = eventId;
        this.creatorId = creatorId;
        this.eventTitle = eventTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxAttendees = Math.max(0, maxAttendees);

        // ensure attendee list exists
        if (initialAttendees != null) {
            this.attendee = new ArrayList<>(initialAttendees);
        } else {
            this.attendee = new ArrayList<>();
        }
        this.description = description == null ? "" : description;
    }

    // prevents negative attendee limits
    public boolean isFull() {
        return attendee.size() >= maxAttendees;
    }

    // attempts to add a new attendee while checking if its full
    public boolean addAttendee(User user){
        if(isFull()) return false;
        return attendee.add(user);
    }


    //getters
    public int getEventId() {
        return eventId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getMaxAttendees() {return maxAttendees;}

    public List<User> getAttendee() {
        return attendee;
    }

    public String getDescription() {
        return description;
    }

    //setters

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
